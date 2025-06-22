package unpsjb.labprog.backend.business.validaciones;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import unpsjb.labprog.backend.business.interfaces.validaciones.IDesignacionRule;
import unpsjb.labprog.backend.business.interfaces.validaciones.IDesignacionValidator;
import unpsjb.labprog.backend.model.Designacion;

/**
 * Validador para operaciones relacionadas con la entidad Designación.
 * 
 * Esta clase implementa el Principio Abierto/Cerrado (OCP) de SOLID,
 * utilizando el patrón Strategy a través de una lista de reglas de validación.
 * 
 * Para agregar nuevas validaciones, simplemente se debe:
 * 1. Crear una nueva clase que implemente IDesignacionRule
 * 2. Marcarla como @Component para que Spring la inyecte automáticamente
 * 
 */
@Component
public class DesignacionValidator implements IDesignacionValidator {

    /**
     * Lista de reglas de validación inyectadas automáticamente por Spring.
     * Spring encontrará todas las implementaciones de IDesignacionRule
     * marcadas como @Component y las inyectará en esta lista.
     * 
     *  El orden de ejecución está determinado por @Order en cada regla:
     * - @Order(1): DesignacionBasicValidationRule (validaciones básicas)
     * - @Order(2): TipoDesignacionValidationRule (validaciones de tipo)
     * - @Order(3): ConflictoDesignacionValidationRule (validaciones de conflictos)
     */
    @Autowired
    private List<IDesignacionRule> validationRules;

    /**
     * Valida una designación aplicando todas las reglas de validación registradas.
     * Las reglas se ejecutan en el orden definido por @Order.
     * 
     * @param designacion La designación a validar
     * @throws IllegalArgumentException Si alguna regla de validación falla
     */
    @Override
    public void validarDesignacion(Designacion designacion) {
        for (IDesignacionRule rule : validationRules) {
            try {
                rule.validate(designacion);
            } catch (IllegalArgumentException e) {
                // Re-lanzar la excepción con información adicional sobre qué regla falló
                throw new IllegalArgumentException(e.getMessage());
            }
        }
    }
}
