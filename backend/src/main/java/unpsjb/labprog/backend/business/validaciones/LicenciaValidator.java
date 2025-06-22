package unpsjb.labprog.backend.business.validaciones;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import unpsjb.labprog.backend.business.interfaces.validaciones.ILicenciaRule;
import unpsjb.labprog.backend.business.interfaces.validaciones.ILicenciaValidator;
import unpsjb.labprog.backend.business.interfaces.servicios.IArticuloLicenciaService;
import unpsjb.labprog.backend.business.validaciones.factory.LicenciaRuleFactory;
import unpsjb.labprog.backend.model.Licencia;

/**
 * Validador para operaciones relacionadas con la entidad Licencia.
 * 
 * Esta clase implementa el Principio Abierto/Cerrado (OCP) de SOLID
 * utilizando el patrón Plugin con Class Loader dinámico.
 * 
 * Las reglas de validación se cargan dinámicamente desde un archivo de configuración
 * y pueden ser agregadas sin necesidad de recompilar el core de la aplicación.
 * 
 * Configuración: validation-rules.properties
 * Los nombres de las clases se especifican directamente en el archivo de configuración.
 */
@Component
public class LicenciaValidator implements ILicenciaValidator {
    
    private static final String CONFIG_FILE = "validation-rules.properties";
    private LicenciaRuleFactory ruleFactory;
    private List<String> configuredRules;
    private List<String> configuredArticleRules;
    
    private IArticuloLicenciaService articuloLicenciaService;

    @Autowired
    public LicenciaValidator(IArticuloLicenciaService articuloLicenciaService) {
        this.articuloLicenciaService = articuloLicenciaService;
        this.ruleFactory = LicenciaRuleFactory.getInstance();
        this.configuredRules = loadConfiguredRules();
        this.configuredArticleRules = loadConfiguredArticleRules();
        registerAllArticles();

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
            String rulesOrder = props.getProperty("licencia.rules.order");
            if (rulesOrder == null || rulesOrder.trim().isEmpty()) {
                throw new IllegalStateException("Configuración 'licencia.rules.order' no encontrada o vacía en " + CONFIG_FILE);
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
     * Carga la lista de reglas de artículos desde el archivo de configuración.
     * 
     * @return Lista de nombres de reglas de artículos configuradas
     */
    private List<String> loadConfiguredArticleRules() {
        List<String> articleRules = new ArrayList<>();
        Properties props = new Properties();
        
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new IllegalStateException("Archivo de configuración " + CONFIG_FILE + " no encontrado. " +
                    "El sistema de validaciones requiere configuración explícita.");
            }
            
            props.load(input);
            
            // Leer las reglas de artículos
            String articleRulesOrder = props.getProperty("licencia.rules.articulos");
            if (articleRulesOrder != null && !articleRulesOrder.trim().isEmpty()) {
                String[] ruleNames = articleRulesOrder.split(",");
                for (String ruleName : ruleNames) {
                    String trimmedName = ruleName.trim();
                    if (!trimmedName.isEmpty()) {
                        articleRules.add(trimmedName);
                    }
                }
            }
            
        } catch (IOException e) {
            throw new IllegalStateException("Error cargando configuración de artículos desde " + CONFIG_FILE + ": " + e.getMessage(), e);
        }
        
        // Las reglas de artículos son opcionales, por lo que pueden estar vacías
        return articleRules;
    }

    /**
     * Valida una licencia aplicando todas las reglas configuradas en orden.
     * Primero aplica las reglas generales, luego las reglas específicas de artículos.
     * 
     * @param licencia La licencia a validar
     * @throws IllegalArgumentException Si alguna regla de validación falla
     */
    @Override
    public void validarLicencia(Licencia licencia) {
        // Aplicar reglas generales
        for (String ruleName : configuredRules) {
            ILicenciaRule rule = ruleFactory.getRule(ruleName);
            
            if (rule == null) {
                System.err.println("Advertencia: No se pudo cargar la regla: " + ruleName);
                continue;
            }
            
            try {
                rule.validate(licencia);
            } catch (IllegalArgumentException e) {
                // Re-lanzar la excepción con información sobre qué regla falló
                throw new IllegalArgumentException(e.getMessage());
            }
        }
        
        // Aplicar reglas específicas de artículos
        for (String articleRuleName : configuredArticleRules) {
            ILicenciaRule articleRule = ruleFactory.getRule(articleRuleName);
            
            if (articleRule == null) {
                System.err.println("Advertencia: No se pudo cargar la regla de artículo: " + articleRuleName);
                continue;
            }
            
            try {
                articleRule.validate(licencia);
            } catch (IllegalArgumentException e) {
                // Re-lanzar la excepción con información sobre qué regla falló
                throw new IllegalArgumentException(e.getMessage());
            }
        }
    }
    
    /**
     * Registra automáticamente todos los artículos definidos en el archivo de configuración.
     * Delega directamente al ArticuloLicenciaService.
     */
    private void registerAllArticles() {
        if (articuloLicenciaService == null) {
            System.err.println("ArticuloLicenciaService no disponible, omitiendo registro automático de artículos");
            return;
        }
        
        try {
            // Registrar todos los artículos desde el archivo de configuración
            articuloLicenciaService.cargarArticulosDesdeConfiguracion();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

}
