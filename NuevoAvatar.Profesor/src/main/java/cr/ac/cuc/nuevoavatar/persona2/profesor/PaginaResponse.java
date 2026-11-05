package cr.ac.cuc.nuevoavatar.persona2.profesor;

import java.util.List;
import org.springframework.data.domain.Page;

public record PaginaResponse<T>(List<T> items, int page, int size, long total) {
    public static <T> PaginaResponse<T> desde(Page<T> pagina) {
        return new PaginaResponse<>(pagina.getContent(), pagina.getNumber(), pagina.getSize(), pagina.getTotalElements());
    }
}
