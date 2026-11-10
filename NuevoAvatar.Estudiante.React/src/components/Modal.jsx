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
        <h2 id={tituloId}>{titulo}</h2>
        {children}
    </dialog>
}
