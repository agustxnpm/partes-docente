package unpsjb.labprog.backend.business.interfaces.mensajes;

import unpsjb.labprog.backend.model.Cargo;

/**
 * Interfaz para constructor de mensajes específicos de Cargo.
 * Extiende la interfaz base IMensajeBuilder aplicando el principio DIP.
 */
public interface ICargoMensajeBuilder extends IMensajeBuilder<Cargo> {
}
