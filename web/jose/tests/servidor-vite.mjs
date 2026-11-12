import path from 'node:path'
import { createServer } from 'vite'

// Mantengo el servidor dentro del ejecutor para poder cerrarlo también en Windows,
// sin depender de terminar el árbol cmd -> npm -> Vite mediante taskkill.
export default async function iniciarServidor(config) {
    const root = path.dirname(config.configFile)
    const baseURL = new URL(config.projects[0].use.baseURL)
    const servidor = await createServer({
        root,
        clearScreen: false,
        server: {
            host: baseURL.hostname,
            port: Number(baseURL.port),
            strictPort: true,
            open: false,
        },
    })
    try {
        await servidor.listen()
    } catch (error) {
        await servidor.close()
        throw error
    }
    return async () => { await servidor.close() }
}
