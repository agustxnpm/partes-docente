package unpsjb.labprog.backend.business.interfaces.mensajes;

import unpsjb.labprog.backend.model.Division;

/**
 * Interfaz para constructor de mensajes específicos de Division.
 * Extiende la interfaz base IMensajeBuilder aplicando el principio DIP.
 */
public interface IDivisionMensajeBuilder extends IMensajeBuilder<Division> {
}
