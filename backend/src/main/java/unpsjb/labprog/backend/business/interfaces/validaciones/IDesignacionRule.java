package unpsjb.labprog.backend.business.interfaces.validaciones;

import unpsjb.labprog.backend.model.Designacion;

/**
 * Interfaz que define una regla de validación para designaciones.
 * 
 * Implementa el patrón Strategy para permitir diferentes validaciones
 * de designaciones sin modificar el código existente (Open/Close Principle).
 * 
 * Cada implementación de esta interfaz representa una regla específica
 * de validación que puede ser aplicada a una designación.
 */
public interface IDesignacionRule {
    
    /**
     * Valida una designación según la regla específica implementada.
     * 
     * @param designacion La designación a validar
     * @throws IllegalArgumentException Si la designación no cumple con la regla
     */
    void validate(Designacion designacion);
    
    /**
     * Retorna el nombre descriptivo de la regla de validación.
     * Útil para identificar qué regla falló en caso de error.
     * 
     * @return Nombre descriptivo de la regla
     */
    String getRuleName();
}
