
let users = [
    { email: "estudiante@cuc.cr", type: "Cédula", id: "123456789", name: "Carlos Méndez", role: "Estudiante" },
    { email: "profesor@cuc.ac.cr", type: "Cédula", id: "987654321", name: "Laura Vargas", role: "Profesor" },
    { email: "admin@cuc.ac.cr", type: "Pasaporte", id: "P1234567", name: "Ana López", role: "Administrador" }
];

let editing = null;
let toastTimer;

const $ = id => document.getElementById(id);

function toast(message, error = false) {
    const element = $("toast");
    element.textContent = message;
    element.className = "toast show" + (error ? " error" : "");

    clearTimeout(toastTimer);
    toastTimer = setTimeout(() => element.className = "toast", 2800);
}

function escapeHtml(value) {
    return String(value).replace(/[&<>"']/g, character => ({
        "&": "&amp;",
        "<": "&lt;",
        ">": "&gt;",
        '"': "&quot;",
        "'": "&#39;"
    }[character]));
}

function filteredUsers() {
    const query = $("search").value.trim().toLowerCase();
    const type = $("typeFilter").value;
    const domain = $("domainFilter").value;
    const role = $("roleFilter").value;

    return users.filter(user =>
        (!query || [user.email, user.id, user.name].some(value =>
            value.toLowerCase().includes(query))) &&
        (!type || user.type === type) &&
        (!domain || user.email.endsWith("@" + domain)) &&
        (!role || user.role === role)
    );
}

function renderUsers() {
    const list = filteredUsers();
    $("rows").innerHTML = "";

    if (!list.length) {
        $("rows").innerHTML =
            '<tr><td colspan="6" class="empty">No hay usuarios que coincidan con los filtros.</td></tr>';
    }

    list.forEach(user => {
        const row = document.createElement("tr");

        row.innerHTML = `
            <td>${escapeHtml(user.email)}</td>
            <td>${escapeHtml(user.type)}</td>
            <td>${escapeHtml(user.id)}</td>
            <td>${escapeHtml(user.name)}</td>
            <td><span class="role-pill">${escapeHtml(user.role)}</span></td>
            <td>
                <div class="actions">
                    <button class="icon-btn" title="Editar" aria-label="Editar usuario">✎</button>
                    <button class="icon-btn delete" title="Eliminar" aria-label="Eliminar usuario">⌫</button>
                </div>
            </td>`;

        row.querySelectorAll("button")[0].onclick = () => editUser(user.email);
        row.querySelectorAll("button")[1].onclick = () => deleteUser(user.email);

        $("rows").appendChild(row);
    });

    $("count").textContent =
        `Mostrando ${list.length ? 1 : 0} a ${list.length} de ${list.length} registros`;
}

function showModal() {
    $("backdrop").classList.add("open");
    setTimeout(() => $("emailUser").focus(), 40);
}

function closeModal() {
    $("backdrop").classList.remove("open");
    $("form").reset();

    $("emailUser").readOnly = false;
    $("emailDomain").disabled = false;
    editing = null;

    $("modalTitle").textContent = "Nuevo usuario";
    $("password").required = true;
    $("passStar").style.display = "inline";
    $("passHelp").textContent = "Obligatoria al crear un usuario.";

    updateRoles();
}

function createUser() {
    closeModal();
    showModal();
}

function editUser(email) {
    const user = users.find(item => item.email === email);
    if (!user) return;

    editing = email;

    const parts = user.email.split("@");

    $("modalTitle").textContent = "Editar usuario";
    $("emailUser").value = parts[0];
    $("emailDomain").value = parts[1];

    $("emailUser").readOnly = true;
    $("emailDomain").disabled = true;

    $("idType").value = user.type;
    $("identification").value = user.id;
    $("name").value = user.name;

    updateRoles();
    $("role").value = user.role;

    $("password").value = "";
    $("password").required = false;
    $("passStar").style.display = "none";
    $("passHelp").textContent = "Déjala vacía para conservar la contraseña.";

    showModal();
}

function deleteUser(email) {
    if (!confirm(`¿Confirmas que deseas eliminar a ${email}?`)) return;

    users = users.filter(user => user.email !== email);
    renderUsers();
    toast("Usuario eliminado en el prototipo.");
}

if ($("rows")) {
$("newUser").addEventListener("click", createUser);
$("close").addEventListener("click", closeModal);
$("cancel").addEventListener("click", closeModal);

$("backdrop").addEventListener("click", event => {
    if (event.target === $("backdrop")) closeModal();
});

document.addEventListener("keydown", event => {
    if (event.key === "Escape") closeModal();
});

$("form").addEventListener("submit", event => {
    event.preventDefault();

    const local = $("emailUser").value.trim().toLowerCase();
    const domain = $("emailDomain").value;
    const email = local + "@" + domain;
    const type = $("idType").value;
    const id = $("identification").value.trim();
    const name = $("name").value.trim();
    const role = $("role").value;
    const password = $("password").value;

    if (!/^[a-zA-Z0-9._-]+$/.test(local)) {
        toast("Escribe una parte local válida para el correo.", true);
        return;
    }

    if (!name) {
        toast("El nombre no puede estar vacío.", true);
        return;
    }

    const allowedRoles = domain === "cuc.cr"
        ? ["Estudiante"]
        : ["Profesor", "Administrador"];

    if (!allowedRoles.includes(role)) {
        toast("El rol no coincide con el dominio seleccionado.", true);
        return;
    }

    if (!editing && users.some(user => user.email.toLowerCase() === email)) {
        toast("Ese correo ya existe.", true);
        return;
    }

    if (!editing && !password.trim()) {
        toast("La contraseña es obligatoria al crear.", true);
        return;
    }

    const data = { email, type, id, name, role };

    if (editing) {
        const index = users.findIndex(user => user.email === editing);
        users[index] = { ...users[index], ...data };
        toast("Usuario actualizado en el prototipo.");
    } else {
        users.push(data);
        toast("Usuario creado en el prototipo.");
    }

    closeModal();
    renderUsers();
});

function updateRoles() {
    const domain = $("emailDomain").value;
    const role = $("role");
    const previous = role.value;

    role.innerHTML = '<option value="">Seleccione...</option>' +
        (domain === "cuc.cr"
            ? "<option>Estudiante</option>"
            : "<option>Profesor</option><option>Administrador</option>");

    if ([...role.options].some(option => option.value === previous)) {
        role.value = previous;
    }
}

$("emailDomain").addEventListener("change", updateRoles);

$("filter").addEventListener("click", renderUsers);

$("clear").addEventListener("click", () => {
    $("search").value = "";
    $("typeFilter").value = "";
    $("domainFilter").value = "";
    $("roleFilter").value = "";

    renderUsers();
    toast("Filtros limpiados.");
});

$("search").addEventListener("keydown", event => {
    if (event.key === "Enter") renderUsers();
});

["typeFilter", "domainFilter", "roleFilter"].forEach(id => {
    $(id).addEventListener("change", renderUsers);
});

$("prev").addEventListener("click", () => toast("Ya estás en la primera página."));
$("next").addEventListener("click", () => toast("Todos los registros caben en esta página."));
updateRoles();
renderUsers();
}

$("sidebarToggle").addEventListener("click", () => {
    const stage = document.querySelector(".stage");
    const collapsed = stage.classList.toggle("sidebar-collapsed");
    const button = $("sidebarToggle");

    button.title = collapsed ? "Expandir menú" : "Contraer menú";
    button.setAttribute("aria-label", button.title);
    button.setAttribute("aria-expanded", String(!collapsed));
});

$("catalogToggle").addEventListener("click", () => {
    const subtree = $("catalogSub");
    const open = subtree.style.display !== "none";

    subtree.style.display = open ? "none" : "grid";
    $("catalogSign").textContent = open ? "＋" : "−";
    $("catalogToggle").setAttribute("aria-expanded", String(!open));
});

$("securityToggle").addEventListener("click", () => {
    const subtree = $("securitySub");
    const open = subtree.style.display !== "none";

    subtree.style.display = open ? "none" : "grid";
    $("securitySign").textContent = open ? "＋" : "−";
    $("securityToggle").setAttribute("aria-expanded", String(!open));
});

function setTheme() {
    document.body.classList.toggle("dark");

    const dark = document.body.classList.contains("dark");
    $("themeToggle").textContent = dark ? "☀" : "☾";

    localStorage.setItem("avatar-theme", dark ? "dark" : "light");
}

const savedTheme = localStorage.getItem("avatar-theme");

if (savedTheme === "dark") {
    document.body.classList.add("dark");
    $("themeToggle").textContent = "☀";
}

$("themeToggle").addEventListener("click", setTheme);
$("notify").addEventListener("click", () => toast("No tienes notificaciones nuevas."));
