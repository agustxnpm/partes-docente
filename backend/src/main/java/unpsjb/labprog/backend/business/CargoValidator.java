package unpsjb.labprog.backend.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import unpsjb.labprog.backend.model.Cargo;
import unpsjb.labprog.backend.model.Division;
import unpsjb.labprog.backend.model.TipoDesignacion;

@Component
public class CargoValidator {

    @Autowired
    @Lazy
    private DivisionService divisionService;

    public void validar(Cargo cargo) {

        if (cargo.getTipoDesignacion() == TipoDesignacion.CARGO && cargo.getDivision() != null) {
            throw new IllegalArgumentException(
                    "Cargo de " + cargo.getNombre() + " es CARGO y no corresponde asignar división");
        }

        if (cargo.getTipoDesignacion() == TipoDesignacion.ESPACIO_CURRICULAR && cargo.getDivision() == null) {
            throw new IllegalArgumentException("Espacio Curricular " + cargo.getNombre() + " falta asignar división");
        }

        if (cargo.getDivision() != null) {
            Division divisionExistente = divisionService.buscarDivisionExistente(cargo.getDivision());
            if (divisionExistente != null) {
                cargo.setDivision(divisionExistente);
            } else {
                throw new IllegalArgumentException("La división especificada no existe.");
            }
        }

    }
}
