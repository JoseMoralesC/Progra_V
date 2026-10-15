package cr.ac.cuc.nuevoavatar.persona5;
@org.springframework.context.annotation.Import(cr.ac.cuc.nuevoavatar.integracion.IntegracionConfiguration.class)
@org.springframework.boot.autoconfigure.SpringBootApplication
public class NuevoAvatarPersona5Application {
    public static void main(String[] args){cr.ac.cuc.nuevoavatar.integracion.IntegracionBootstrap.run(NuevoAvatarPersona5Application.class,"Notificacion",args);}
}
