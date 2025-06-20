package unpsjb.labprog.backend.business.mensajes;

import org.springframework.stereotype.Component;

import unpsjb.labprog.backend.business.interfaces.mensajes.ILicenciaMensajeBuilder;
import unpsjb.labprog.backend.model.Licencia;

/**
 * Implementación del constructor de mensajes para la entidad Licencia.
 * Aplica el principio SRP centralizando la lógica de mensajes únicamente para Licencia.
 * Aplica el principio DIP implementando la abstracción ILicenciaMensajeBuilder.
 */
@Component
public class LicenciaMensajeBuilder implements ILicenciaMensajeBuilder {

    @Override
    public String generarMensajeExitoCreacion(Licencia licencia) {
        return generarMensajeExitoLicenciaOtorgada(licencia);
    }

    @Override
    public String generarMensajeExitoActualizacion(Licencia licencia) {
        return "Licencia artículo " + licencia.getArticuloLicencia().getArticulo() + " a "
                + licencia.getPersona().getNombre() + " " + licencia.getPersona().getApellido()
                + " actualizada correctamente";
    }

    @Override
    public String generarMensajeExitoBorrado(Licencia licencia) {
        return "Licencia artículo " + licencia.getArticuloLicencia().getArticulo() + " de "
                + licencia.getPersona().getNombre() + " " + licencia.getPersona().getApellido()
                + " eliminada correctamente";
    }

    @Override
    public String generarMensajeExitoLicenciaOtorgada(Licencia licencia) {
        return "Se otorga Licencia artículo " + licencia.getArticuloLicencia().getArticulo() + " a "
                + licencia.getPersona().getNombre() + " " + licencia.getPersona().getApellido();
    }
}
