package cr.ac.cuc.nuevoavatar.persona4.seguridad;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final AutorizacionInterceptor autorizacion;

    public WebConfig(AutorizacionInterceptor autorizacion) {
        this.autorizacion = autorizacion;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(autorizacion).addPathPatterns(
                "/expediente", "/expediente/**",
                "/prematricula", "/prematricula/**",
                "/historialacademico", "/listadoestudiantes");
    }
}
