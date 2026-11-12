# Política de contraseña parametrizable para ADM24

La política institucional que ADM24 debe aplicar es:

- Mínimo de 8 caracteres.
- Máximo de 30 caracteres.
- Al menos una letra mayúscula.
- Al menos un número.
- No se requiere minúscula ni carácter especial.

Los parámetros propuestos para administrar estas reglas mediante ADM6 son:

| ID propuesto | Valor propuesto | Significado |
|---|---:|---|
| `PASSMIN` | `8` | Longitud mínima |
| `PASSMAX` | `30` | Longitud máxima |
| `PASSMAY` | `true` | Requiere mayúscula |
| `PASSNUM` | `true` | Requiere número |

Los identificadores y valores requieren aprobación del responsable de ADM6 y
deben registrarse allí antes de habilitar el cambio de contraseña. Usuario
obtiene los parámetros mediante `DatosRemotos` y conserva la validación
definitiva en el backend. React consulta la misma política mediante una
operación autenticada del Gateway. Si falta un valor, es inválido o Parámetro
no está disponible, no se aplican reglas alternativas y el cambio se rechaza.

El máximo de 72 bytes UTF-8 es un límite técnico de BCrypt, independiente de la
longitud institucional de 30 caracteres. Usuario mantiene la verificación de
la contraseña actual con BCrypt y guarda únicamente el hash nuevo. La política
no se aplica al login: las contraseñas existentes y sus hashes no se modifican
ni se invalidan por esta funcionalidad.

La confirmación de contraseña se valida en React y no se envía al servidor. La
bitácora registra el evento de cambio sin contraseñas ni hashes.
