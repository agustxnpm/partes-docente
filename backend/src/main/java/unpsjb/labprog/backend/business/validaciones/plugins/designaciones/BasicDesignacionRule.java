package unpsjb.labprog.backend.business.validaciones.plugins.designaciones;

import unpsjb.labprog.backend.business.interfaces.validaciones.IDesignacionRule;
import unpsjb.labprog.backend.model.Designacion;

/**
 * Plugin de validación básica para designaciones.
 * Verifica que los datos fundamentales de la designación estén presentes.
 * 
 * Este plugin implementa el patrón Singleton y puede ser cargado dinámicamente
 * sin necesidad de recompilar el core de la aplicación.
 */
public class BasicDesignacionRule implements IDesignacionRule {

    // Singleton
    private static BasicDesignacionRule instance = null;
    
    private BasicDesignacionRule() {}
    
    public static BasicDesignacionRule getInstance() {
        if (instance == null) {
            instance = new BasicDesignacionRule();
        }
        return instance;
    }

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
