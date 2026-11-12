import { useCallback, useEffect, useLayoutEffect, useRef, useState } from 'react'
import { createPortal } from 'react-dom'
import './row-actions-menu.css'

export function RowActionsMenu({ rowLabel = 'registro', actions = [] }) {
    const trigger = useRef(null)
    const menu = useRef(null)
    const closeTimer = useRef(null)
    const pinned = useRef(false)
    const itemRefs = useRef([])
    const [open, setOpen] = useState(false)
    const [position, setPosition] = useState(null)
    const opciones = actions.filter(action => action && typeof action.onSelect === 'function')

    const cerrar = useCallback((devolverFoco = false) => {
        window.clearTimeout(closeTimer.current)
        pinned.current = false
        setOpen(false)
        if (devolverFoco) window.requestAnimationFrame(() => trigger.current?.focus())
    }, [])

    const programarCierre = useCallback(() => {
        window.clearTimeout(closeTimer.current)
        closeTimer.current = window.setTimeout(() => {
            if (!pinned.current) setOpen(false)
        }, 180)
    }, [])

    const abrir = useCallback((fijar = false, enfocarPrimera = false) => {
        window.clearTimeout(closeTimer.current)
        if (fijar) pinned.current = true
        setOpen(true)
        if (enfocarPrimera) window.requestAnimationFrame(() => itemRefs.current[0]?.focus())
    }, [])

    const actualizarPosicion = useCallback(() => {
        const rect = trigger.current?.getBoundingClientRect()
        if (!rect) return
        const menuRect = menu.current?.getBoundingClientRect()
        const width = menuRect?.width || 176
        const height = menuRect?.height || opciones.length * 40
        const left = Math.max(8, Math.min(rect.right - width, window.innerWidth - width - 8))
        let top = rect.bottom + 6
        if (top + height > window.innerHeight - 8) top = Math.max(8, rect.top - height - 6)
        setPosition({ top, left })
    }, [opciones.length])

    useLayoutEffect(() => {
        if (open) actualizarPosicion()
        else setPosition(null)
    }, [actualizarPosicion, open])

    useEffect(() => {
        if (!open) return undefined
        function pointerDown(event) {
            if (trigger.current?.contains(event.target) || menu.current?.contains(event.target)) return
            cerrar()
        }
        function focusIn(event) {
            if (trigger.current?.contains(event.target) || menu.current?.contains(event.target)) return
            cerrar()
        }
        function keyDown(event) {
            if (event.key === 'Escape') {
                event.preventDefault()
                cerrar(true)
            }
        }
        document.addEventListener('pointerdown', pointerDown)
        document.addEventListener('focusin', focusIn)
        document.addEventListener('keydown', keyDown)
        window.addEventListener('resize', actualizarPosicion)
        window.addEventListener('scroll', actualizarPosicion, true)
        return () => {
            document.removeEventListener('pointerdown', pointerDown)
            document.removeEventListener('focusin', focusIn)
            document.removeEventListener('keydown', keyDown)
            window.removeEventListener('resize', actualizarPosicion)
            window.removeEventListener('scroll', actualizarPosicion, true)
        }
    }, [actualizarPosicion, cerrar, open])

    function navegarMenu(event, index) {
        if (event.key === 'ArrowDown' || event.key === 'ArrowUp') {
            event.preventDefault()
            const delta = event.key === 'ArrowDown' ? 1 : -1
            itemRefs.current[(index + delta + opciones.length) % opciones.length]?.focus()
        } else if (event.key === 'Home' || event.key === 'End') {
            event.preventDefault()
            itemRefs.current[event.key === 'Home' ? 0 : opciones.length - 1]?.focus()
        }
    }

    if (!opciones.length) return null

    return <>
        <button ref={trigger} type="button" className="btn btn-soft row-actions-trigger"
            aria-haspopup="menu" aria-expanded={open} aria-label={`Acciones para ${rowLabel}`}
            onMouseEnter={() => { if (!pinned.current) abrir() }}
            onMouseLeave={programarCierre}
            onFocus={() => { if (!pinned.current) abrir() }}
            onClick={() => pinned.current ? cerrar() : abrir(true)}
            onKeyDown={event => {
                if (event.key === 'ArrowDown') {
                    event.preventDefault()
                    abrir(true, true)
                }
            }}>
            Acciones <span aria-hidden="true">⌄</span>
        </button>
        {open && createPortal(
            <div ref={menu} className="row-actions-menu" role="menu" aria-label={`Acciones para ${rowLabel}`}
                style={position ? { top: position.top, left: position.left } : { visibility: 'hidden' }}
                onMouseEnter={() => window.clearTimeout(closeTimer.current)}
                onMouseLeave={programarCierre}>
                {opciones.map((action, index) => <button key={action.label} ref={element => { itemRefs.current[index] = element }}
                    type="button" role="menuitem"
                    className={`row-actions-menu__item${action.danger ? ' row-actions-menu__item--danger' : ''}`}
                    onKeyDown={event => navegarMenu(event, index)}
                    onClick={() => { cerrar(); action.onSelect() }}>
                    {action.label}
                </button>)}
            </div>, document.body
        )}
    </>
}
