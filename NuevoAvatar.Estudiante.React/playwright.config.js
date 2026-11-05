import { defineConfig } from '@playwright/test'

export default defineConfig({
    testDir: './tests/ui',
    fullyParallel: false,
    workers: 1,
    use: { baseURL: 'http://127.0.0.1:4175', headless: true, screenshot: 'only-on-failure' },
    webServer: {
        command: 'npm run dev',
        url: 'http://127.0.0.1:4175',
        reuseExistingServer: false,
    },
})
