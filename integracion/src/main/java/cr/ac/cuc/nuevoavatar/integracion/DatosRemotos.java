package cr.ac.cuc.nuevoavatar.integracion;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

/** Consultas HTTP a recursos del propietario; nunca ejecuta SQL remoto. */
public class DatosRemotos {
    private final Map<String, Object> contratos;
    private final String key;
    public DatosRemotos(Map<String,Object> contratos, String key) { this.contratos=contratos; this.key=key; }
    @SuppressWarnings("unchecked")
    public Map<String,Object> contrato(String service) {
        var result=(Map<String,Object>)contratos.get(service);
        if(result==null) throw new IllegalArgumentException("Servicio desconocido");
        return result;
    }
    public RestClient cliente(String service) {
        var config=contrato(service);
        String base=System.getenv(service.toUpperCase(Locale.ROOT)+"_BASE_URL");
        if(base==null || base.isBlank()) base="http://localhost:"+config.get("port");
        var factory=new SimpleClientHttpRequestFactory(); factory.setConnectTimeout(3000); factory.setReadTimeout(8000);
        return RestClient.builder().baseUrl(base).requestFactory(factory).defaultHeader("X-Servicio-Key",key).build();
    }
    public List<Map<String,Object>> listar(String service,String recurso,Map<String,?> filtros) {
        try {
            var result=cliente(service).get().uri(uri->{
                uri.path("/interno/datos/{recurso}");
                filtros.forEach((k,v)->uri.queryParam(java.util.Objects.requireNonNull(k),"{"+k+"}"));
                var variables=new HashMap<String,Object>(); variables.put("recurso",recurso); variables.putAll(filtros);
                return uri.build(variables);
            }).retrieve().body(new ParameterizedTypeReference<List<Map<String,Object>>>(){});
            if(result==null) throw new IllegalStateException("Respuesta vacia");
            return result;
        } catch(RestClientException | IllegalStateException ex) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,"No fue posible consultar "+service,ex);
        }
    }
    public Optional<Map<String,Object>> buscar(String service,String recurso,String campo,Object id) {
        return listar(service,recurso,Map.of(campo,id)).stream().findFirst();
    }
    public void exigir(String service,String recurso,String campo,Object id) {
        if(id==null || buscar(service,recurso,campo,id).isEmpty())
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"La referencia a "+service+" no existe");
    }
    public boolean referencias(String service,String recurso,String campo,Object id) {
        try {
            return Boolean.TRUE.equals(cliente(service).get().uri(u->u.path("/interno/referencias/{r}").queryParam("campo","{c}").queryParam("valor","{v}").build(recurso,campo,id)).retrieve().body(Boolean.class));
        } catch(RestClientException ex){throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,"No fue posible comprobar referencias en "+service,ex);}
    }
    @SuppressWarnings("unchecked")
    public void sinReferencias(String service,String resource,Object id){
        for(var ref:(List<Map<String,Object>>)contrato(service).get("references")){
            if(resource.equals(ref.get("resource")) && referencias(ref.get("service").toString(),ref.get("dependent").toString(),ref.get("field").toString(),id))
                throw new ResponseStatusException(HttpStatus.CONFLICT,"El registro tiene referencias en "+ref.get("service"));
        }
    }
    public static Object valor(Map<String,Object> row,String name) {
        return row.entrySet().stream().filter(e->e.getKey().equalsIgnoreCase(name)).map(entry -> Objects.requireNonNull(entry).getValue()).findFirst().orElse(null);
    }
    public static int entero(Map<String,Object> row,String name) { return ((Number)valor(row,name)).intValue(); }
    public static String texto(Map<String,Object> row,String name) { return Objects.toString(valor(row,name),null); }
    public static LocalDate fecha(Map<String,Object> row,String name) { return LocalDate.parse(texto(row,name).substring(0,10)); }
    public <T> T convertir(Map<String,Object> row,Class<T> type) {
        try {
            T result=type.getDeclaredConstructor().newInstance();
            for(Field field:type.getDeclaredFields()) {
                if(java.lang.reflect.Modifier.isStatic(field.getModifiers())) continue;
                String column=field.getName();
                for(var a:field.getAnnotations()) if(a.annotationType().getName().equals("jakarta.persistence.Column"))
                    column=(String)a.annotationType().getMethod("name").invoke(a);
                Object value=valor(row,column); if(value==null) continue;
                Class<?> t=field.getType();
                if(t==Integer.class || t==int.class) value=((Number)value).intValue();
                else if(t==Long.class || t==long.class) value=((Number)value).longValue();
                else if(t==BigDecimal.class) value=new BigDecimal(value.toString());
                else if(t==LocalDate.class) value=LocalDate.parse(value.toString().substring(0,10));
                else if(t==LocalDateTime.class) value=LocalDateTime.parse(value.toString().replace(' ','T'));
                field.setAccessible(true); field.set(result,value);
            }
            return result;
        } catch(ReflectiveOperationException ex) { throw new IllegalStateException("Contrato incompatible",ex); }
    }
}
