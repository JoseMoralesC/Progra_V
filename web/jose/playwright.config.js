import { defineConfig } from '@playwright/test';
export default defineConfig({
  testDir: './tests/ui', fullyParallel: false, workers: 1,
  globalSetup: './tests/servidor-vite.mjs',
  use: { baseURL: 'http://127.0.0.1:5173', headless: true, screenshot: 'only-on-failure' },
});
