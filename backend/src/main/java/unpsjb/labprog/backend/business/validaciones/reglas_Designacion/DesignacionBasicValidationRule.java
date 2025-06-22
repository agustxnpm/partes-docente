package unpsjb.labprog.backend.business.validaciones.reglas_Designacion;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import unpsjb.labprog.backend.business.interfaces.validaciones.IDesignacionRule;
import unpsjb.labprog.backend.model.Designacion;

/**
 * Regla de validación básica para designaciones.
 * Verifica que los datos fundamentales de la designación estén presentes.
 */
@Component
@Order(1)
public class DesignacionBasicValidationRule implements IDesignacionRule {

    @Override
    public void validate(Designacion designacion) {
        if (designacion == null) {
            throw new IllegalArgumentException("La designación no puede ser nula");
        }
        
        if (designacion.getCargo() == null) {
            throw new IllegalArgumentException("El cargo no puede ser nulo");
        }

        if (designacion.getPersona() == null) {
            throw new IllegalArgumentException("La persona no puede ser nula");
        }

        if (designacion.getFechaInicio() == null) {
            throw new IllegalArgumentException("La fecha de inicio es obligatoria");
        }

        if (designacion.getFechaFin() != null &&
                designacion.getFechaFin().isBefore(designacion.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha fin no puede ser anterior a la fecha inicio");
        }
    }

    @Override
    public String getRuleName() {
        return "Validación Básica de Designación";
    }
}
