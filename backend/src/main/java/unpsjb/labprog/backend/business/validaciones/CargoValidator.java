package unpsjb.labprog.backend.business.validaciones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import unpsjb.labprog.backend.business.interfaces.ICargoService;
import unpsjb.labprog.backend.business.interfaces.ICargoValidator;
import unpsjb.labprog.backend.business.interfaces.IDivisionService;
import unpsjb.labprog.backend.model.Cargo;
import unpsjb.labprog.backend.model.Division;
import unpsjb.labprog.backend.model.TipoDesignacion;

/**
 * Validador para operaciones relacionadas con la entidad Cargo.
 * 
 * Esta clase implementa el Principio de Inversión de Dependencias (DIP) del SOLID,
 * dependiendo de abstracciones (interfaces) en lugar de clases concretas:
 * - ICargoService: Interface para operaciones de cargo
 * - IDivisionService: Interface para operaciones de división
 * 
 * 
 */
@Component
public class CargoValidator implements ICargoValidator {

    /**
     * Servicio de división inyectado mediante interfaz (DIP).
     * Se usa @Lazy para evitar dependencias circulares.
     */
    @Autowired
    @Lazy
    private IDivisionService divisionService;

    /**
     * Servicio de cargo inyectado mediante interfaz (DIP).
     * Se usa @Lazy para evitar dependencias circulares.
     */
    @Autowired
    @Lazy
    private ICargoService cargoService;

    public void validarCargo(Cargo cargo) {

        if (cargo.getTipoDesignacion() == TipoDesignacion.CARGO && cargo.getDivision() != null) {
            throw new IllegalArgumentException(
                    "Cargo de " + cargo.getNombre() + " es CARGO y no corresponde asignar división");
        }

        if (cargo.getTipoDesignacion() == TipoDesignacion.ESPACIO_CURRICULAR && cargo.getDivision() == null) {
            throw new IllegalArgumentException("Espacio curricular " + cargo.getNombre() + " falta asignar división");
        }

        if (cargo.getDivision() != null) {
            Division divisionExistente = divisionService.buscarDivisionExistente(cargo.getDivision());
            if (divisionExistente != null) {
                cargo.setDivision(divisionExistente);
            } else {
                throw new IllegalArgumentException("La división especificada no existe.");
            }
        }

        if (cargo.getTipoDesignacion() == TipoDesignacion.CARGO) {
            cargoService.findByNombre(cargo.getNombre()).ifPresent(c -> {
                // Solo lanzar excepción si es un cargo diferente (ID diferente)
                if (c.getId() != (cargo.getId())) {
                    throw new DataIntegrityViolationException("El cargo " + cargo.getNombre() + " ya existe.");
                }
            });
        }

        if (cargo.getTipoDesignacion() == TipoDesignacion.ESPACIO_CURRICULAR) {
            cargoService.findByNombreAndDivision(cargo.getNombre(), cargo.getDivision()).ifPresent(c -> {
                // Solo lanzar excepción si es un cargo diferente (ID diferente)
                if (c.getId() != (cargo.getId())) {
                    throw new DataIntegrityViolationException(
                            "El espacio curricular " + cargo.getNombre() +
                                    " en la division " + cargo.getDivision().getAnio() + "º" +
                                    cargo.getDivision().getNumDivision() + "º" + " ya existe.");
                }
            });
        }

    }
}
