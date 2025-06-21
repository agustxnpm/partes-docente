package unpsjb.labprog.backend.business.interfaces.validaciones;

import unpsjb.labprog.backend.model.Persona;

/**
 * Interfaz para validaciones específicas de Persona.
 * Aplica el principio ISP (Interface Segregation Principle) al ser específica 
 * para una sola responsabilidad.
 * 
 */
public interface IPersonaValidator {
    
    /**
     * Valida una persona
     * @param persona la persona a validar
     * @throws IllegalArgumentException si la validación falla
     */
    void validarPersona(Persona persona);
    
    /**
     * Valida el borrado de una persona
     * @param persona la persona cuyo borrado se va a validar
     * @throws IllegalArgumentException si la validación falla
     */
    void validarBorradoPersona(Persona persona);
    
    /**
     * Valida un DNI
     * @param dni el DNI a validar
     * @throws IllegalArgumentException si la validación falla
     */
    void validarDni(Long dni);
}
