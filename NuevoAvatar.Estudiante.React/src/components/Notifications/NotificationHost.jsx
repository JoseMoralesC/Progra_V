
import { useEffect } from 'react'
import { useNotifications } from '../../context/NotificationContext'
import Notification from './Notification'

function NotificationHost() {
    const { notifications, dismiss } = useNotifications()

    useEffect(() => {
        const timers = notifications
            .filter(notification => notification.duration > 0)
            .map(notification =>
                window.setTimeout(
                    () => dismiss(notification.id),
                    notification.duration
                )
            )

        return () => timers.forEach(timer => window.clearTimeout(timer))
    }, [notifications, dismiss])

    if (notifications.length === 0) {
        return null
    }

    return (
        <div
            className="notification-host"
            aria-label="Notificaciones del sistema"
        >
            <div className="notification-backdrop" />

            <div className="notification-modal-container">
                {notifications.map(notification => (
                    <Notification
                        key={notification.id}
                        notification={notification}
                        onDismiss={dismiss}
                    />
                ))}
            </div>
        </div>
    )
}

export default NotificationHost
