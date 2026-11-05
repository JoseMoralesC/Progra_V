
import AppRoutes from './routes/AppRoutes'
import { SesionProvider } from './sesion/SesionProvider'

function App() {
    return <SesionProvider><AppRoutes /></SesionProvider>
}

export default App
