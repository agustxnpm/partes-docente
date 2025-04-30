package unpsjb.labprog.backend.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import unpsjb.labprog.backend.model.Cargo;

@Component
public class Validator {

    @Autowired
    private CargoValidator cargoValidator;

    public void validarCargo(Cargo cargo) {
        cargoValidator.validar(cargo);
    }

}
