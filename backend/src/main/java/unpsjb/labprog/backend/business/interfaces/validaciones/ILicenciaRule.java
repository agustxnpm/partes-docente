package unpsjb.labprog.backend.business.interfaces.validaciones;

import unpsjb.labprog.backend.model.Licencia;

/**
 * Interfaz que define una regla de validación para licencias.
 * 
 * Implementa el patrón Strategy para permitir diferentes validaciones
 * de licencias sin modificar el código existente (Single Responsibility Principle).
 * 
 * Cada implementación de esta interfaz representa una regla específica
 * de validación que puede ser aplicada a una licencia.
 */
public interface ILicenciaRule {
    
    /**
     * Valida una licencia según la regla específica implementada.
     * 
     * @param licencia La licencia a validar
     * @throws IllegalArgumentException Si la licencia no cumple con la regla
     */
    void validate(Licencia licencia);
    
    /**
     * Retorna el nombre descriptivo de la regla de validación.
     * Útil para identificar qué regla falló en caso de error.
     * 
     * @return Nombre descriptivo de la regla
     */
    String getRuleName();
}
