import react from '@vitejs/plugin-react'
import { fileURLToPath } from 'node:url'
import { defineConfig } from 'vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  // El formulario compartido de otros módulos vive en web/jose. Se usa una sola copia de React.
  resolve: { dedupe: ['react', 'react-dom'] },
  server: {
    host: '127.0.0.1',
    port: 4175,
    strictPort: true,
    fs: { allow: [fileURLToPath(new URL('..', import.meta.url))] },
    proxy: {
      '/api/auth': {
        target: 'http://localhost:5158',
        changeOrigin: true,
        rewrite: path => path.replace(/^\/api\/auth/, ''),
      },
      '/api/users': {
        target: 'http://localhost:8082',
        changeOrigin: true,
        rewrite: path => path.replace(/^\/api\/users/, ''),
      },
      '/api/roles': {
        target: 'http://localhost:5160',
        changeOrigin: true,
        rewrite: path => path.replace(/^\/api\/roles/, ''),
      },
      '/gateway': 'http://localhost:5080',
    },
  },
})
