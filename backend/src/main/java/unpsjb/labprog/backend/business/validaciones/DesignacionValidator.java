package unpsjb.labprog.backend.business.validaciones;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import unpsjb.labprog.backend.business.CargoService;
import unpsjb.labprog.backend.business.DesignacionService;
import unpsjb.labprog.backend.business.LicenciaRepository;
import unpsjb.labprog.backend.business.LicenciaService;
import unpsjb.labprog.backend.model.Cargo;
import unpsjb.labprog.backend.model.Designacion;
import unpsjb.labprog.backend.model.Division;
import unpsjb.labprog.backend.model.Licencia;
import unpsjb.labprog.backend.model.Persona;
import unpsjb.labprog.backend.model.TipoDesignacion;

@Component
public class DesignacionValidator {

    @Autowired
    @Lazy
    private CargoService cargoService;

    @Autowired
    @Lazy
    private DesignacionService designacionService;

    @Autowired
    @Lazy
    LicenciaService licenciaService;

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

            // Verificar si las licencias de la persona existente cubren COMPLETAMENTE
            // el período de la nueva designación
            if (!licenciasCubrenPeriodoCompleto(existente.getPersona(), nuevaDesignacion.getCargo(),
                    nuevaDesignacion.getFechaInicio(), fechaFinNueva)) {
                // No hay licencias que cubran completamente el período, es un conflicto real
                String mensaje;
                if (existente.getCargo().getTipoDesignacion() == TipoDesignacion.CARGO) {
                    mensaje = String.format(
                            "%s %s NO ha sido designado/a como %s. pues el cargo solicitado lo ocupa %s %s para el período",
                            nuevaDesignacion.getPersona().getNombre(),
                            nuevaDesignacion.getPersona().getApellido(),
                            existente.getCargo().getNombre().toLowerCase(),
                            existente.getPersona().getNombre(),
                            existente.getPersona().getApellido());
                } else {
                    Division division = existente.getCargo().getDivision();
                    mensaje = String.format(
                            "%s %s NO ha sido designado/a debido a que la asignatura %s de la división %dº %dº turno %s lo ocupa %s %s para el período",
                            nuevaDesignacion.getPersona().getNombre(),
                            nuevaDesignacion.getPersona().getApellido(),
                            existente.getCargo().getNombre(),
                            division.getAnio(),
                            division.getNumDivision(),
                            division.getTurno(),
                            existente.getPersona().getNombre(),
                            existente.getPersona().getApellido());
                }
                throw new IllegalArgumentException(mensaje);
            }
        }
    }

    /**
     * Verifica si las licencias de una persona cubren completamente un período dado
     */
    private boolean licenciasCubrenPeriodoCompleto(Persona persona, Cargo cargo,
            LocalDate fechaInicio, LocalDate fechaFin) {
        // Obtener todas las licencias válidas de la persona en el período
        List<Licencia> licenciasEnPeriodo = licenciaService.findLicenciasEnPeriodo(
                cargo, persona, fechaInicio, fechaFin);

        if (licenciasEnPeriodo.isEmpty()) {
            return false;
        }

        // Verificar si las licencias cubren todo el período día por día
        LocalDate fechaActual = fechaInicio;

        while (!fechaActual.isAfter(fechaFin)) {
            final LocalDate fecha = fechaActual;

            boolean diaCubierto = licenciasEnPeriodo.stream()
                    .anyMatch(licencia -> !fecha.isBefore(licencia.getPedidoDesde()) &&
                            !fecha.isAfter(licencia.getPedidoHasta()));

            if (!diaCubierto) {
                return false; // Encontramos un día no cubierto
            }

            fechaActual = fechaActual.plusDays(1);
        }

        return true; // Todos los días están cubiertos
    }
}
