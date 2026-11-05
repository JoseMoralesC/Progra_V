
import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter } from 'react-router-dom'

import App from './App.jsx'
import { NotificationProvider } from './context/NotificationContext'
import NotificationHost from './components/Notifications/NotificationHost'

import './index.css'
import './styles/portal.css'
import './styles/sesion.css'
import './styles/notas.css'

createRoot(document.getElementById('root')).render(
    <StrictMode>
        <BrowserRouter>
            <NotificationProvider>
                <App />
                <NotificationHost />
            </NotificationProvider>
        </BrowserRouter>
    </StrictMode>
)
