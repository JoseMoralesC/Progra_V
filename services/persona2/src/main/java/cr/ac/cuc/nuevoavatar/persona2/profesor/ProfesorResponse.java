package cr.ac.cuc.nuevoavatar.persona2.profesor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record ProfesorResponse(
    Integer id,
    String tipoIdentificacion,
    String identificacion,
    String email,
    String nombreCompleto,
    LocalDate fechaNacimiento,
    List<String> telefonos
) {
    public static ProfesorResponse desde(Profesor profesor) {
        List<String> numeros = new ArrayList<>();

        for (ProfesorTelefono telefono : profesor.getTelefonos()) {
            numeros.add(telefono.getTelefono());
        }

        return new ProfesorResponse(
            profesor.getId(),
            profesor.getTipoIdentificacion(),
            profesor.getIdentificacion(),
            profesor.getEmail(),
            profesor.getNombreCompleto(),
            profesor.getFechaNacimiento(),
            numeros
        );
    }
}