package unpsjb.labprog.backend.business.validaciones.reglas_Designacion;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import unpsjb.labprog.backend.business.interfaces.servicios.ICargoService;
import unpsjb.labprog.backend.business.interfaces.validaciones.IDesignacionRule;
import unpsjb.labprog.backend.model.Cargo;
import unpsjb.labprog.backend.model.Designacion;
import unpsjb.labprog.backend.model.Division;
import unpsjb.labprog.backend.model.TipoDesignacion;

/**
 * Regla de validación para tipos de designación.
 * Verifica que la designación cumpla con las reglas específicas de cada tipo.
 */
@Component
@Order(2)
public class TipoDesignacionValidationRule implements IDesignacionRule {

    @Autowired
    private ICargoService cargoService;

    @Override
    public void validate(Designacion designacion) {
        // Validación para cargo tipo CARGO
        if (designacion.getCargo().getTipoDesignacion() == TipoDesignacion.CARGO
                && designacion.getCargo().getDivision() != null) {
            throw new IllegalArgumentException("Un cargo tipo CARGO no debe tener división asignada");
        }

        // Validación específica para ESPACIO_CURRICULAR
        if (designacion.getCargo().getTipoDesignacion() == TipoDesignacion.ESPACIO_CURRICULAR) {
            validateEspacioCurricular(designacion);
        }
    }

    private void validateEspacioCurricular(Designacion designacion) {
        Division division = designacion.getCargo().getDivision();
        if (division == null) {
            throw new IllegalArgumentException("Un ESPACIO_CURRICULAR requiere una división");
        }

        // Buscar el cargo específico que coincida con nombre y división
        Optional<Cargo> cargoExacto = cargoService.findByNombreAndDivisionExacta(
                designacion.getCargo().getNombre(),
                division);

        if (!cargoExacto.isPresent()) {
            throw new IllegalArgumentException(
                    String.format("No existe el espacio curricular %s para la división %dº %dº turno %s",
                            designacion.getCargo().getNombre(),
                            division.getAnio(),
                            division.getNumDivision(),
                            division.getTurno()));
        }

        // Actualizar el cargo en la designación con el cargo exacto encontrado
        designacion.setCargo(cargoExacto.get());
    }

    @Override
    public String getRuleName() {
        return "Validación de Tipo de Designación";
    }
}
