package unpsjb.labprog.backend.business.validaciones;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Component;

import unpsjb.labprog.backend.business.interfaces.validaciones.IDesignacionRule;
import unpsjb.labprog.backend.business.interfaces.validaciones.IDesignacionValidator;
import unpsjb.labprog.backend.business.validaciones.factory.DesignacionRuleFactory;
import unpsjb.labprog.backend.model.Designacion;

/**
 * Validador para operaciones relacionadas con la entidad Designación.
 * 
 * Esta clase implementa el Principio Abierto/Cerrado (OCP) de SOLID
 * utilizando el patrón Plugin con Class Loader dinámico.
 * 
 * Las reglas de validación se cargan dinámicamente desde un archivo de configuración
 * y pueden ser agregadas sin necesidad de recompilar el core de la aplicación.
 * 
 * Configuración: validation-rules.properties
 * Convención de nombres: [NombreRegla]DesignacionRule
 */
@Component
public class DesignacionValidator implements IDesignacionValidator {

    private static final String CONFIG_FILE = "validation-rules.properties";
    private DesignacionRuleFactory ruleFactory;
    private List<String> configuredRules;

    public DesignacionValidator() {
        this.ruleFactory = DesignacionRuleFactory.getInstance();
        this.configuredRules = loadConfiguredRules();
    }

    /**
     * Carga la lista de reglas desde el archivo de configuración.
     * 
     * @return Lista de nombres de reglas configuradas
     */
    private List<String> loadConfiguredRules() {
        List<String> rules = new ArrayList<>();
        Properties props = new Properties();
        
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new IllegalStateException("Archivo de configuración " + CONFIG_FILE + " no encontrado. " +
                    "El sistema de validaciones requiere configuración explícita.");
            }
            
            props.load(input);
            
            // Leer las reglas en orden
            String rulesOrder = props.getProperty("designacion.rules.order");
            if (rulesOrder == null || rulesOrder.trim().isEmpty()) {
                throw new IllegalStateException("Configuración 'designacion.rules.order' no encontrada o vacía en " + CONFIG_FILE);
            }
            
            String[] ruleNames = rulesOrder.split(",");
            for (String ruleName : ruleNames) {
                String trimmedName = ruleName.trim();
                if (!trimmedName.isEmpty()) {
                    rules.add(trimmedName);
                }
            }
            
        } catch (IOException e) {
            throw new IllegalStateException("Error cargando configuración desde " + CONFIG_FILE + ": " + e.getMessage(), e);
        }
        
        if (rules.isEmpty()) {
            throw new IllegalStateException("No se encontraron reglas válidas en la configuración " + CONFIG_FILE);
        }
        
        return rules;
    }
    
    /**
     * Valida una designación aplicando todas las reglas configuradas en orden.
     * 
     * @param designacion La designación a validar
     * @throws IllegalArgumentException Si alguna regla de validación falla
     */
    @Override
    public void validarDesignacion(Designacion designacion) {
        for (String ruleName : configuredRules) {
            IDesignacionRule rule = ruleFactory.getRule(ruleName);
            
            if (rule == null) {
                System.err.println("Advertencia: No se pudo cargar la regla: " + ruleName);
                continue;
            }
            
            try {
                rule.validate(designacion);
            } catch (IllegalArgumentException e) {
                // Re-lanzar la excepción con información sobre qué regla falló
                throw new IllegalArgumentException(
                    e.getMessage());
            }
        }
    }
    
}
