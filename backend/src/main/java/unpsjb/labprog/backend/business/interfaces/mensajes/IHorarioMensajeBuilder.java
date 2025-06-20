package unpsjb.labprog.backend.business.interfaces.mensajes;

import unpsjb.labprog.backend.model.Horario;

/**
 * Interfaz para constructor de mensajes específicos de Horario.
 * Extiende la interfaz base IMensajeBuilder aplicando el principio DIP.
 */
public interface IHorarioMensajeBuilder extends IMensajeBuilder<Horario> {
}
