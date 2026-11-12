import { useEffect, useId, useRef } from 'react'

export function ConfirmarEliminacion({
    onConfirmar,
    onCancelar,
    titulo = 'Confirmar eliminación',
    mensaje = '¿Realmente desea eliminar el elemento seleccionado?',
    confirmar = 'Sí',
    cancelar = 'No',
    ocupado = false
}) {
    const dialogo = useRef(null)
    const tituloId = useId()
    useEffect(() => {
        const elemento = dialogo.current
        elemento.showModal()
        return () => elemento.close()
    }, [])
    return <dialog ref={dialogo} className="panel" aria-labelledby={tituloId}
        onCancel={evento => { evento.preventDefault(); if (!ocupado) onCancelar() }}>
        <h2 id={tituloId}>{titulo}</h2>
        <p>{mensaje}</p>
        <div className="instituciones-acciones">
            <button type="button" className="btn btn-danger" onClick={onConfirmar} disabled={ocupado}>{confirmar}</button>
            <button type="button" className="btn btn-soft" onClick={onCancelar} disabled={ocupado} autoFocus>{cancelar}</button>
        </div>
    </dialog>
}
