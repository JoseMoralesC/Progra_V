package cr.ac.cuc.nuevoavatar.persona4;
@org.springframework.context.annotation.Import(cr.ac.cuc.nuevoavatar.integracion.IntegracionConfiguration.class)
@org.springframework.boot.autoconfigure.SpringBootApplication
public class NuevoAvatarPersona4Application {
    public static void main(String[] args){cr.ac.cuc.nuevoavatar.integracion.IntegracionBootstrap.run(NuevoAvatarPersona4Application.class,"HistorialAcademico",args);}
}
