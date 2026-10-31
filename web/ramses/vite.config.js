import { defineConfig } from 'vite';
export default defineConfig({ server: { port: 4174, proxy: { '/gateway': 'http://localhost:5000' } } });
