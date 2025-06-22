package unpsjb.labprog.backend.business.validaciones.factory;

import java.util.HashMap;
import java.util.Map;

import unpsjb.labprog.backend.business.interfaces.validaciones.IDesignacionRule;
import unpsjb.labprog.backend.business.validaciones.locator.ServiceLocator;
import unpsjb.labprog.backend.business.validaciones.plugins.designaciones.ConflictoDesignacionRule;
import unpsjb.labprog.backend.business.validaciones.plugins.designaciones.TipoDesignacionDesignacionRule;

/**
 * Fábrica para crear reglas de validación de designaciones dinámicamente
 * utilizando reflection y class loading.
 * 
 * Esta implementación permite cargar validadores como plugins sin necesidad
 * de recompilar el core de la aplicación.
 * 
 * Los nombres de las clases se especifican directamente en el archivo de configuración:
 * - Paquete: unpsjb.labprog.backend.business.validaciones.plugins.designaciones
 * - Método: getInstance() (patrón Singleton)
 * 
 * Ejemplo: "BasicDesignacionRule" -> BasicDesignacionRule
 */
public class DesignacionRuleFactory {
    
    // Mapa cache para almacenar las reglas ya cargadas
    private Map<String, IDesignacionRule> ruleCache;
    
    // Singleton
    private static DesignacionRuleFactory instance = null;
    
    private DesignacionRuleFactory() {
        ruleCache = new HashMap<>();
    }
    
    public static DesignacionRuleFactory getInstance() {
        if (instance == null) {
            instance = new DesignacionRuleFactory();
        }
        return instance;
    }
    
    /**
     * Obtiene una regla de validación por su nombre.
     * Si no está en cache, intenta cargarla dinámicamente.
     * 
     * @param ruleName Nombre completo de la clase (ej: "BasicDesignacionRule", "TipoDesignacionDesignacionRule")
     * @return La regla de validación o null si no se pudo cargar
     */
    public IDesignacionRule getRule(String ruleName) {
        // Si ya está en cache, la devolvemos
        if (ruleCache.containsKey(ruleName)) {
            return ruleCache.get(ruleName);
        }
        
        // Intentar cargar la regla dinámicamente
        IDesignacionRule rule = loadRule(ruleName);
        if (rule != null) {
            // Inyectar servicios si es necesario
            injectServices(rule);
            ruleCache.put(ruleName, rule);
        }
        
        return rule;
    }
    
    /**
     * Carga una regla dinámicamente usando reflection.
     * 
     * @param ruleName Nombre completo de la clase
     * @return La regla cargada o null si falló
     */
    private IDesignacionRule loadRule(String ruleName) {
        try {
            // Construir el nombre completo de la clase con el paquete
            String className = buildClassName(ruleName);
            
            // Cargar la clase
            Class<?> ruleClass = Class.forName(className);
            
            // Verificar que implementa la interfaz correcta
            if (!IDesignacionRule.class.isAssignableFrom(ruleClass)) {
                System.err.println("La clase " + className + " no implementa IDesignacionRule");
                return null;
            }
            
            // Obtener la instancia usando el patrón Singleton
            Object ruleInstance = ruleClass.getMethod("getInstance").invoke(null);
            
            return (IDesignacionRule) ruleInstance;
            
        } catch (ClassNotFoundException cnfe) {
            System.err.println("No se encontró la clase para la regla: " + ruleName);
            return null;
        } catch (NoSuchMethodException nsme) {
            System.err.println("La regla " + ruleName + " no implementa el método getInstance()");
            return null;
        } catch (Exception e) {
            System.err.println("Error cargando la regla " + ruleName + ": " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Construye el nombre completo de la clase agregando el paquete.
     * 
     * @param ruleName Nombre completo de la clase (ej: "BasicDesignacionRule")
     * @return Nombre completo de la clase con paquete
     */
    private String buildClassName(String ruleName) {
        return "unpsjb.labprog.backend.business.validaciones.plugins.designaciones." + ruleName;
    }
    
    /**
     * Limpia el cache de reglas.
     * Útil para recargar reglas en tiempo de ejecución.
     */
    public void clearCache() {
        ruleCache.clear();
    }
    
    /**
     * Verifica si una regla está disponible.
     * 
     * @param ruleName Nombre de la regla
     * @return true si la regla se puede cargar, false en caso contrario
     */
    public boolean isRuleAvailable(String ruleName) {
        return getRule(ruleName) != null;
    }
    
    /**
     * Inyecta servicios en los plugins que los necesiten.
     * 
     * @param rule La regla a la que inyectar servicios
     */
    private void injectServices(IDesignacionRule rule) {
        // Inyectar servicios en TipoDesignacionDesignacionRule
        if (rule instanceof TipoDesignacionDesignacionRule) {
            TipoDesignacionDesignacionRule tipoRule = (TipoDesignacionDesignacionRule) rule;
            tipoRule.setCargoService(ServiceLocator.getCargoService());
        }
        
        // Inyectar servicios en ConflictoDesignacionRule
        if (rule instanceof ConflictoDesignacionRule) {
            ConflictoDesignacionRule conflictoRule = (ConflictoDesignacionRule) rule;
            conflictoRule.setDesignacionService(ServiceLocator.getDesignacionService());
            conflictoRule.setLicenciaService(ServiceLocator.getLicenciaService());
        }
        
    }
}
