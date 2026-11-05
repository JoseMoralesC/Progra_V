
import {
    createContext,
    useCallback,
    useContext,
    useState
} from 'react'

const NotificationContext = createContext(null)

export function NotificationProvider({ children }) {
    const [notifications, setNotifications] = useState([])

    const notify = useCallback((
        message,
        type = 'info',
        duration = 4000
    ) => {
        const id = crypto.randomUUID()

        setNotifications(current => [
            ...current,
            {
                id,
                message,
                type,
                duration
            }
        ])

        return id
    }, [])

    const dismiss = useCallback((id) => {
        setNotifications(current =>
            current.filter(notification => notification.id !== id)
        )
    }, [])

    return (
        <NotificationContext.Provider
            value={{
                notifications,
                notify,
                dismiss
            }}
        >
            {children}
        </NotificationContext.Provider>
    )
}

export function useNotifications() {
    const context = useContext(NotificationContext)

    if (!context) {
        throw new Error(
            'useNotifications debe utilizarse dentro de NotificationProvider.'
        )
    }

    return context
}
