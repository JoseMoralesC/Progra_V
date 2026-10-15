package cr.ac.cuc.nuevoavatar.integracion;

import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.MapPropertySource;

/** La ejecucion desde IDE y desde lanzador usa la misma base y cuenta propia. */
public final class IntegracionBootstrap {
    private IntegracionBootstrap(){}
    public static void run(Class<?> application,String service,String[] args){
        var config=new HashMap<String,String>();
        for(Path p=Path.of("").toAbsolutePath();p!=null;p=p.getParent()){
            Path file=p.resolve(".env");if(!Files.isRegularFile(file))continue;
            try {
                for(String line:Files.readAllLines(file,StandardCharsets.UTF_8)){
                    String text=line.strip();if(text.startsWith("export "))text=text.substring(7).strip();
                    if(text.isBlank() || text.startsWith("#") || !text.contains("="))continue;
                    int at=text.indexOf('=');String key=text.substring(0,at).strip(),value=text.substring(at+1).strip();
                    if(value.length()>1 && value.charAt(0)==value.charAt(value.length()-1) && (value.startsWith("\"") || value.startsWith("'")))value=value.substring(1,value.length()-1);
                    config.put(key,value);
                }
            }catch(java.io.IOException ex){throw new IllegalStateException("No se pudo leer la configuracion local",ex);}
            break;
        }
        config.putAll(System.getenv());
        String database=config.getOrDefault("SERVICE_DB_NAME","PrograV_"+service);
        if(database.equalsIgnoreCase(config.getOrDefault("DB_NAME","PrograV")))throw new IllegalStateException("Un servicio no puede conectarse a la base de origen");
        String user=config.getOrDefault("SERVICE_DB_USER_"+service.toUpperCase(Locale.ROOT),"");
        String password=config.getOrDefault("SERVICE_DB_PASSWORD_"+service.toUpperCase(Locale.ROOT),"");
        if(user.isBlank() || password.isBlank())throw new IllegalStateException("Faltan credenciales SQL de "+service);
        var own=new HashMap<String,Object>();
        own.put("spring.datasource.url","jdbc:sqlserver://"+config.getOrDefault("DB_HOST","localhost")+":"+config.getOrDefault("DB_PORT","1433")+";databaseName="+database+";encrypt=true;trustServerCertificate=true");
        own.put("spring.datasource.username",user);own.put("spring.datasource.password",password);
        own.put("integration.service",service);own.put("SERVICE_DB_NAME",database);
        own.put("SERVICES_INTERNAL_KEY",config.getOrDefault("SERVICES_INTERNAL_KEY",""));
        var app=new SpringApplication(application);
        app.setDefaultProperties(new HashMap<String,Object>(config));
        app.addInitializers(context->context.getEnvironment().getPropertySources().addFirst(new MapPropertySource("base-propia",own)));
        app.run(args);
    }
}
