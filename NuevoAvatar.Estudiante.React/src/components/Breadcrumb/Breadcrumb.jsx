
function Breadcrumb({ items = [] }) {
    return (
        <nav className="crumb" aria-label="Ruta de navegación">
            {items.map((item, index) => (
                <span key={`${item.label}-${index}`}>
                    {index > 0 && (
                        <span aria-hidden="true"> / </span>
                    )}

                    {index === items.length - 1 ? (
                        <strong aria-current="page">
                            {item.label}
                        </strong>
                    ) : (
                        <span>{item.label}</span>
                    )}
                </span>
            ))}
        </nav>
    )
}

export default Breadcrumb
