import { expect } from '@playwright/test'

export async function cerrarBienvenida(page) {
    const dialogo = page.getByRole('dialog', { name: 'Bienvenido a Nuevo Avatar' })
    const visible = await dialogo.waitFor({ state: 'visible', timeout: 5000 }).then(() => true).catch(() => false)
    if (visible) {
        await dialogo.getByRole('button', { name: /Omitir/ }).click()
        await expect(dialogo).toHaveCount(0)
    }
}

export async function accionar(page, fila, accion) {
    await fila.getByRole('button', { name: /^Acciones para / }).click()
    await page.getByRole('menuitem', { name: accion, exact: true }).click()
}
