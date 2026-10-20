package cr.ac.cuc.nuevoavatar.persona2;
@org.springframework.context.annotation.Import(cr.ac.cuc.nuevoavatar.integracion.IntegracionConfiguration.class)
@org.springframework.boot.autoconfigure.SpringBootApplication
public class NuevoAvatarPersona2Application {
    public static void main(String[] args){cr.ac.cuc.nuevoavatar.integracion.IntegracionBootstrap.run(NuevoAvatarPersona2Application.class,"Usuario",args);}
}
