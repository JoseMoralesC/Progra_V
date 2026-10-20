package cr.ac.cuc.nuevoavatar.persona4;

import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import cr.ac.cuc.nuevoavatar.integracion.DatosRemotos;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import static org.mockito.Mockito.*;

/** Simulo los servicios remotos con los datos de prueba locales; no abro conexiones HTTP. */
@org.springframework.test.context.TestPropertySource(properties={
    "spring.datasource.url=jdbc:h2:mem:pruebaExpediente;MODE=MSSQLServer;DB_CLOSE_DELAY=-1",
    "spring.datasource.username=sa", "spring.datasource.password=",
    "spring.datasource.driver-class-name=org.h2.Driver", "spring.sql.init.mode=always"
})
public abstract class PruebaRemotaBase {
    @Autowired protected JdbcTemplate fixture;
    @MockitoBean protected DatosRemotos remotos;
    @MockitoBean(name="verificarBase") private InitializingBean verificacionBase;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void configurarRemotos() throws Exception {
        try(var connection=java.util.Objects.requireNonNull(fixture.getDataSource()).getConnection()) {
            if(!connection.getMetaData().getURL().startsWith("jdbc:h2:mem:"))
                throw new IllegalStateException("Estas pruebas solo admiten H2 en memoria");
        }
        Map<String,Object> contratos;
        try(var input=getClass().getResourceAsStream("/contratos.json")) {
            contratos=new com.fasterxml.jackson.databind.ObjectMapper().readValue(input,Map.class);
        }
        when(remotos.contrato(anyString())).thenAnswer(i->contratos.get(i.getArgument(0)));
        when(remotos.listar(anyString(),anyString(),anyMap())).thenAnswer(i->{
            String recurso=i.getArgument(1);
            Map<String,Object> filtros=i.getArgument(2);
            String tabla=tabla(contratos,i.getArgument(0),recurso);
            var condiciones=new ArrayList<String>();var valores=new ArrayList<Object>();
            filtros.forEach((k,v)->{condiciones.add(k+"=?");valores.add(v);});
            return fixture.queryForList("SELECT * FROM "+tabla+(condiciones.isEmpty()?"":" WHERE "+String.join(" AND ",condiciones)),valores.toArray());
        });
        when(remotos.buscar(anyString(),anyString(),anyString(),any())).thenAnswer(i->
            remotos.listar(i.getArgument(0),i.getArgument(1),Map.of(i.getArgument(2),i.getArgument(3))).stream().findFirst());
        doAnswer(i->{
            String servicio=i.getArgument(0),recurso=i.getArgument(1);Object id=i.getArgument(2);
            var contrato=(Map<String,Object>)contratos.get(servicio);
            for(var ref:(List<Map<String,Object>>)contrato.get("references")) {
                if(!recurso.equals(ref.get("resource")))continue;
                String tabla=tabla(contratos,ref.get("service").toString(),ref.get("dependent").toString());
                Integer total=fixture.queryForObject("SELECT COUNT(*) FROM "+tabla+" WHERE "+ref.get("field")+"=?",Integer.class,id);
                if(total!=null && total>0)throw new ResponseStatusException(HttpStatus.CONFLICT,"Referencia remota en "+ref.get("service"));
            }
            return null;
        }).when(remotos).sinReferencias(anyString(),anyString(),any());
    }

    @SuppressWarnings("unchecked")
    private String tabla(Map<String,Object> contratos,String servicio,String recurso) {
        var contrato=(Map<String,Object>)contratos.get(servicio);
        var recursos=(Map<String,Object>)contrato.get("resources");
        return ((Map<String,Object>)recursos.get(recurso)).get("table").toString().replace("[","").replace("]","");
    }
}
