# Política inicial de contraseña para ADM24

Esta política se define como regla inicial de ADM24 porque no hay parámetros de
contraseña configurados en `PrograV_Parametro`. No agrega ni asume IDs de
parámetros.

Una contraseña nueva debe cumplir todas estas condiciones:

- Tener al menos 8 caracteres Unicode.
- Incluir al menos una letra mayúscula.
- Incluir al menos una letra minúscula.
- Incluir al menos un número.
- Incluir al menos un carácter especial, entendido como un carácter que no sea
  letra, número ni espacio en blanco; se considera puntuación o símbolo Unicode.
- No superar 72 bytes al codificarse en UTF-8. Este máximo técnico evita que
  BCrypt rechace la contraseña; por ello, los caracteres Unicode de varios bytes
  reducen la cantidad máxima que puede escribirse.

La misma política se valida en React para dar respuesta inmediata y en Usuario
para que no dependa del cliente. Usuario verifica la contraseña actual con
`BCryptPasswordEncoder` y persiste únicamente el nuevo hash BCrypt. La
confirmación de contraseña se valida en React y no se envía al servidor.

La bitácora registra únicamente el evento de cambio de contraseña. No registra
la contraseña actual, la nueva contraseña ni el hash.
