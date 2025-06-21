package unpsjb.labprog.backend.business.interfaces.validaciones;

import unpsjb.labprog.backend.model.Horario;

/**
 * Interfaz para validaciones específicas de Horario.
 * Aplica el principio ISP (Interface Segregation Principle) al ser específica 
 * para una sola responsabilidad.
 * 
 */
public interface IHorarioValidator {
    
    /**
     * Valida un horario
     * @param horario el horario a validar
     * @throws IllegalArgumentException si la validación falla
     */
    void validarHorario(Horario horario);
    
    /**
     * Valida el borrado de un horario
     * @param horario el horario cuyo borrado se va a validar
     * @throws IllegalArgumentException si la validación falla
     */
    void validarBorradoHorario(Horario horario);
}
