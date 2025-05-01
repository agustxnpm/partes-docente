package unpsjb.labprog.backend.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import unpsjb.labprog.backend.model.Cargo;
import unpsjb.labprog.backend.model.Persona;

@Component
public class Validator {

    @Autowired
    private CargoValidator cargoValidator;

    @Autowired
    private PersonaValidator personaValidator;

    public void validarCargo(Cargo cargo) {
        cargoValidator.validar(cargo);
    }

    public void validarPersona(Persona persona) {
        personaValidator.validarPersona(persona);
    }

    public void validarDni(Long dni) {
        personaValidator.validarDni(dni);
    }
}
