package unpsjb.labprog.backend.business.interfaces.mensajes;

/**
 * Interfaz base para constructores de mensajes.
 * Aplica el principio DIP (Dependency Inversion Principle) proporcionando una abstracción
 * común para todos los constructores de mensajes específicos de entidades.
 * 
 * @param <T> Tipo de entidad para la cual se construyen mensajes
 */
public interface IMensajeBuilder<T> {
    
    /**
     * Genera mensaje de éxito para creación de entidad.
     * @param entidad La entidad creada
     * @return Mensaje de éxito
     */
    String generarMensajeExitoCreacion(T entidad);
    
    /**
     * Genera mensaje de éxito para actualización de entidad.
     * @param entidad La entidad actualizada
     * @return Mensaje de éxito
     */
    String generarMensajeExitoActualizacion(T entidad);
    
    /**
     * Genera mensaje de éxito para borrado de entidad.
     * @param entidad La entidad borrada
     * @return Mensaje de éxito
     */
    String generarMensajeExitoBorrado(T entidad);
}
