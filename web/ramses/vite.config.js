import { defineConfig } from 'vite'
import envURL from '../../tools/env-url.cjs'
import gatewayConfig from '../../tools/gateway-env.cjs'

const frontend = envURL('RAMSES_FRONTEND_BASE_URL')
export default defineConfig({
  server: {
    host: frontend.hostname,
    port: Number(frontend.port || (frontend.protocol === 'https:' ? 443 : 80)),
    https: frontend.protocol === 'https:' ? {} : undefined,
    strictPort: true,
    proxy: { '/gateway': gatewayConfig.gatewayBaseURL },
  },
})
