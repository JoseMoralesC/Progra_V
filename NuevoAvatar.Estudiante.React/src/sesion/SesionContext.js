import { createContext, useContext } from 'react'

export const SesionContext = createContext(null)

export function useSesion() {
    const sesion = useContext(SesionContext)
    if (!sesion) throw new Error('useSesion requiere SesionProvider.')
    return sesion
}
