const gateway = require('./env-url.cjs')('GATEWAY_BASE_URL')

module.exports = {
    gatewayBaseURL: gateway.origin,
    gatewayHost: gateway.hostname,
    gatewayPort: Number(gateway.port || (gateway.protocol === 'https:' ? 443 : 80)),
}
