import { Modal } from './Modal'
import './welcome-modal.css'

export function WelcomeModal({ nombre, esEstudiante, onCerrar, onContinuar }) {
    return <Modal titulo="Bienvenido a Nuevo Avatar" onCerrar={onCerrar}>
        <div className="welcome-modal-content">
            <span className="welcome-modal-icon" aria-hidden="true">✦</span>
            <h3>¡Hola, {nombre}!</h3>
            <p>{esEstudiante
                ? 'Bienvenido a tu portal estudiantil. Aquí puedes consultar tu expediente, revisar tus prematrículas y administrar tu información personal.'
                : 'Bienvenido al panel administrativo. Desde aquí puedes gestionar los módulos y la información del sistema según tus permisos.'}</p>
            <div className="welcome-modal-actions">
                <button className="btn btn-primary" type="button" onClick={onContinuar}>
                    {esEstudiante ? 'Ir a mi portal' : 'Ir al panel'}
                </button>
                <button className="welcome-modal-skip" type="button" onClick={onCerrar}>
                    Omitir <span aria-hidden="true">→</span>
                </button>
            </div>
        </div>
    </Modal>
}
