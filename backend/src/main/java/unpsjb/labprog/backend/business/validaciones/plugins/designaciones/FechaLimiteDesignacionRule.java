package unpsjb.labprog.backend.business.validaciones.plugins.designaciones;

import java.time.LocalDate;

import unpsjb.labprog.backend.business.interfaces.validaciones.IDesignacionRule;
import unpsjb.labprog.backend.model.Designacion;

/**
 * Ejemplo de plugin extensible para validación de fechas límite.
 * 
 * Este plugin demuestra cómo se puede agregar una nueva regla de validación
 * sin modificar el core de la aplicación:
 * 
 * 1. Crear esta clase en el paquete plugins
 * 2. Implementar IDesignacionRule
 * 3. Agregar el patrón Singleton
 * 4. Agregar "fechaLimite" al archivo validation-rules.properties
 * 5. La regla se carga automáticamente
 * 
 * Para activar esta regla:
 * - Agregar "fechaLimite" a rules.order en validation-rules.properties
 */
public class FechaLimiteDesignacionRule implements IDesignacionRule {

    private static final int MAX_YEARS_FUTURE = 2; // Máximo 2 años en el futuro
    
    // Singleton
    private static FechaLimiteDesignacionRule instance = null;
    
    private FechaLimiteDesignacionRule() {}
    
    public static FechaLimiteDesignacionRule getInstance() {
        if (instance == null) {
            instance = new FechaLimiteDesignacionRule();
        }
        return instance;
    }

    @Override
    public void validate(Designacion designacion) {
        LocalDate fechaLimite = LocalDate.now().plusYears(MAX_YEARS_FUTURE);
        
        if (designacion.getFechaInicio().isAfter(fechaLimite)) {
            throw new IllegalArgumentException(
                String.format("No se pueden crear designaciones con fecha de inicio superior a %d años en el futuro. " +
                             "Fecha límite: %s, Fecha proporcionada: %s", 
                             MAX_YEARS_FUTURE, 
                             fechaLimite.toString(), 
                             designacion.getFechaInicio().toString()));
        }
        
        if (designacion.getFechaFin() != null && designacion.getFechaFin().isAfter(fechaLimite.plusYears(1))) {
            throw new IllegalArgumentException(
                String.format("No se pueden crear designaciones con fecha de fin superior a %d años en el futuro", 
                             MAX_YEARS_FUTURE + 1));
        }
    }

    @Override
    public String getRuleName() {
        return "Validación de Fecha Límite para Designaciones";
    }
}
