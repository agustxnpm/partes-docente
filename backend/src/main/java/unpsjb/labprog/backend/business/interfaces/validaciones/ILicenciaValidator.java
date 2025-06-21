package unpsjb.labprog.backend.business.interfaces.validaciones;

import unpsjb.labprog.backend.model.Licencia;

/**
 * Interfaz para validaciones específicas de Licencia.
 * Aplica el principio ISP (Interface Segregation Principle) al ser específica 
 * para una sola responsabilidad.
 * 
 */
public interface ILicenciaValidator {
    
    /**
     * Valida una licencia
     * @param licencia la licencia a validar
     * @throws IllegalArgumentException si la validación falla
     */
    void validarLicencia(Licencia licencia);
}
