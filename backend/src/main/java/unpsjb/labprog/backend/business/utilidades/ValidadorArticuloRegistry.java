package unpsjb.labprog.backend.business.utilidades;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import unpsjb.labprog.backend.business.validaciones.ArticuloLicenciaValidator;

/**
 * Registro de validadores de artículos.
 * Utiliza el patrón Registry para almacenar y recuperar validadores
 * basados en la anotación @ValidadorArticulo.
 */
@Component
public class ValidadorArticuloRegistry {
    private final Map<String, ArticuloLicenciaValidator> validadores = new HashMap<>();
    
    @Autowired
    private ApplicationContext context;
    
    @PostConstruct
    public void init() {
        Map<String, Object> beans = context.getBeansWithAnnotation(ValidadorArticulo.class);
        for (Object bean : beans.values()) {
            if (bean instanceof ArticuloLicenciaValidator) {
                ValidadorArticulo annotation = bean.getClass().getAnnotation(ValidadorArticulo.class);
                validadores.put(annotation.codigoArticulo(), (ArticuloLicenciaValidator) bean);
            }
        }
    }
    
    public ArticuloLicenciaValidator getValidador(String codigoArticulo) {
        ArticuloLicenciaValidator validator = validadores.get(codigoArticulo);
        if (validator == null) {
            // Podrías lanzar una excepción o manejar este caso de otra forma
            throw new IllegalArgumentException("No hay validador registrado para el artículo " + codigoArticulo);
        }
        return validator;
    }
    
    public boolean existeValidador(String codigoArticulo) {
        return validadores.containsKey(codigoArticulo);
    }
}