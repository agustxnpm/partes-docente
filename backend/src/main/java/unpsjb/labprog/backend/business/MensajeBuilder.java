package unpsjb.labprog.backend.business;

import org.springframework.stereotype.Component;

import unpsjb.labprog.backend.model.Cargo;
import unpsjb.labprog.backend.model.Division;
import unpsjb.labprog.backend.model.Persona;
import unpsjb.labprog.backend.model.TipoDesignacion;

@Component
public class MensajeBuilder {

    /* ---------------------------MENSAJES PARA LA ENTIDAD CARGO --------------------------- */

    public String generarMensajeExitoCargoCreado(Cargo cargo) {
        if (cargo.getTipoDesignacion() == TipoDesignacion.CARGO) {
            return "Cargo de " + cargo.getNombre() + " ingresado correctamente";
        }

        if (cargo.getTipoDesignacion() == TipoDesignacion.ESPACIO_CURRICULAR) {
            return "Espacio Curricular " + cargo.getNombre() + " para la división "
                    + cargo.getDivision().getAnio() + "º " + cargo.getDivision().getNumDivision() + "º "
                    + "Turno " + cargo.getDivision().getTurno() + " ingresado correctamente";
        }

        throw new IllegalArgumentException("Tipo de designación no válido");
    }

    /* ---------------------------MENSAJES PARA LA ENTIDAD CARGO --------------------------- */





    /* ---------------------------MENSAJES PARA LA ENTIDAD DIVISION --------------------------- */

    public String generarMensajeExitoDivisionCreada(Division division) {
        return "División " + division.getAnio() + "º" + " " + division.getNumDivision() + "º turno "
                + division.getTurno() + " ingresada correctamente";
    }

    public String generarMensajeDivisionDuplicada(Division division) {
        return "Ya existe la division " + division.getAnio() + "º" + " " + division.getNumDivision() + "º turno "
                + division.getTurno();
    }

    public String generarMensajeExitoDivisionActualizada(Division division) {
        return "División " + division.getAnio() + "º" + " " + division.getNumDivision() + "º turno "
                + division.getTurno() + " actualizada correctamente";
    }

    public String generarMensajeExitoDivisionBorrada(Division division) {
        return "División " + division.getAnio() + "º" + " " + division.getNumDivision() + "º turno "
                + division.getTurno() + " borrada correctamente";
    }
    /* ---------------------------MENSAJES PARA LA ENTIDAD DIVISION --------------------------- */







    /* ---------------------------MENSAJES PARA LA ENTIDAD PERSONA --------------------------- */
    public String generarMensajeExitoPersonaActualizada(Persona persona) {
        return persona.getNombre() + " " + persona.getApellido() + " con DNI " + persona.getDni()
                + " actualizado/a correctamente";
    }

    public String generarMensajeExitoPersonaBorrada(Persona persona) {
        return persona.getNombre() + " " + persona.getApellido() + " con DNI " + persona.getDni()
                + " borrado/a correctamente";
    }

    public String generarMensajeExitoPersonaCreada(Persona persona) {
        return persona.getNombre() + " " + persona.getApellido() + " con DNI " + persona.getDni()
                + " ingresado/a correctamente";
    }
     /* ---------------------------MENSAJES PARA LA ENTIDAD PERSONA --------------------------- */

}