package unpsjb.labprog.backend.business.interfaces.mensajes;

import unpsjb.labprog.backend.model.Persona;

/**
 * Interfaz para constructor de mensajes específicos de Persona.
 * Extiende la interfaz base IMensajeBuilder aplicando el principio DIP.
 */
public interface IPersonaMensajeBuilder extends IMensajeBuilder<Persona> {
}
