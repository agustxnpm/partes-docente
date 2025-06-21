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
    private IDivisionService divisionService;

    /**
     * Servicio de cargo inyectado mediante interfaz (DIP).
     * Se usa @Lazy para evitar dependencias circulares.
     */
    @Autowired
    @Lazy
    private ICargoService cargoService;

    public void validarCargo(Cargo cargo) {
        validarTipoDesignacionYDivision(cargo);
        validarYAsignarDivisionExistente(cargo);
        validarUnicidadCargo(cargo);
        validarUnicidadEspacioCurricular(cargo);
        validarHorariosEspacioCurricular(cargo);
    }

    /**
     * Valida la coherencia entre el tipo de designación y la división asignada.
     */
    private void validarTipoDesignacionYDivision(Cargo cargo) {
        if (cargo.getTipoDesignacion() == TipoDesignacion.CARGO && cargo.getDivision() != null) {
            throw new IllegalArgumentException(
                    "Cargo de " + cargo.getNombre() + " es CARGO y no corresponde asignar división");
        }

        if (cargo.getTipoDesignacion() == TipoDesignacion.ESPACIO_CURRICULAR && cargo.getDivision() == null) {
            throw new IllegalArgumentException("Espacio curricular " + cargo.getNombre() + " falta asignar división");
        }
    }

    /**
     * Valida que la división especificada exista y la asigna al cargo.
     */
    private void validarYAsignarDivisionExistente(Cargo cargo) {
        if (cargo.getDivision() != null) {
            Division divisionExistente = divisionService.buscarDivisionExistente(cargo.getDivision());
            if (divisionExistente != null) {
                cargo.setDivision(divisionExistente);
            } else {
                throw new IllegalArgumentException("La división especificada no existe.");
            }
        }
    }

    /**
     * Valida que no exista otro cargo con el mismo nombre.
     */
    private void validarUnicidadCargo(Cargo cargo) {
        if (cargo.getTipoDesignacion() == TipoDesignacion.CARGO) {
            cargoService.findByNombre(cargo.getNombre()).ifPresent(c -> {
                // Solo lanzar excepción si es un cargo diferente (ID diferente)
                if (c.getId() != (cargo.getId())) {
                    throw new DataIntegrityViolationException("El cargo " + cargo.getNombre() + " ya existe.");
                }
            });
        }
    }

    /**
     * Valida que no exista otro espacio curricular con el mismo nombre en la misma división.
     */
    private void validarUnicidadEspacioCurricular(Cargo cargo) {
        if (cargo.getTipoDesignacion() == TipoDesignacion.ESPACIO_CURRICULAR) {
            cargoService.findByNombreAndDivision(cargo.getNombre(), cargo.getDivision()).ifPresent(c -> {
                // Solo lanzar excepción si es un cargo diferente (ID diferente)
                if (c.getId() != (cargo.getId())) {
                    throw new DataIntegrityViolationException(
                            "El espacio curricular " + cargo.getNombre() + " en la division " 
                            + cargo.getDivision().getAnio() + "º"
                            + cargo.getDivision().getNumDivision() + "º" + " ya existe.");
                }
            });
        }
    }

    /**
     * Valida los conflictos de horarios para espacios curriculares.
     */
    private void validarHorariosEspacioCurricular(Cargo cargo) {
        if (cargo.getTipoDesignacion() == TipoDesignacion.ESPACIO_CURRICULAR && 
            cargo.getDivision() != null && 
            cargo.getHorario() != null && 
            !cargo.getHorario().isEmpty()) {
            validarConflictosHorarios(cargo);
        }
    }
    

    /**
     * Valida que no haya conflictos de horarios para la misma división.
     */
    private void validarConflictosHorarios(Cargo cargo) {
        List<Cargo> cargosEnDivision = obtenerCargosEspaciosCurricularesEnDivision(cargo);
        Map<String, String> horariosOcupados = construirMapaHorariosOcupados(cargosEnDivision);
        verificarConflictosHorarios(cargo, horariosOcupados);
    }

    /**
     * Obtiene todos los cargos de tipo ESPACIO_CURRICULAR para la misma división,
     * excluyendo el cargo actual.
     */
    private List<Cargo> obtenerCargosEspaciosCurricularesEnDivision(Cargo cargo) {
        return cargoService.findAll().stream()
            .filter(c -> esEspacioCurricular(c))
            .filter(c -> esMismaDivision(c, cargo))
            .filter(c -> noEsElMismoCargo(c, cargo))
            .filter(c -> tieneHorarios(c))
            .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Verifica si un cargo es de tipo ESPACIO_CURRICULAR.
     */
    private boolean esEspacioCurricular(Cargo cargo) {
        return cargo.getTipoDesignacion() == TipoDesignacion.ESPACIO_CURRICULAR;
    }

    /**
     * Verifica si dos cargos pertenecen a la misma división.
     */
    private boolean esMismaDivision(Cargo cargoExistente, Cargo cargoNuevo) {
        return cargoExistente.getDivision() != null && 
               cargoNuevo.getDivision() != null &&
               cargoExistente.getDivision().getId() == cargoNuevo.getDivision().getId();
    }

    /**
     * Verifica que no sea el mismo cargo (para casos de actualización).
     */
    private boolean noEsElMismoCargo(Cargo cargoExistente, Cargo cargoNuevo) {
        return cargoExistente.getId() != cargoNuevo.getId();
    }

    /**
     * Verifica si un cargo tiene horarios asignados.
     */
    private boolean tieneHorarios(Cargo cargo) {
        return cargo.getHorario() != null && !cargo.getHorario().isEmpty();
    }

    /**
     * Construye un mapa con los horarios ocupados para búsqueda eficiente O(1).
     * La clave es "dia-hora" y el valor es el nombre del cargo que ocupa ese horario.
     */
    private Map<String, String> construirMapaHorariosOcupados(List<Cargo> cargosEnDivision) {
        Map<String, String> horariosOcupados = new HashMap<>();
        
        for (Cargo cargoExistente : cargosEnDivision) {
            for (Horario horarioExistente : cargoExistente.getHorario()) {
                String claveHorario = construirClaveHorario(horarioExistente);
                horariosOcupados.put(claveHorario, cargoExistente.getNombre());
            }
        }
        
        return horariosOcupados;
    }

    /**
     * Construye la clave única para identificar un horario específico.
     */
    private String construirClaveHorario(Horario horario) {
        return horario.getDia() + "-" + horario.getHora();
    }

    /**
     * Verifica si existen conflictos entre los horarios del nuevo cargo
     * y los horarios ya ocupados en la división.
     */
    private void verificarConflictosHorarios(Cargo cargo, Map<String, String> horariosOcupados) {
        for (Horario horarioNuevo : cargo.getHorario()) {
            String claveHorarioNuevo = construirClaveHorario(horarioNuevo);
            
            if (horariosOcupados.containsKey(claveHorarioNuevo)) {
                String cargoConflicto = horariosOcupados.get(claveHorarioNuevo);
                String mensaje = construirMensajeConflictoHorario(cargo, horarioNuevo, cargoConflicto);
                throw new IllegalArgumentException(mensaje);
            }
        }
    }

    /**
     * Construye el mensaje de error detallado para conflictos de horario.
     */
    private String construirMensajeConflictoHorario(Cargo cargo, Horario horario, String cargoConflicto) {
        return String.format(
            "Conflicto de horarios detectado: El %s a la %dº hora ya está ocupado por '%s' en la división %dº%dº %s. " +
            "No se puede asignar el mismo horario a '%s'.",
            horario.getDia().toLowerCase(),
            horario.getHora(),
            cargoConflicto,
            cargo.getDivision().getAnio(),
            cargo.getDivision().getNumDivision(),
            cargo.getDivision().getTurno().toString().toLowerCase(),
            cargo.getNombre()
        );
    }
}
