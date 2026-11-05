
const notificationConfig = {
    success: {
        icon: '✓',
        title: '¡Operación exitosa!',
        label: 'Éxito'
    },
    error: {
        icon: '✕',
        title: 'Ocurrió un problema',
        label: 'Error'
    },
    warning: {
        icon: '!',
        title: 'Atención requerida',
        label: 'Advertencia'
    },
    info: {
        icon: 'i',
        title: 'Información',
        label: 'Información'
    }
}

function Notification({ notification, onDismiss }) {
    const config =
        notificationConfig[notification.type] || notificationConfig.info

    return (
        <article
            className={`notification-modal notification-${notification.type}`}
            role={notification.type === 'error' ? 'alertdialog' : 'status'}
            aria-labelledby={`notification-title-${notification.id}`}
            aria-describedby={`notification-message-${notification.id}`}
        >
            <button
                type="button"
                className="notification-close"
                onClick={() => onDismiss(notification.id)}
                aria-label="Cerrar notificación"
            >
                ×
            </button>

            <div className="notification-modal-icon" aria-hidden="true">
                {config.icon}
            </div>

            <div className="notification-modal-content">
                <span className="notification-modal-label">
                    {config.label}
                </span>

                <h2 id={`notification-title-${notification.id}`}>
                    {config.title}
                </h2>

                <p id={`notification-message-${notification.id}`}>
                    {notification.message}
                </p>
            </div>

            <button
                type="button"
                className="notification-accept"
                onClick={() => onDismiss(notification.id)}
            >
                Aceptar
            </button>
        </article>
    )
}

export default Notification
