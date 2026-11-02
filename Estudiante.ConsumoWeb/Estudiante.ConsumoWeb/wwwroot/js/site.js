(() => {
    const $ = id => document.getElementById(id);

    const stage = $("stage");
    const sidebarToggle = $("sidebarToggle");
    const themeToggle = $("themeToggle");
    const toastElement = $("toast");

    let toastTimer;

    // =================================
    // NOTIFICACIONES
    // =================================

    window.showToast = function (message, error = false) {
        if (!toastElement) return;

        toastElement.textContent = message;

        toastElement.className = error
            ? "toast show error"
            : "toast show";

        clearTimeout(toastTimer);

        toastTimer = setTimeout(() => {
            toastElement.className = "toast";
        }, 3000);
    };

    // =================================
    // SIDEBAR
    // =================================

    if (stage && sidebarToggle) {
        sidebarToggle.addEventListener("click", () => {
            const collapsed = stage.classList.toggle("sidebar-collapsed");

            sidebarToggle.setAttribute(
                "aria-expanded",
                String(!collapsed)
            );

            sidebarToggle.setAttribute(
                "aria-label",
                collapsed ? "Expandir menú" : "Contraer menú"
            );

            sidebarToggle.title = collapsed
                ? "Expandir menú"
                : "Contraer menú";
        });
    }

    // =================================
    // TEMA CLARO / OSCURO
    // =================================

    function applyTheme(theme) {
        const dark = theme === "dark";

        document.body.classList.toggle("dark", dark);

        if (themeToggle) {
            themeToggle.textContent = dark ? "☀" : "☾";

            themeToggle.setAttribute(
                "aria-label",
                dark ? "Cambiar a tema claro" : "Cambiar a tema oscuro"
            );
        }
    }

    let savedTheme = "light";

    try {
        savedTheme = localStorage.getItem("avatar-theme") || "light";
    } catch {
        savedTheme = "light";
    }

    applyTheme(savedTheme);

    if (themeToggle) {
        themeToggle.addEventListener("click", () => {
            const dark = !document.body.classList.contains("dark");

            const nextTheme = dark ? "dark" : "light";

            applyTheme(nextTheme);

            try {
                localStorage.setItem("avatar-theme", nextTheme);
            } catch {
                // La interfaz continúa funcionando aunque el almacenamiento no esté disponible.
            }
        });
    }
})();