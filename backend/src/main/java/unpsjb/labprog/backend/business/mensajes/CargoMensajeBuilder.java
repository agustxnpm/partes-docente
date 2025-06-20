package unpsjb.labprog.backend.business.mensajes;

import org.springframework.stereotype.Component;

import unpsjb.labprog.backend.business.interfaces.mensajes.ICargoMensajeBuilder;
import unpsjb.labprog.backend.model.Cargo;
import unpsjb.labprog.backend.model.TipoDesignacion;

/**
 * Implementación del constructor de mensajes para la entidad Cargo.
 * Aplica el principio SRP (Single Responsibility Principle) centralizando
 * la lógica de construcción de mensajes únicamente para Cargo.
 * 
 * Aplica el principio DIP (Dependency Inversion Principle) implementando
 * la abstracción ICargoMensajeBuilder.
 */
@Component
public class CargoMensajeBuilder implements ICargoMensajeBuilder {

    @Override
    public String generarMensajeExitoCreacion(Cargo cargo) {
        if (cargo.getTipoDesignacion() == TipoDesignacion.CARGO) {
            return "Cargo de " + cargo.getNombre() + " ingresado correctamente";
        }

        if (cargo.getTipoDesignacion() == TipoDesignacion.ESPACIO_CURRICULAR) {
            return "Espacio curricular " + cargo.getNombre() + " para la división "
                    + cargo.getDivision().getAnio() + "º " + cargo.getDivision().getNumDivision() + "º "
                    + "Turno " + cargo.getDivision().getTurno() + " ingresado correctamente";
        }

        throw new IllegalArgumentException("Tipo de designación no válido");
    }

    @Override
    public String generarMensajeExitoActualizacion(Cargo cargo) {
        if (cargo.getTipoDesignacion() == TipoDesignacion.CARGO) {
            return "Cargo de " + cargo.getNombre() + " actualizado correctamente";
        }

        if (cargo.getTipoDesignacion() == TipoDesignacion.ESPACIO_CURRICULAR) {
            return "Espacio curricular " + cargo.getNombre() + " para la división "
                    + cargo.getDivision().getAnio() + "º " + cargo.getDivision().getNumDivision() + "º "
                    + "Turno " + cargo.getDivision().getTurno() + " actualizado correctamente";
        }

        throw new IllegalArgumentException("Tipo de designación no válido");
    }

    @Override
    public String generarMensajeExitoBorrado(Cargo cargo) {
        if (cargo.getTipoDesignacion() == TipoDesignacion.CARGO) {
            return "Cargo de " + cargo.getNombre() + " borrado correctamente";
        }

        if (cargo.getTipoDesignacion() == TipoDesignacion.ESPACIO_CURRICULAR) {
            return "Espacio curricular " + cargo.getNombre() + " para la división "
                    + cargo.getDivision().getAnio() + "º " + cargo.getDivision().getNumDivision() + "º "
                    + "Turno " + cargo.getDivision().getTurno() + " borrado correctamente";
        }

        throw new IllegalArgumentException("Tipo de designación no válido");
    }
}
