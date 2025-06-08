package unpsjb.labprog.backend.business.interfaces;

import unpsjb.labprog.backend.model.Cargo;

/**
 * Interfaz para validaciones específicas de Cargo.
 * Aplica el principio ISP (Interface Segregation Principle) al ser específica 
 * para una sola responsabilidad.
 * 
 */
public interface ICargoValidator {
    
    /**
     * Valida un cargo
     * @param cargo el cargo a validar
     * @throws IllegalArgumentException si la validación falla
     */
    void validarCargo(Cargo cargo);
}
