const configuredEmail = (import.meta.env.VITE_ADMINISTRATIVE_CONTACT_EMAIL || '').trim()
const validEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(configuredEmail)

// El equipo de integración configura este único valor en el .env de React.
export const ADMINISTRATIVE_CONTACT_EMAIL = validEmail ? configuredEmail : null
