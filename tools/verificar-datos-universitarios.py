"""Verifica SQL y, con --funcional, autentica las ocho cuentas sin exponer claves ni tokens."""
import argparse
from collections import Counter
from datetime import datetime, timezone
import importlib.util
import json

import requests

spec=importlib.util.spec_from_file_location('renovacion',__import__('pathlib').Path(__file__).with_name('renovar-datos-universitarios.py'))
r=importlib.util.module_from_spec(spec)
spec.loader.exec_module(r)


def main():
    parser=argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--funcional',action='store_true')
    args=parser.parse_args()
    plan=json.loads(r.protect().decrypt((r.PRIVATE/'plan-cifrado.bin').read_bytes()))
    cfg=r.m.configuration()
    with r.m.connect(cfg,'master') as connection:
        q=connection.cursor()
        inv=r.inventory(q)
        checks=r.validate(q,inv,plan['seed'])
        remaining={k:sum(r.flagged(row) for row in v['rows']) for k,v in inv.items() if v['table']!='seguridad.parametro'}
        assert not any(remaining.values()), 'Se encontraron marcas artificiales residuales'
        for tab in ['seguridad.usuario','academico.Profesor','matricula.Estudiante']:
            rows=inv[r.OWNER[tab]+'/'+tab]['rows']
            for row in rows:
                email=row.get('email',row.get('Email'))
                ident=row.get('identificacion',row.get('Identificacion'))
                assert email.endswith('@cuc.cr') and r.re.fullmatch('[1-7][0-9]{8}',ident)
        connection.rollback()
    report={'verified_at_utc':datetime.now(timezone.utc).isoformat(),'sql_checks':checks,
            'remaining_test_rows':remaining,'accounts':[],'geography':[],
            'passwords_and_tokens_recorded':False}
    if args.funcional:
        http=requests.Session();http.trust_env=False
        base='http://127.0.0.1:5080'
        admin_headers=None
        for cred in plan['team_credentials']:
            response=http.post(base+'/gateway/auth/login',headers={'usuario':cred['usuario'],'contrasena':cred['contrasena_inicial']},timeout=45)
            assert response.status_code==201, 'Login: '+cred['usuario']+' HTTP '+str(response.status_code)
            body=response.json()
            access=body.get('access_token') or body.get('accessToken') or body.get('AccessToken')
            assert access, 'Respuesta sin access token'
            headers={'Authorization':'Bearer '+access}
            session=http.get(base+'/gateway/auth/sesion',headers=headers,timeout=45)
            assert session.status_code==200, 'No se pudo resolver identidad de '+cred['usuario']
            identity=session.json()
            role='ADMINISTRADOR' if cred['rol']=='ADMIN' else 'ESTUDIANTE'
            assert identity.get('rol',identity.get('Rol'))==role
            assert identity.get('usuarioID',identity.get('UsuarioID'))==cred['usuario']+'@cuc.cr'
            menu=http.get(base+'/gateway/menu',headers=headers,timeout=45)
            assert menu.status_code==200, 'No se pudo resolver menú'
            modules=[item.get('idModulo',item.get('IdModulo')) for item in menu.json()]
            assert 'ADM24' in modules
            users=http.get(base+'/gateway/admin/usuario',headers=headers,timeout=45)
            assert users.status_code==(200 if role=='ADMINISTRADOR' else 403), 'No se respeta separación de permisos'
            own_status=pre_status=None
            if role=='ESTUDIANTE':
                assert 'ADM4' not in modules
                student=identity.get('estudianteId',identity.get('EstudianteId'))
                assert student
                own=http.get(base+'/gateway/expediente/propio',headers=headers,timeout=45)
                assert own.status_code==200, 'Expediente propio no disponible'
                own_status=own.status_code
                pre=http.get(base+'/gateway/mat/prematricula/propio',headers=headers,timeout=45)
                assert pre.status_code==200, 'Prematrícula propia no disponible'
                pre_status=pre.status_code
            else:
                assert 'ADM4' in modules
                admin_headers=headers
            report['accounts'].append({'integrante':cred['integrante'],'usuario':cred['usuario'],
                'role':role,'login_http':response.status_code,'session_http':session.status_code,
                'users_http':users.status_code,'own_record_http':own_status,'prematricula_http':pre_status,
                'modules':modules})
        geo=json.loads((r.ROOT/'db/datos_realistas/geografia-2026.json').read_text(encoding='utf-8'))
        for province in geo['provincias']:
            pid=province['ProvinciaId']
            response=http.get(base+'/gateway/mat/cantones/'+str(pid),headers=admin_headers,timeout=45)
            assert response.status_code==200
            expected={c['CantonId'] for c in geo['cantones'] if c['ProvinciaId']==pid}
            actual=response.json()
            assert {c.get('cantonId',c.get('CantonId')) for c in actual}==expected
            assert all(c.get('provinciaId',c.get('ProvinciaId'))==pid for c in actual)
            report['geography'].append({'province_id':pid,'cantons':len(actual),'http':response.status_code,'matches_official_catalog':True})
        rejected=http.post(base+'/gateway/auth/login',headers={'usuario':'jose','contrasena':r.token()},timeout=45)
        assert rejected.status_code==401
        report['incorrect_password_http']=rejected.status_code
    report['passed']=True
    destination='verificacion.json' if args.funcional else 'verificacion-sql.json'
    (r.PUBLIC/destination).write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf-8')
    print('SQL: '+str(len(checks))+' comprobaciones correctas. Cuentas autenticadas: '+str(len(report['accounts']))+'. Provincias filtradas: '+str(len(report['geography']))+'.')


if __name__=='__main__':
    main()
