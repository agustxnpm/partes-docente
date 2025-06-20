package unpsjb.labprog.backend.business.mensajes;

import org.springframework.stereotype.Component;

import unpsjb.labprog.backend.business.interfaces.mensajes.IDivisionMensajeBuilder;
import unpsjb.labprog.backend.model.Division;

/**
 * Implementación del constructor de mensajes para la entidad Division.
 * Aplica el principio SRP centralizando la lógica de mensajes únicamente para Division.
 * Aplica el principio DIP implementando la abstracción IDivisionMensajeBuilder.
 */
@Component
public class DivisionMensajeBuilder implements IDivisionMensajeBuilder {

    @Override
    public String generarMensajeExitoCreacion(Division division) {
        return "División " + division.getAnio() + "º" + " " + division.getNumDivision() + "º turno "
                + division.getTurno() + " ingresada correctamente";
    }

    @Override
    public String generarMensajeExitoActualizacion(Division division) {
        return "División " + division.getAnio() + "º" + " " + division.getNumDivision() + "º turno "
                + division.getTurno() + " actualizada correctamente";
    }

    @Override
    public String generarMensajeExitoBorrado(Division division) {
        return "División " + division.getAnio() + "º" + " " + division.getNumDivision() + "º turno "
                + division.getTurno() + " borrada correctamente";
    }
}
