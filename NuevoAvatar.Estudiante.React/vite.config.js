import react from '@vitejs/plugin-react'
import { defineConfig } from 'vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
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
    },
  },
})
