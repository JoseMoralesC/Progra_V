const { existsSync } = require('node:fs')
const { resolve } = require('node:path')

const envFile = resolve(__dirname, '../.env')
if (existsSync(envFile)) process.loadEnvFile(envFile)

module.exports = function envURL(name) {
    try {
        const url = new URL(process.env[name])
        if (!['http:', 'https:'].includes(url.protocol) || url.username ||
            url.password || url.pathname !== '/' || url.search || url.hash) throw new Error()
        return url
    } catch {
        throw new Error(`Configura ${name} en el .env raiz con un origen HTTP(S) valido, sin credenciales, rutas ni parametros.`)
    }
}
