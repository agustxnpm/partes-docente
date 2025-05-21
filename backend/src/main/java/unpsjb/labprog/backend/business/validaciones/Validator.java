package unpsjb.labprog.backend.business.validaciones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import unpsjb.labprog.backend.model.ArticuloLicencia;
import unpsjb.labprog.backend.model.Cargo;
import unpsjb.labprog.backend.model.Designacion;
import unpsjb.labprog.backend.model.Division;
import unpsjb.labprog.backend.model.Horario;
import unpsjb.labprog.backend.model.Licencia;
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

    @Autowired
    private HorarioValidator horarioValidator;

    @Autowired
    private ArticuloLicenciaValidator articuloLicenciaValidator;

    @Autowired
    private LicenciaValidator licenciaValidator;

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

    public void validarHorario(Horario horario) {
        horarioValidator.validarHorario(horario);
    }

    public void validarBorradoHorario(Horario horario) {
        horarioValidator.validarBorrado(horario);
    }

    public void validarLicencia(Licencia licencia) {
        licenciaValidator.validateLicencia(licencia);
    }

}
