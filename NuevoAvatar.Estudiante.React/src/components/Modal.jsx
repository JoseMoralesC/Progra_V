import { useEffect, useId, useRef } from 'react'
import './modal.css'

export function Modal({ titulo, onCerrar, ocupado = false, children }) {
    const dialogo = useRef(null)
    const tituloId = useId()

    useEffect(() => {
        const elemento = dialogo.current
        const anterior = document.activeElement
        elemento.showModal()
        return () => {
            elemento.close()
            if (anterior instanceof HTMLElement && anterior.isConnected) anterior.focus()
        }
    }, [])

    return <dialog ref={dialogo} className="panel mantenimiento-modal" aria-labelledby={tituloId}
        onCancel={evento => {
            evento.preventDefault()
            if (!ocupado) onCerrar()
        }}>
        <div className="mantenimiento-modal__header">
            <h2 id={tituloId}>{titulo}</h2>
            <button type="button" className="mantenimiento-modal__close" aria-label="Cerrar ventana"
                onClick={onCerrar} disabled={ocupado}>×</button>
        </div>
        <div className="mantenimiento-modal__content">{children}</div>
    </dialog>
}
