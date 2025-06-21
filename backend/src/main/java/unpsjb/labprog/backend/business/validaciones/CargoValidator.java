package unpsjb.labprog.backend.business.validaciones;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import unpsjb.labprog.backend.business.interfaces.servicios.ICargoService;
import unpsjb.labprog.backend.business.interfaces.servicios.IDivisionService;
import unpsjb.labprog.backend.business.interfaces.validaciones.ICargoValidator;
import unpsjb.labprog.backend.model.Cargo;
import unpsjb.labprog.backend.model.Division;
import unpsjb.labprog.backend.model.Horario;
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
                            "El espacio curricular " + cargo.getNombre() + " en la division " + cargo.getDivision().getAnio() + "º"
                                    + cargo.getDivision().getNumDivision() + "º" + " ya existe.");
                }
            });
        }
        
        // Validar conflictos de horarios para espacios curriculares
        if (cargo.getTipoDesignacion() == TipoDesignacion.ESPACIO_CURRICULAR && 
            cargo.getDivision() != null && 
            cargo.getHorario() != null && 
            !cargo.getHorario().isEmpty()) {
            validarConflictosHorarios(cargo);
        }

    }
    
    /**
     * Valida que no haya conflictos de horarios para la misma división.
     * Un conflicto ocurre cuando dos espacios curriculares diferentes
     * tienen asignado el mismo horario (día y hora) para la misma división.
     */
    /**
     * Valida que no haya conflictos de horarios para la misma división
     * Optimizado para complejidad O(n + m) usando HashSet para búsqueda eficiente
     */
    private void validarConflictosHorarios(Cargo cargo) {
        // Crear un mapa de horarios ocupados para búsqueda O(1)
        Map<String, String> horariosOcupados = new HashMap<>();
        
        // Obtener todos los cargos de tipo ESPACIO_CURRICULAR para la misma división
        List<Cargo> cargosEnDivision = cargoService.findAll().stream()
            .filter(c -> c.getTipoDesignacion() == TipoDesignacion.ESPACIO_CURRICULAR)
            .filter(c -> c.getDivision() != null && c.getDivision().getId() == cargo.getDivision().getId())
            .filter(c -> c.getId() != cargo.getId()) // Excluir el cargo actual
            .filter(c -> c.getHorario() != null && !c.getHorario().isEmpty())
            .collect(java.util.stream.Collectors.toList());
        
        // Construir el mapa de horarios ocupados - O(n)
        for (Cargo cargoExistente : cargosEnDivision) {
            for (Horario horarioExistente : cargoExistente.getHorario()) {
                String claveHorario = horarioExistente.getDia() + "-" + horarioExistente.getHora();
                horariosOcupados.put(claveHorario, cargoExistente.getNombre());
            }
        }
        
        // Verificar conflictos con los nuevos horarios - O(m)
        for (Horario horarioNuevo : cargo.getHorario()) {
            String claveHorarioNuevo = horarioNuevo.getDia() + "-" + horarioNuevo.getHora();
            
            if (horariosOcupados.containsKey(claveHorarioNuevo)) {
                String cargoConflicto = horariosOcupados.get(claveHorarioNuevo);
                String mensaje = String.format(
                    "Conflicto de horarios detectado: El %s a la %dº hora ya está ocupado por '%s' en la división %dº%dº %s. " +
                    "No se puede asignar el mismo horario a '%s'.",
                    horarioNuevo.getDia().toLowerCase(),
                    horarioNuevo.getHora(),
                    cargoConflicto,
                    cargo.getDivision().getAnio(),
                    cargo.getDivision().getNumDivision(),
                    cargo.getDivision().getTurno().toString().toLowerCase(),
                    cargo.getNombre()
                );
                
                throw new IllegalArgumentException(mensaje);
            }
        }
    }
}
