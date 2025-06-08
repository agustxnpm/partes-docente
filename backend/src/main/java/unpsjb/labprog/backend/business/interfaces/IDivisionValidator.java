package unpsjb.labprog.backend.business.interfaces;

import unpsjb.labprog.backend.model.Division;

/**
 * Interfaz para validaciones específicas de División.
 * Aplica el principio ISP (Interface Segregation Principle) al ser específica 
 * para una sola responsabilidad.
 */
public interface IDivisionValidator {
    
    /**
     * Valida una división
     * @param division la división a validar
     * @throws IllegalArgumentException si la validación falla
     */
    void validarDivision(Division division);
    
    /**
     * Valida el borrado de una división
     * @param division la división cuyo borrado se va a validar
     * @throws IllegalArgumentException si la validación falla
     */
    void validarBorradoDivision(Division division);
}
