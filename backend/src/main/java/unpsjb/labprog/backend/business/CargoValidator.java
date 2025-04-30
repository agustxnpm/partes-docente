package unpsjb.labprog.backend.business;

import org.springframework.stereotype.Component;

import unpsjb.labprog.backend.model.Cargo;
import unpsjb.labprog.backend.model.TipoDesignacion;

@Component
public class CargoValidator {
    
     public void validar(Cargo cargo) {
        if (cargo.getTipoDesignacion() == TipoDesignacion.CARGO && cargo.getDivision() != null) {
            throw new IllegalArgumentException("Cargo de " + cargo.getNombre() + " es CARGO y no corresponde asignar división");
        }

        if (cargo.getTipoDesignacion() == TipoDesignacion.ESPACIO_CURRICULAR && cargo.getDivision() == null) {
            throw new IllegalArgumentException("Espacio Curricular " + cargo.getNombre() + " falta asignar división");
        }
    }
}
