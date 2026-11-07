import { useEffect, useRef } from 'react'

export function ConfirmarEliminacion({ onConfirmar, onCancelar }) {
    const dialogo = useRef(null)
    useEffect(() => {
        const elemento = dialogo.current
        elemento.showModal()
        return () => elemento.close()
    }, [])
    return <dialog ref={dialogo} className="panel" aria-label="Confirmar eliminación"
        onCancel={evento => { evento.preventDefault(); onCancelar() }}>
        <p>¿Realmente desea eliminar el elemento seleccionado?</p>
        <div className="instituciones-acciones">
            <button type="button" className="btn btn-danger" onClick={onConfirmar}>Sí</button>
            <button type="button" className="btn btn-soft" onClick={onCancelar} autoFocus>No</button>
        </div>
    </dialog>
}
