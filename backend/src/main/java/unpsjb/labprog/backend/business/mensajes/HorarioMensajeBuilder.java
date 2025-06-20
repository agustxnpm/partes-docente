package unpsjb.labprog.backend.business.mensajes;

import org.springframework.stereotype.Component;

import unpsjb.labprog.backend.business.interfaces.mensajes.IHorarioMensajeBuilder;
import unpsjb.labprog.backend.model.Horario;

/**
 * Implementación del constructor de mensajes para la entidad Horario.
 * Aplica el principio SRP centralizando la lógica de mensajes únicamente para Horario.
 * Aplica el principio DIP implementando la abstracción IHorarioMensajeBuilder.
 */
@Component
public class HorarioMensajeBuilder implements IHorarioMensajeBuilder {

    @Override
    public String generarMensajeExitoCreacion(Horario horario) {
        return "Horario de " + horario.getDia() + " a las " + horario.getHora() + " hs. creado exitosamente";
    }

    @Override
    public String generarMensajeExitoActualizacion(Horario horario) {
        return "Horario de " + horario.getDia() + " a las " + horario.getHora() + " hs. actualizado exitosamente";
    }

    @Override
    public String generarMensajeExitoBorrado(Horario horario) {
        return "Horario de " + horario.getDia() + " a las " + horario.getHora() + " hs. borrado exitosamente";
    }
}
