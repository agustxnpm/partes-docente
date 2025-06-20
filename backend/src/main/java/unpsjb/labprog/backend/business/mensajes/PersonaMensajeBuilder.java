package unpsjb.labprog.backend.business.mensajes;

import org.springframework.stereotype.Component;

import unpsjb.labprog.backend.business.interfaces.mensajes.IPersonaMensajeBuilder;
import unpsjb.labprog.backend.model.Persona;

/**
 * Implementación del constructor de mensajes para la entidad Persona.
 * Aplica el principio SRP centralizando la lógica de mensajes únicamente para Persona.
 * Aplica el principio DIP implementando la abstracción IPersonaMensajeBuilder.
 */
@Component
public class PersonaMensajeBuilder implements IPersonaMensajeBuilder {

    @Override
    public String generarMensajeExitoCreacion(Persona persona) {
        return persona.getNombre() + " " + persona.getApellido() + " con DNI " + persona.getDni()
                + " ingresado/a correctamente";
    }

    @Override
    public String generarMensajeExitoActualizacion(Persona persona) {
        return persona.getNombre() + " " + persona.getApellido() + " con DNI " + persona.getDni()
                + " actualizado/a correctamente";
    }

    @Override
    public String generarMensajeExitoBorrado(Persona persona) {
        return persona.getNombre() + " " + persona.getApellido() + " con DNI " + persona.getDni()
                + " borrado/a correctamente";
    }
}
