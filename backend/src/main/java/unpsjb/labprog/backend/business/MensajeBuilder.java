package unpsjb.labprog.backend.business;

import org.springframework.stereotype.Component;

import unpsjb.labprog.backend.model.Cargo;
import unpsjb.labprog.backend.model.Designacion;
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
            return "Espacio curricular " + cargo.getNombre() + " para la división "
                    + cargo.getDivision().getAnio() + "º " + cargo.getDivision().getNumDivision() + "º "
                    + "Turno " + cargo.getDivision().getTurno() + " ingresado correctamente";
        }

        throw new IllegalArgumentException("Tipo de designación no válido");
    }

    public String generarMensajeExitoCargoActualizado(Cargo cargo) {
        if (cargo.getTipoDesignacion() == TipoDesignacion.CARGO) {
            return "Cargo de " + cargo.getNombre() + " actualizado correctamente";
        }

        if (cargo.getTipoDesignacion() == TipoDesignacion.ESPACIO_CURRICULAR) {
            return "Espacio curricular " + cargo.getNombre() + " para la división "
                    + cargo.getDivision().getAnio() + "º " + cargo.getDivision().getNumDivision() + "º "
                    + "Turno " + cargo.getDivision().getTurno() + " actualizado correctamente";
        }

        throw new IllegalArgumentException("Tipo de designación no válido");
    }

    public String generarMensajeExitoCargoBorrado(Cargo cargo) {
        if (cargo.getTipoDesignacion() == TipoDesignacion.CARGO) {
            return "Cargo de " + cargo.getNombre() + " borrado correctamente";
        }

        if (cargo.getTipoDesignacion() == TipoDesignacion.ESPACIO_CURRICULAR) {
            return "Espacio curricular " + cargo.getNombre() + " para la división "
                    + cargo.getDivision().getAnio() + "º " + cargo.getDivision().getNumDivision() + "º "
                    + "Turno " + cargo.getDivision().getTurno() + " borrado correctamente";
        }

        throw new IllegalArgumentException("Tipo de designación no válido");
    }

     /* -------------------------------------------------------------------------------------- */





    /* ---------------------------MENSAJES PARA LA ENTIDAD DIVISION --------------------------- */

    public String generarMensajeExitoDivisionCreada(Division division) {
        return "División " + division.getAnio() + "º" + " " + division.getNumDivision() + "º turno "
                + division.getTurno() + " ingresada correctamente";
    }

    public String generarMensajeDivisionDuplicada(Division division) {
        return "Ya existe la division " + division.getAnio() + "º" + " " + division.getNumDivision() + "º";
    }

    public String generarMensajeExitoDivisionActualizada(Division division) {
        return "División " + division.getAnio() + "º" + " " + division.getNumDivision() + "º turno "
                + division.getTurno() + " actualizada correctamente";
    }

    public String generarMensajeExitoDivisionBorrada(Division division) {
        return "División " + division.getAnio() + "º" + " " + division.getNumDivision() + "º turno "
                + division.getTurno() + " borrada correctamente";
    }

    public String generarMensajeFalloBorrado(Division division) {
        return "No se puede borrar la división " + division.getAnio() + "º" + " " + division.getNumDivision() + "º turno "
                + division.getTurno() + " porque tiene cargos asignados";
    }
     /* -------------------------------------------------------------------------------------- */







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

    
     /* -------------------------------------------------------------------------------------- */

    


    /* ---------------------------MENSAJES PARA LA ENTIDAD DESIGNACION --------------------------- */

    public String generarMensajeExitoDesignacionCreada(Designacion designacion) {
        

        if (designacion.getCargo().getTipoDesignacion() == TipoDesignacion.CARGO) {

            
            return designacion.getPersona().getNombre() + " " + designacion.getPersona().getApellido()
                            + " ha sido designado/a como " + designacion.getCargo().getNombre() + " exitosamente";
        }

        if (designacion.getCargo().getTipoDesignacion() == TipoDesignacion.ESPACIO_CURRICULAR) {
            return designacion.getPersona().getNombre() + " " + designacion.getPersona().getApellido()
                            + " ha sido designado/a a la asignatura " + designacion.getCargo().getNombre()
                            + " a la división " + designacion.getCargo().getDivision().getAnio() + "º "
                            + designacion.getCargo().getDivision().getNumDivision() + "º turno " + designacion.getCargo().getDivision().getTurno() + " exitosamente";
        }

        throw new IllegalArgumentException("Tipo de designación no válido");

    }

    public String generarMensajeExitoDesignacionActualizada(Designacion designacion) {
        if (designacion.getCargo().getTipoDesignacion() == TipoDesignacion.CARGO) {
            return designacion.getPersona().getNombre() + " " + designacion.getPersona().getApellido()
                            + " ha sido designado/a como " + designacion.getCargo().getNombre() + " exitosamente";
        }

        if (designacion.getCargo().getTipoDesignacion() == TipoDesignacion.ESPACIO_CURRICULAR) {
            return designacion.getPersona().getNombre() + " " + designacion.getPersona().getApellido()
                            + " ha sido designado/a a la asignatura " + designacion.getCargo().getNombre()
                            + " a la división " + designacion.getCargo().getDivision().getAnio() + "º "
                            + designacion.getCargo().getDivision().getNumDivision() + "º turno " + designacion.getCargo().getDivision().getTurno() + " exitosamente";
        }

        throw new IllegalArgumentException("Tipo de designación no válido");
    }
    
    public String generarMensajeExitoDesignacionBorrada(Designacion designacion) {
        if (designacion.getCargo().getTipoDesignacion() == TipoDesignacion.CARGO) {
            return designacion.getPersona().getNombre() + " " + designacion.getPersona().getApellido()
                            + " ha sido removido/a del cargo " + designacion.getCargo().getNombre() + " exitosamente";
        }

        if (designacion.getCargo().getTipoDesignacion() == TipoDesignacion.ESPACIO_CURRICULAR) {
            return designacion.getPersona().getNombre() + " " + designacion.getPersona().getApellido()
                            + " ha sido removido/a de la asignatura " + designacion.getCargo().getNombre()
                            + " de " + designacion.getCargo().getDivision().getAnio() + "º "
                            + designacion.getCargo().getDivision().getNumDivision() + "º turno " + designacion.getCargo().getDivision().getTurno() + " exitosamente";
        }

        throw new IllegalArgumentException("Tipo de designación no válido");
    }

 
    /*-------------------------------------------------------------------------------------- */

}