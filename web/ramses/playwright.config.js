import fs from 'node:fs';
import { defineConfig } from '@playwright/test';
const hasBundled = fs.existsSync(`${process.env.LOCALAPPDATA || ''}/ms-playwright`);
export default defineConfig({ testDir: './tests/ui', timeout: 15000, webServer: { command: 'npm run dev', url: 'http://127.0.0.1:4174', reuseExistingServer: true, timeout: 30000 }, use: { baseURL: 'http://127.0.0.1:4174', ...(hasBundled ? {} : { channel: 'chrome' }) } });
