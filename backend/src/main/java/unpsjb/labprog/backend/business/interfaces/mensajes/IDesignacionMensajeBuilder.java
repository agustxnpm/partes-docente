package unpsjb.labprog.backend.business.interfaces.mensajes;

import unpsjb.labprog.backend.model.Designacion;
import unpsjb.labprog.backend.model.Persona;

/**
 * Interfaz para constructor de mensajes específicos de Designacion.
 * Extiende la interfaz base IMensajeBuilder aplicando el principio DIP.
 */
public interface IDesignacionMensajeBuilder extends IMensajeBuilder<Designacion> {
    
    /**
     * Genera mensaje específico para designación de suplencia.
     * @param suplente La designación del suplente
     * @param reemplazado La persona reemplazada
     * @return Mensaje de éxito para suplencia
     */
    String generarMensajeExitoDesignacionSuplencia(Designacion suplente, Persona reemplazado);
}
