package unpsjb.labprog.backend.business.validaciones;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import unpsjb.labprog.backend.business.interfaces.servicios.ICargoService;
import unpsjb.labprog.backend.business.interfaces.servicios.IDesignacionService;
import unpsjb.labprog.backend.business.interfaces.servicios.ILicenciaService;
import unpsjb.labprog.backend.business.interfaces.validaciones.IDesignacionValidator;
import unpsjb.labprog.backend.model.Cargo;
import unpsjb.labprog.backend.model.Designacion;
import unpsjb.labprog.backend.model.Division;
import unpsjb.labprog.backend.model.TipoDesignacion;

/**
 * Validador para operaciones relacionadas con la entidad Designación.
 * 
 * Esta clase implementa el Principio de Inversión de Dependencias (DIP) de SOLID,
 * dependiendo de abstracciones (interfaces) en lugar de clases concretas:
 * - ICargoService: Interface para operaciones de cargo
 * - IDesignacionService: Interface para operaciones de designación
 * 
 * 
 * 
 */
@Component
public class DesignacionValidator implements IDesignacionValidator {

    /**
     * Servicio de cargo inyectado mediante interfaz (DIP).
     * Se usa @Lazy para evitar dependencias circulares.
     */
    @Autowired
    private ICargoService cargoService;

    /**
     * Servicio de designación inyectado mediante interfaz (DIP).
     * Se usa @Lazy para evitar dependencias circulares.
     */
    @Autowired
    private IDesignacionService designacionService;

    /**
     * Servicio de licencia inyectado mediante interfaz (DIP).
     */
    @Autowired
    private ILicenciaService licenciaService;

    public void validarDesignacion(Designacion designacion) {

        if (designacion == null) {
            throw new IllegalArgumentException("La designación no puede ser nula");
        }
        if (designacion.getCargo() == null) {
            throw new IllegalArgumentException("El cargo no puede ser nulo");
        }

        if (designacion.getPersona() == null) {
            throw new IllegalArgumentException("La persona no puede ser nula");
        }

        if (designacion.getCargo().getTipoDesignacion() == TipoDesignacion.CARGO
                && designacion.getCargo().getDivision() != null) {
            throw new IllegalArgumentException("Un cargo tipo CARGO no debe tener división asignada");
        }

        // Validación específica para ESPACIO_CURRICULAR
        if (designacion.getCargo().getTipoDesignacion() == TipoDesignacion.ESPACIO_CURRICULAR) {
            Division division = designacion.getCargo().getDivision();
            if (division == null) {
                throw new IllegalArgumentException("Un ESPACIO_CURRICULAR requiere una división");
            }

            // Buscar el cargo específico que coincida con nombre y división
            Optional<Cargo> cargoExacto = cargoService.findByNombreAndDivisionExacta(
                    designacion.getCargo().getNombre(),
                    division);

            if (!cargoExacto.isPresent()) {
                throw new IllegalArgumentException(
                        String.format("No existe el espacio curricular %s para la división %dº %dº turno %s",
                                designacion.getCargo().getNombre(),
                                division.getAnio(),
                                division.getNumDivision(),
                                division.getTurno()));
            }

            // Actualizar el cargo en la designación con el cargo exacto encontrado
            designacion.setCargo(cargoExacto.get());
        }

        if (designacion.getFechaInicio() == null) {
            throw new IllegalArgumentException("La fecha de inicio es obligatoria");
        }

        if (designacion.getFechaFin() != null &&
                designacion.getFechaFin().isBefore(designacion.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha fin no puede ser anterior a la fecha inicio");
        }

        validarConflictosDesignacion(designacion);

    }

    private void validarConflictosDesignacion(Designacion nuevaDesignacion) {
        // Buscar designaciones existentes que se solapen con la nueva
        List<Designacion> designacionesConflictivas = designacionService
                .findDesignacionesSuperpuestas(
                        nuevaDesignacion.getCargo().getId(),
                        nuevaDesignacion.getFechaInicio(),
                        nuevaDesignacion.getFechaFin(),
                        nuevaDesignacion.getId());

        if (designacionesConflictivas.isEmpty()) {
            return; // No hay conflictos
        }

        // Verificar cada designación conflictiva
        for (Designacion existente : designacionesConflictivas) {
            LocalDate fechaFinNueva = nuevaDesignacion.getFechaFin() != null ? nuevaDesignacion.getFechaFin()
                    : LocalDate.now().plusYears(100);

            // Verificar si existe conflicto real considerando licencias
            if (existeConflictoReal(existente, nuevaDesignacion, fechaFinNueva)) {
                String mensaje = construirMensajeError(existente, nuevaDesignacion);
                throw new IllegalArgumentException(mensaje);
            }
        }
    }

    /**
     * Verifica si existe un conflicto real entre una designación existente y una nueva
     * considerando las licencias tanto de la persona existente como de otras personas
     * que puedan justificar la designación existente.
     */
    private boolean existeConflictoReal(Designacion existente, Designacion nueva, LocalDate fechaFinNueva) {
        // Primero verificar si la persona existente tiene licencias que cubran el período
        if (licenciaService.licenciasCubrenPeriodoCompleto(existente.getPersona(), nueva.getCargo(),
                nueva.getFechaInicio(), fechaFinNueva)) {
            return false; // No hay conflicto, la persona existente tiene licencias
        }

        // Si la persona existente no tiene licencias que cubran el período,
        // verificar si la designación existente puede estar justificada por
        // las licencias de otras personas (suplencias en cascada)
        if (existeJustificacionPorOtrasLicencias(existente, nueva.getFechaInicio(), fechaFinNueva)) {
            return false; // No hay conflicto, existe justificación por otras licencias
        }

        return true; // Conflicto real
    }

    /**
     * Verifica si existe justificación para una designación existente basada en
     * las licencias de otras personas que puedan haber originado la suplencia.
     */
    private boolean existeJustificacionPorOtrasLicencias(Designacion designacionExistente, 
            LocalDate fechaInicio, LocalDate fechaFin) {
        
        // Buscar otras designaciones superpuestas para el mismo cargo que no sean la existente
        List<Designacion> otrasDesignaciones = designacionService
                .findDesignacionesSuperpuestas(
                        designacionExistente.getCargo().getId(),
                        fechaInicio,
                        fechaFin,
                        designacionExistente.getId());

        // Verificar si alguna de estas otras designaciones tiene licencias que justifiquen la suplencia
        for (Designacion otraDesignacion : otrasDesignaciones) {
            if (licenciaService.licenciasCubrenPeriodoCompleto(otraDesignacion.getPersona(), 
                    designacionExistente.getCargo(), fechaInicio, fechaFin)) {
                return true; // Encontramos justificación
            }
        }

        return false; // No se encontró justificación
    }

    /**
     * Construye el mensaje de error apropiado para un conflicto de designación
     */
    private String construirMensajeError(Designacion existente, Designacion nueva) {
        if (existente.getCargo().getTipoDesignacion() == TipoDesignacion.CARGO) {
            return String.format(
                    "%s %s NO ha sido designado/a como %s. pues el cargo solicitado lo ocupa %s %s para el período",
                    nueva.getPersona().getNombre(),
                    nueva.getPersona().getApellido(),
                    existente.getCargo().getNombre().toLowerCase(),
                    existente.getPersona().getNombre(),
                    existente.getPersona().getApellido());
        } else {
            Division division = existente.getCargo().getDivision();
            return String.format(
                    "%s %s NO ha sido designado/a debido a que la asignatura %s de la división %dº %dº turno %s lo ocupa %s %s para el período",
                    nueva.getPersona().getNombre(),
                    nueva.getPersona().getApellido(),
                    existente.getCargo().getNombre(),
                    division.getAnio(),
                    division.getNumDivision(),
                    division.getTurno(),
                    existente.getPersona().getNombre(),
                    existente.getPersona().getApellido());
        }
    }

}
