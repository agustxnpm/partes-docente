package unpsjb.labprog.backend.business.validaciones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import unpsjb.labprog.backend.business.interfaces.servicios.ICargoService;
import unpsjb.labprog.backend.business.interfaces.servicios.IDivisionService;
import unpsjb.labprog.backend.business.interfaces.validaciones.IDivisionValidator;
import unpsjb.labprog.backend.model.Division;

/**
 * Validador para operaciones relacionadas con la entidad División.
 * 
 * Esta clase implementa el Principio de Inversión de Dependencias (DIP) del SOLID,
 * dependiendo de abstracciones (interfaces) en lugar de clases concretas:
 * - ICargoService: Interface para operaciones de cargo
 * - IDivisionService: Interface para operaciones de división
 * 
 * 
 */
@Component
public class DivisionValidator implements IDivisionValidator {

    /**
     * Servicio de cargo inyectado mediante interfaz (DIP).
     * Se usa @Lazy para evitar dependencias circulares.
     */
    @Autowired
    @Lazy
    private ICargoService cargoService;

    /**
     * Servicio de división inyectado mediante interfaz (DIP).
     * Se usa @Lazy para evitar dependencias circulares.
     */
    @Autowired
    @Lazy
    private IDivisionService divisionService;

    public void validarDivision(Division division) {
        validarAnio(division);
        validarNumeroDivision(division);
        validarTurno(division);
        validarUnicidadDivision(division);
    }

    /**
     * Valida que el año sea un número positivo.
     */
    private void validarAnio(Division division) {
        if (division.getAnio() <= 0) {
            throw new IllegalArgumentException("El año debe ser un número positivo");
        }
    }

    /**
     * Valida que el número de división sea un número positivo.
     */
    private void validarNumeroDivision(Division division) {
        if (division.getNumDivision() <= 0) {
            throw new IllegalArgumentException("El número de división debe ser un número positivo");
        }
    }

    /**
     * Valida que el turno no esté vacío o nulo.
     */
    private void validarTurno(Division division) {
        if (division.getTurno() == null || division.getTurno().isEmpty()) {
            throw new IllegalArgumentException("El turno no puede estar vacío");
        }
    }

    /**
     * Valida que no exista otra división con los mismos datos (año y número).
     */
    private void validarUnicidadDivision(Division division) {
        Division divisionExistente = buscarDivisionExistente(division);
        
        if (divisionExistente != null) {
            throw new DataIntegrityViolationException("Ya existe la división " + division.getAnio() + "º " 
               + division.getNumDivision() + "º");
        }
    }

    /**
     * Busca una división existente con el mismo año y número.
     */
    private Division buscarDivisionExistente(Division division) {
        return divisionService.buscarDivisionExistente(
                division.getAnio(),
                division.getNumDivision());
    }

    public void validarBorradoDivision(Division division) {
        validarCargosAsociados(division);
    }

    /**
     * Valida que no existan cargos asociados a la división antes de permitir su eliminación.
     */
    private void validarCargosAsociados(Division division) {
        if (tieneCargosAsociados(division)) {
            throw new IllegalArgumentException("No se puede borrar la división " + division.getAnio() + "º" + " "
               + division.getNumDivision() + "º turno " + division.getTurno() 
               + " porque tiene cargos asignados");
        }
    }

    /**
     * Verifica si la división tiene cargos asociados.
     */
    private boolean tieneCargosAsociados(Division division) {
        return cargoService.existsByDivisionId(division.getId());
    }

}
