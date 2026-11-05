
function Footer({
    systemName = 'Nuevo Avatar V2',
    text = 'Sistema administrativo'
}) {
    return (
        <footer className="portal-footer">
            <span>{systemName}</span>
            <span> · {text}</span>
        </footer>
    )
}

export default Footer
