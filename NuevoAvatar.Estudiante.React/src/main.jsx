
import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter } from 'react-router-dom'

import App from './App.jsx'
import { NotificationProvider } from './context/NotificationContext'
import { AuthProvider } from './context/AuthContext'
import NotificationHost from './components/Notifications/NotificationHost'

import './index.css'
import './styles/portal.css'

createRoot(document.getElementById('root')).render(
    <StrictMode>
        <BrowserRouter>
            <NotificationProvider>
                <AuthProvider>
                    <App />
                    <NotificationHost />
                </AuthProvider>
            </NotificationProvider>
        </BrowserRouter>
    </StrictMode>
)
