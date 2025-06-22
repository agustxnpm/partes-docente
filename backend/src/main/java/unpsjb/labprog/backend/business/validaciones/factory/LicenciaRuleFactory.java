package unpsjb.labprog.backend.business.validaciones.factory;

import java.util.HashMap;
import java.util.Map;

import unpsjb.labprog.backend.business.interfaces.validaciones.ILicenciaRule;
import unpsjb.labprog.backend.business.validaciones.locator.ServiceLocator;
import unpsjb.labprog.backend.business.validaciones.plugins.licencias.Articulo23ALicenciaRule;
import unpsjb.labprog.backend.business.validaciones.plugins.licencias.Articulo36ALicenciaRule;
import unpsjb.labprog.backend.business.validaciones.plugins.licencias.Articulo5ALicenciaRule;
import unpsjb.labprog.backend.business.validaciones.plugins.licencias.PersonaLicenciaRule;
import unpsjb.labprog.backend.business.validaciones.plugins.licencias.SuperposicionLicenciaRule;

/**
 * Fábrica para crear reglas de validación de licencias dinámicamente
 * utilizando reflection y class loading basado en naming conventions.
 * 
 * Esta implementación permite cargar validadores como plugins sin necesidad
 * de recompilar el core de la aplicación.
 * 
 * Convención de nombres:
 * - Clase: [NombreRegla]LicenciaRule
 * - Paquete: unpsjb.labprog.backend.business.validaciones.plugins.licencias
 * - Método: getInstance() (patrón Singleton)
 * 
 * Ejemplo: "basic" -> BasicLicenciaRule
 */
public class LicenciaRuleFactory {

    // Mapa cache para almacenar las reglas ya cargadas
    private Map<String, ILicenciaRule> ruleCache;

    // Singleton
    private static LicenciaRuleFactory instance = null;

    private LicenciaRuleFactory() {
        ruleCache = new HashMap<>();
    }

    public static LicenciaRuleFactory getInstance() {
        if (instance == null) {
            instance = new LicenciaRuleFactory();
        }
        return instance;
    }

    /**
     * Obtiene una regla de validación por su nombre.
     * Si no está en cache, intenta cargarla dinámicamente.
     * 
     * @param ruleName Nombre de la regla (ej: "basic", "persona", "superposicion",
     *                 "articulo23A")
     * @return La regla de validación o null si no se pudo cargar
     */
    public ILicenciaRule getRule(String ruleName) {
        // Si ya está en cache, la devolvemos
        if (ruleCache.containsKey(ruleName)) {
            return ruleCache.get(ruleName);
        }

        // Intentar cargar la regla dinámicamente
        ILicenciaRule rule = loadRule(ruleName);
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
     * @param ruleName Nombre de la regla
     * @return La regla cargada o null si falló
     */
    private ILicenciaRule loadRule(String ruleName) {
        try {
            // Construir el nombre de la clase siguiendo la convención
            String className = buildClassName(ruleName);

            // Cargar la clase
            Class<?> ruleClass = Class.forName(className);

            // Verificar que implementa la interfaz correcta
            if (!ILicenciaRule.class.isAssignableFrom(ruleClass)) {
                System.err.println("La clase " + className + " no implementa ILicenciaRule");
                return null;
            }

            // Obtener la instancia usando el patrón Singleton
            Object ruleInstance = ruleClass.getMethod("getInstance").invoke(null);

            return (ILicenciaRule) ruleInstance;

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
     * Construye el nombre completo de la clase siguiendo la convención.
     * 
     * Convención:
     * unpsjb.labprog.backend.business.validaciones.plugins.licencias.[NombreRegla]LicenciaRule
     * 
     * Ejemplos:
     * - "basic" -> "BasicLicenciaRule"
     * - "persona" -> "PersonaLicenciaRule"
     * - "superposicion" -> "SuperposicionLicenciaRule"
     * - "articulo23A" -> "Articulo23ALicenciaRule"
     * 
     * @param ruleName Nombre base de la regla
     * @return Nombre completo de la clase
     */
    private String buildClassName(String ruleName) {
        // Convertir primera letra a mayúscula
        String capitalizedName = ruleName.substring(0, 1).toUpperCase() +
                ruleName.substring(1);

        return "unpsjb.labprog.backend.business.validaciones.plugins.licencias." +
                capitalizedName + "LicenciaRule";
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
    private void injectServices(ILicenciaRule rule) {
        // Inyectar servicios en PersonaLicenciaRule
        if (rule instanceof PersonaLicenciaRule) {
            PersonaLicenciaRule personaRule = 
                (PersonaLicenciaRule) rule;
            personaRule.setPersonaService(ServiceLocator.getPersonaService());
        }
        
        // Inyectar servicios en SuperposicionLicenciaRule
        if (rule instanceof SuperposicionLicenciaRule) {
            SuperposicionLicenciaRule superposicionRule = 
                (SuperposicionLicenciaRule) rule;
            superposicionRule.setLicenciaService(ServiceLocator.getLicenciaService());
        }

        // Inyectar servicios en Articulo5ALicenciaRule
        if (rule instanceof Articulo5ALicenciaRule) {
            Articulo5ALicenciaRule art5aRule = 
                (Articulo5ALicenciaRule) rule;
            art5aRule.setLicenciaService(ServiceLocator.getLicenciaService());
        }

        // Inyectar servicios en Articulo36ALicenciaRule
        if (rule instanceof Articulo36ALicenciaRule) {
            Articulo36ALicenciaRule art36aRule = 
                (Articulo36ALicenciaRule) rule;
            art36aRule.setLicenciaService(ServiceLocator.getLicenciaService());
        }
        
        // Inyectar servicios en Articulo23ALicenciaRule
        if (rule instanceof Articulo23ALicenciaRule) {
            Articulo23ALicenciaRule art23aRule = 
                (Articulo23ALicenciaRule) rule;
            art23aRule.setLicenciaService(ServiceLocator.getLicenciaService());
        }
    }
}