import gatewayConfig from '../tools/gateway-env.cjs'
import frontendConfig from '../tools/frontend-env.cjs'
import react from '@vitejs/plugin-react'
import { fileURLToPath } from 'node:url'
import { defineConfig } from 'vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  // El formulario compartido de otros módulos vive en web/jose. Se usa una sola copia de React.
  resolve: { dedupe: ['react', 'react-dom'] },
  server: {
    host: frontendConfig.frontendHost,
    port: frontendConfig.frontendPort,
    strictPort: true,
    https: frontendConfig.frontendHttps ? {} : undefined,
    fs: { allow: [fileURLToPath(new URL('..', import.meta.url))] },
    proxy: {
      '/gateway': gatewayConfig.gatewayBaseURL,
    },
  },
})
