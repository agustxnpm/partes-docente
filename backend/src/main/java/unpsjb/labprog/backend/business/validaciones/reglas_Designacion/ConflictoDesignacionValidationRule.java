package unpsjb.labprog.backend.business.validaciones.reglas_Designacion;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import unpsjb.labprog.backend.business.interfaces.servicios.IDesignacionService;
import unpsjb.labprog.backend.business.interfaces.servicios.ILicenciaService;
import unpsjb.labprog.backend.business.interfaces.validaciones.IDesignacionRule;
import unpsjb.labprog.backend.model.Designacion;
import unpsjb.labprog.backend.model.Division;
import unpsjb.labprog.backend.model.TipoDesignacion;

/**
 * Regla de validación para conflictos de designación.
 * Verifica que no existan conflictos con designaciones existentes.
 */
@Component
@Order(3)
public class ConflictoDesignacionValidationRule implements IDesignacionRule {

    @Autowired
    private IDesignacionService designacionService;

    @Autowired
    private ILicenciaService licenciaService;

    @Override
    public void validate(Designacion designacion) {
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

    @Override
    public String getRuleName() {
        return "Validación de Conflictos de Designación";
    }
}
