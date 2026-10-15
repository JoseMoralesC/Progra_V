package cr.ac.cuc.nuevoavatar.integracion;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import javax.sql.DataSource;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class IntegracionConfiguration implements WebMvcConfigurer {
    private final String key;
    private final String service;
    private final Map<String,Object> contracts;
    @SuppressWarnings("unchecked")
    public IntegracionConfiguration(@Value("${integration.service}") String service,
          @Value("${SERVICES_INTERNAL_KEY:}") String key) throws Exception {
        if(key.length()<32) throw new IllegalStateException("Configurar SERVICES_INTERNAL_KEY (minimo 32 caracteres)");
        this.key=key; this.service=service;
        try(var input=new ClassPathResource("contratos.json").getInputStream()) {
            this.contracts=new com.fasterxml.jackson.databind.ObjectMapper().readValue(input,Map.class);
        }
    }
    @Bean public DatosRemotos datosRemotos() { return new DatosRemotos(contracts,key); }
    @Bean public org.springframework.beans.factory.InitializingBean verificarBase(JdbcTemplate jdbc,
        @Value("${SERVICE_DB_NAME:}") String configuredDatabase) {
        return ()->{
            String expected=configuredDatabase.isBlank()?"PrograV_"+service:configuredDatabase;
            String actual=jdbc.queryForObject("SELECT DB_NAME()",String.class);
            if(!expected.equals(actual))throw new IllegalStateException("La conexion no apunta a la base propia de "+service);
        };
    }
    @Override public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor(){
            @Override public boolean preHandle(HttpServletRequest req,HttpServletResponse res,Object handler) throws Exception {
                String supplied=Objects.toString(req.getHeader("X-Servicio-Key"),"");
                if(!MessageDigest.isEqual(key.getBytes(StandardCharsets.UTF_8),supplied.getBytes(StandardCharsets.UTF_8))) {
                    res.setStatus(401); res.setContentType("application/json");res.getWriter().write("{\"error\":\"No autorizado\"}");return false;
                }
                return true;
            }
        }).addPathPatterns("/interno/**").order(-100);
        registry.addInterceptor(new HandlerInterceptor(){
            @Override public boolean preHandle(HttpServletRequest req,HttpServletResponse res,Object handler){
                if("DELETE".equals(req.getMethod()) && !"Matricula".equals(service)) {
                    String[] parts=req.getRequestURI().split("/");
                    if(parts.length==3 && !parts[1].equals("interno")) {
                        @SuppressWarnings("unchecked") var resources=(Map<String,Object>)datosRemotos().contrato(service).get("resources");
                        String resource=resources.keySet().stream().filter(r->r.equalsIgnoreCase(parts[1])).findFirst().orElse(null);
                        if(resource==null && "Expediente".equals(service))resource="Estudiante";
                        if(resource!=null)datosRemotos().sinReferencias(service,resource,java.net.URLDecoder.decode(parts[2],StandardCharsets.UTF_8));
                    }
                }
                return true;
            }
        }).addPathPatterns("/**").order(100);
    }
    @RestController
    public static class RecursosInternos {
        private final JdbcTemplate jdbc; private final DatosRemotos remote; private final String service;
        RecursosInternos(JdbcTemplate jdbc,DatosRemotos remote,@Value("${integration.service}") String service){this.jdbc=jdbc;this.remote=remote;this.service=service;}
        @GetMapping("/interno/estado")
        public Map<String,String> estado(){return Map.of("servicio",service,"base",jdbc.queryForObject("SELECT DB_NAME()",String.class));}
        @SuppressWarnings("unchecked")
        @GetMapping("/interno/referencias/{resource}")
        public boolean referencias(@PathVariable String resource,@RequestParam String campo,@RequestParam String valor){
            var resources=(Map<String,Object>)remote.contrato(service).get("resources");
            var table=(Map<String,Object>)resources.get(resource);
            if(table==null)throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            var columns=(List<String>)table.get("columns");
            String column=columns.stream().filter(c->c.equalsIgnoreCase(campo)).findFirst().orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST));
            return Boolean.TRUE.equals(jdbc.queryForObject("SELECT CASE WHEN EXISTS(SELECT 1 FROM "+table.get("table")+" WHERE ["+column+"]=?) THEN 1 ELSE 0 END",Boolean.class,valor));
        }
        @SuppressWarnings("unchecked")
        @GetMapping("/interno/datos/{resource}")
        public List<Map<String,Object>> listar(@PathVariable String resource,@RequestParam Map<String,String> filters) {
            var resources=(Map<String,Object>)remote.contrato(service).get("resources");
            var table=(Map<String,Object>)resources.get(resource);
            if(table==null || Boolean.FALSE.equals(table.get("readable"))) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            var columns=(List<String>)table.get("columns");
            String sql="SELECT "+columns.stream().map(c->"["+c+"]").collect(java.util.stream.Collectors.joining(","))+" FROM "+table.get("table");
            var args=new ArrayList<Object>(); var conditions=new ArrayList<String>();
            for(var filter:filters.entrySet()) {
                String column=columns.stream().filter(c->c.equalsIgnoreCase(filter.getKey())).findFirst().orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"Filtro desconocido"));
                conditions.add("["+column+"]=?"); args.add(filter.getValue());
            }
            if(!conditions.isEmpty()) sql+=" WHERE "+String.join(" AND ",conditions);
            sql+=" ORDER BY ["+table.get("key")+"]";
            return jdbc.query(sql,(rs,n)->{
                var row=new LinkedHashMap<String,Object>();
                for(String col:columns){Object value=rs.getObject(col);
                    if(value instanceof java.sql.Date d)value=d.toLocalDate().toString();
                    if(value instanceof java.sql.Timestamp t)value=t.toLocalDateTime().toString();
                    row.put(col,value);}
                return row;
            },args.toArray());
        }
    }
}
