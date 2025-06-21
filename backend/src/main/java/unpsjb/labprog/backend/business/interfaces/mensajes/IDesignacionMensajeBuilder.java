package unpsjb.labprog.backend.business.interfaces.mensajes;

import unpsjb.labprog.backend.model.Designacion;

/**
 * Interfaz para constructor de mensajes específicos de Designacion.
 * Extiende la interfaz base IMensajeBuilder aplicando el principio DIP.
 */
public interface IDesignacionMensajeBuilder extends IMensajeBuilder<Designacion> {
    
    /**
     * Genera mensaje específico para designación de suplencia.
     * @param suplente La designación del suplente
     * @return Mensaje de éxito para suplencia
     */
    String generarMensajeExitoDesignacionSuplencia(Designacion suplente);

    /**
     * Determina automáticamente el mensaje de éxito apropiado para una designación,
     * considerando si es titular o suplente.
     * @param designacion La designación guardada
     * @return Mensaje de éxito apropiado
     */
    String determinarMensajeExitoCreacion(Designacion designacion);
}
