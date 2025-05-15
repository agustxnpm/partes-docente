package unpsjb.labprog.backend.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import unpsjb.labprog.backend.model.Cargo;
import unpsjb.labprog.backend.model.Designacion;
import unpsjb.labprog.backend.model.Division;
import unpsjb.labprog.backend.model.Persona;

@Component
public class Validator {

    @Autowired
    private CargoValidator cargoValidator;

    @Autowired
    private PersonaValidator personaValidator;

    @Autowired
    private DivisionValidator divisionValidator;

    @Autowired
    private DesignacionValidator designacionValidator;

    public void validarCargo(Cargo cargo) {
        cargoValidator.validar(cargo);
    }

    public void validarPersona(Persona persona) {
        personaValidator.validarPersona(persona);
    }

    public void validarBorradoPersona(Persona persona) {
        personaValidator.validarBorrado(persona);
    }

    public void validarDni(Long dni) {
        personaValidator.validarDni(dni);
    }

    public void validarDivision(Division division) {
        divisionValidator.validarDivision(division);
    }

    public void validarBorradoDivision(Division division) {
        divisionValidator.validarBorrado(division);
    }

    public void validarDesignacion(Designacion designacion) {
        designacionValidator.validarDesignacion(designacion);
    }
}
