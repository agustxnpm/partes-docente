package unpsjb.labprog.backend.business.interfaces.mensajes;

import unpsjb.labprog.backend.model.Licencia;

/**
 * Interfaz para constructor de mensajes específicos de Licencia.
 * Extiende la interfaz base IMensajeBuilder aplicando el principio DIP.
 */
public interface ILicenciaMensajeBuilder extends IMensajeBuilder<Licencia> {
    
    /**
     * Genera mensaje específico para otorgamiento de licencia.
     * @param licencia La licencia otorgada
     * @return Mensaje de éxito para otorgamiento
     */
    String generarMensajeExitoLicenciaOtorgada(Licencia licencia);
}
