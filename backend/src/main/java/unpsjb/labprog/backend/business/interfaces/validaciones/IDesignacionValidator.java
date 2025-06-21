package unpsjb.labprog.backend.business.interfaces.validaciones;

import unpsjb.labprog.backend.model.Designacion;

/**
 * Interfaz para validaciones específicas de Designación.
 * Aplica el principio ISP (Interface Segregation Principle) al ser específica 
 * para una sola responsabilidad.
 */
public interface IDesignacionValidator {
    
    /**
     * Valida una designación
     * @param designacion la designación a validar
     * @throws IllegalArgumentException si la validación falla
     */
    void validarDesignacion(Designacion designacion);
}
