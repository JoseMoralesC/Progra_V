const frontend = require('./env-url.cjs')('FRONTEND_BASE_URL')

module.exports = {
    frontendBaseURL: frontend.origin,
    frontendHost: frontend.hostname,
    frontendPort: Number(frontend.port || (frontend.protocol === 'https:' ? 443 : 80)),
    frontendHttps: frontend.protocol === 'https:',
}
