package unpsjb.labprog.backend.business.validaciones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import unpsjb.labprog.backend.business.CargoService;
import unpsjb.labprog.backend.business.DivisionService;
import unpsjb.labprog.backend.model.Division;

@Component
public class DivisionValidator {

    @Autowired
    @Lazy
    private CargoService cargoService;

    @Autowired
    @Lazy
    private DivisionService divisionService;

    public void validarDivision(Division division) {

        if (division.getAnio() <= 0)
            throw new IllegalArgumentException("El año debe ser un número positivo");

        if (division.getNumDivision() <= 0)
            throw new IllegalArgumentException("El número de división debe ser un número positivo");

        if (division.getTurno() == null || division.getTurno().isEmpty())
            throw new IllegalArgumentException("El turno no puede estar vacío");

        // Validar que no exista otra división con los mismos datos
        Division divisionExistente = divisionService
                .buscarDivisionExistente(
                        division.getAnio(),
                        division.getNumDivision());

        if (divisionExistente != null)
            throw new DataIntegrityViolationException("Ya existe la división " + division.getAnio() + "º "
                    + division.getNumDivision() + "º");
    }

    public void validarBorrado(Division division) {
        // Verificar si hay cargos asociados a esta división
        boolean tieneCargos = cargoService.existsByDivisionId(division.getId());

        if (tieneCargos) {
            throw new IllegalArgumentException("No se puede borrar la división " + division.getAnio() + "º" + " "
                    + division.getNumDivision() + "º turno " + division.getTurno() + " porque tiene cargos asignados");
        }
    }
}
