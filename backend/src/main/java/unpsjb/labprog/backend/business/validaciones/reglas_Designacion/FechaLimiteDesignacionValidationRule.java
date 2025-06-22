package unpsjb.labprog.backend.business.validaciones.reglas_Designacion;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import unpsjb.labprog.backend.business.interfaces.validaciones.IDesignacionRule;
import unpsjb.labprog.backend.model.Designacion;

// PARA LA DEMO

/**
 * Ejemplo de regla de validación extensible para designaciones.
 * Esta regla valida que las designaciones no se hagan en fechas futuras muy lejanas.
 * 
 * IMPORTANTE: Esta es una regla de ejemplo para demostrar cómo se puede
 * extender el sistema sin modificar el código existente.
 * 
 * Para activar esta regla, simplemente descomenta la anotación @Component.
 * Para desactivarla, comenta la anotación @Component.
 */
// @Component  // Descomenta esta línea para activar la regla
public class FechaLimiteDesignacionValidationRule implements IDesignacionRule {

    private static final int MAX_YEARS_FUTURE = 2; // Máximo 2 años en el futuro

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
