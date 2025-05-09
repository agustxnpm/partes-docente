package unpsjb.labprog.backend.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import unpsjb.labprog.backend.model.Cargo;
import unpsjb.labprog.backend.model.Division;
import unpsjb.labprog.backend.model.TipoDesignacion;

@Component
public class CargoValidator {

    @Autowired
    @Lazy
    private DivisionService divisionService;

    @Autowired
    @Lazy
    private CargoService cargoService;

    public void validar(Cargo cargo) {

        if (cargo.getTipoDesignacion() == TipoDesignacion.CARGO && cargo.getDivision() != null) {
            throw new IllegalArgumentException(
                    "Cargo de " + cargo.getNombre() + " es CARGO y no corresponde asignar división");
        }

        if (cargo.getTipoDesignacion() == TipoDesignacion.ESPACIO_CURRICULAR && cargo.getDivision() == null) {
            throw new IllegalArgumentException("Espacio curricular " + cargo.getNombre() + " falta asignar división");
        }

        if (cargo.getDivision() != null) {
            Division divisionExistente = divisionService.buscarDivisionExistente(cargo.getDivision());
            if (divisionExistente != null) {
                cargo.setDivision(divisionExistente);
            } else {
                throw new IllegalArgumentException("La división especificada no existe.");
            }
        }

        if (cargo.getTipoDesignacion() == TipoDesignacion.CARGO) {
            cargoService.findByNombre(cargo.getNombre()).ifPresent(c -> {
                // Solo lanzar excepción si es un cargo diferente (ID diferente)
                if (c.getId() != (cargo.getId())) {
                    throw new DataIntegrityViolationException("El cargo " + cargo.getNombre() + " ya existe.");
                }
            });
        }

        if (cargo.getTipoDesignacion() == TipoDesignacion.ESPACIO_CURRICULAR) {
            cargoService.findByNombreAndDivision(cargo.getNombre(), cargo.getDivision()).ifPresent(c -> {
                // Solo lanzar excepción si es un cargo diferente (ID diferente)
                if (c.getId() != (cargo.getId())) {
                    throw new DataIntegrityViolationException(
                        "El espacio curricular " + cargo.getNombre() + 
                        " en la division " + cargo.getDivision().getAnio() + "º" + 
                        cargo.getDivision().getNumDivision() + "º" + " ya existe."
                    );
                }
            });
        }

    }
}
