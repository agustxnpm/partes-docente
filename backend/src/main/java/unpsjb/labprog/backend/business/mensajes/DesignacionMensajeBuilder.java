package unpsjb.labprog.backend.business.mensajes;

import org.springframework.stereotype.Component;

import unpsjb.labprog.backend.business.interfaces.mensajes.IDesignacionMensajeBuilder;
import unpsjb.labprog.backend.model.Designacion;
import unpsjb.labprog.backend.model.Persona;
import unpsjb.labprog.backend.model.TipoDesignacion;

/**
 * Implementación del constructor de mensajes para la entidad Designacion.
 * Aplica el principio SRP centralizando la lógica de mensajes únicamente para Designacion.
 * Aplica el principio DIP implementando la abstracción IDesignacionMensajeBuilder.
 */
@Component
public class DesignacionMensajeBuilder implements IDesignacionMensajeBuilder {

    @Override
    public String generarMensajeExitoCreacion(Designacion designacion) {
        if (designacion.getCargo().getTipoDesignacion() == TipoDesignacion.CARGO) {
            return designacion.getPersona().getNombre() + " " + designacion.getPersona().getApellido()
                    + " ha sido designado/a como " + designacion.getCargo().getNombre() + " exitosamente";
        }

        if (designacion.getCargo().getTipoDesignacion() == TipoDesignacion.ESPACIO_CURRICULAR) {
            return designacion.getPersona().getNombre() + " " + designacion.getPersona().getApellido()
                    + " ha sido designado/a a la asignatura " + designacion.getCargo().getNombre()
                    + " a la división " + designacion.getCargo().getDivision().getAnio() + "º "
                    + designacion.getCargo().getDivision().getNumDivision() + "º turno "
                    + designacion.getCargo().getDivision().getTurno() + " exitosamente";
        }

        throw new IllegalArgumentException("Tipo de designación no válido");
    }

    @Override
    public String generarMensajeExitoActualizacion(Designacion designacion) {
        return "Designacion de " + designacion.getPersona().getNombre() + " " + designacion.getPersona().getApellido()
                + " actualizada correctamente";
    }

    @Override
    public String generarMensajeExitoBorrado(Designacion designacion) {
        if (designacion.getCargo().getTipoDesignacion() == TipoDesignacion.CARGO) {
            return designacion.getPersona().getNombre() + " " + designacion.getPersona().getApellido()
                    + " ha sido removido/a del cargo " + designacion.getCargo().getNombre() + " exitosamente";
        }

        if (designacion.getCargo().getTipoDesignacion() == TipoDesignacion.ESPACIO_CURRICULAR) {
            return designacion.getPersona().getNombre() + " " + designacion.getPersona().getApellido()
                    + " ha sido removido/a de la asignatura " + designacion.getCargo().getNombre()
                    + " de " + designacion.getCargo().getDivision().getAnio() + "º "
                    + designacion.getCargo().getDivision().getNumDivision() + "º turno "
                    + designacion.getCargo().getDivision().getTurno() + " exitosamente";
        }

        throw new IllegalArgumentException("Tipo de designación no válido");
    }

    @Override
    public String generarMensajeExitoDesignacionSuplencia(Designacion suplente, Persona reemplazado) {
        String tipoCargoStr = suplente.getCargo().getTipoDesignacion() == TipoDesignacion.CARGO ? "al cargo" : "a la asignatura";
    
        return String.format("%s %s ha sido designado/a %s %s exitosamente, en reemplazo de %s %s",
                suplente.getPersona().getNombre(),
                suplente.getPersona().getApellido(),
                tipoCargoStr,
                suplente.getCargo().getNombre().toLowerCase(),
                reemplazado.getNombre(),
                reemplazado.getApellido());
    }
}
