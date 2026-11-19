import React from 'react'
import { createRoot } from 'react-dom/client'
import { MemoryRouter } from 'react-router-dom'
import { Dashboard } from '../../src/pages/Dashboard/Dashboard.jsx'
import '../../src/index.css'

window.pruebaDashboard = { rutas: [], corte: 25, fallar: false, comprobaciones: 0 }
const fila = id => ({ idBitacora: id, modulo: 'Autenticación', accion: 'Inicio de sesión',
    resultado: 'Exitoso', usuario: 'prueba@ejemplo.cr', fechaHora: '2026-11-14T11:30:00',
    descripcion: `Inicio de sesión exitoso. Evento ${id}` })
const cliente = { async request(ruta) {
    const estado = window.pruebaDashboard
    estado.rutas.push(ruta)
    const url = new URL(ruta, window.location.origin)
    if (url.pathname === '/gateway/menu') return []
    if (url.pathname === '/gateway/fin/notificar/historial') return { items: [] }
    if (url.pathname === '/gateway/admin/bitacora') {
        if (url.searchParams.get('size') === '1') estado.comprobaciones++
        if (estado.fallar) throw new Error('Servicio no disponible')
        const corte = Number(url.searchParams.get('hastaId') ?? estado.corte)
        const size = Number(url.searchParams.get('size'))
        const page = Number(url.searchParams.get('page'))
        return { hastaId: corte, total: corte,
            items: Array.from({ length: corte }, (_, i) => fila(corte - i)).slice(page * size, (page + 1) * size) }
    }
    return []
} }
const usuario = { usuarioID: 'prueba@ejemplo.cr', nombre: 'Prueba', rol: 'ADMINISTRADOR',
    modulos: ['ADM3', 'ADM4', 'ADM9', 'ADM10', 'ADM12', 'ADM22'].map(idModulo => ({ idModulo })) }
createRoot(document.getElementById('root')).render(
    <MemoryRouter><Dashboard cliente={cliente} usuario={usuario} /></MemoryRouter>)
