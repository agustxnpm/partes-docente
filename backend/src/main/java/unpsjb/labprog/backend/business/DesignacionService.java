package unpsjb.labprog.backend.business;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import unpsjb.labprog.backend.business.interfaces.IDesignacionService;
import unpsjb.labprog.backend.business.interfaces.ILicenciaService;
import unpsjb.labprog.backend.business.interfaces.IDesignacionValidator;
import unpsjb.labprog.backend.business.utilidades.MensajeBuilder;
import unpsjb.labprog.backend.model.Cargo;
import unpsjb.labprog.backend.model.Designacion;
import unpsjb.labprog.backend.model.Licencia;
import unpsjb.labprog.backend.model.Persona;

/**
 * Implementación del servicio de designaciones.
 * Aplica el principio DIP (Dependency Inversion Principle) dependiendo de abstracciones
 * en lugar de implementaciones concretas.
 */
@Service
public class DesignacionService implements IDesignacionService {

    @Autowired
    private DesignacionRepository designacionRepository;

    @Autowired
    private MensajeBuilder mensajeBuilder;

    // Aplicando DIP e ISP: Dependemos de la abstracción específica IDesignacionValidator
    @Autowired
    @Lazy
    private IDesignacionValidator designacionValidator;

    // Aplicando DIP: Dependemos de la abstracción ILicenciaService, no de la implementación concreta
    @Autowired
    @Lazy
    private ILicenciaService licenciaService;

    @Transactional
    public Designacion save(Designacion designacion) {
        designacionValidator.validarDesignacion(designacion);
        return designacionRepository.save(designacion);

    }

    @Transactional
    public void delete(Designacion designacion) {
        designacionRepository.delete(designacion);
    }

    public List<Designacion> findAll() {
        return designacionRepository.findAll();
    }

    public Designacion findById(Long id) {
        return designacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Designacion no encontrada"));
    }

    public List<Designacion> findDesignacionesSuperpuestas(Long cargoId, LocalDate fechaInicio, LocalDate fechaFin,
            Long designacionId) {
        return designacionRepository.findDesignacionesSuperpuestas(cargoId, fechaInicio, fechaFin, designacionId);
    }

    public Page<Designacion> findByPage(int page, int size) {
        return designacionRepository.findAll(
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
    }

    public List<Designacion> findSuplenciasEnPeriodo(Long cargoId, LocalDate fechaInicio, LocalDate fechaFin,
            Persona personaTitular, Long designacionId) {
        return designacionRepository.findSuplenciasEnPeriodo(cargoId, fechaInicio, fechaFin, personaTitular,
                designacionId);
    }

    public String getMensajeExitoActualizacion(Designacion designacion) {
        return mensajeBuilder.generarMensajeExitoDesignacionActualizada(designacion);
    }

    public String getMensajeExitoBorrado(Designacion designacion) {
        return mensajeBuilder.generarMensajeExitoDesignacionBorrada(designacion);
    }

    public String getMensajeExito(Designacion designacion) {
        return mensajeBuilder.generarMensajeExitoDesignacionCreada(designacion);
    }

    public String getMensajeExitoDesignacionSuplencia(Designacion designacionSuplantada, Persona personaSuplantada) {
        return mensajeBuilder.generarMensajeExitoDesignacionSuplencia(designacionSuplantada, personaSuplantada);
    }

    /**
     * Determina el mensaje de éxito apropiado para una designación guardada
     */
    public String determinarMensajeExito(Designacion designacionGuardada) {
        if ("Suplente".equalsIgnoreCase(designacionGuardada.getSituacionRevista())) {
            Persona personaReemplazada = encontrarPersonaReemplazada(designacionGuardada);

            if (personaReemplazada != null) {
                return getMensajeExitoDesignacionSuplencia(designacionGuardada, personaReemplazada);
            } else {
                return getMensajeExito(designacionGuardada);
            }
        } else {
            return getMensajeExito(designacionGuardada);
        }
    }

    /**
     * Encuentra la persona que está siendo reemplazada por una suplencia,
     * basándose en las licencias que cubran el período
     */
    public Persona encontrarPersonaReemplazada(Designacion designacionGuardada) {

        List<Designacion> posiblesOriginales = findDesignacionesSuperpuestas(
                designacionGuardada.getCargo().getId(),
                designacionGuardada.getFechaInicio(),
                designacionGuardada.getFechaFin(),
                designacionGuardada.getId());

        for (Designacion designacionExistente : posiblesOriginales) {
            LocalDate fechaFinNueva = designacionGuardada.getFechaFin() != null ? designacionGuardada.getFechaFin()
                    : LocalDate.now().plusYears(100);

            // Usar el mismo método que usa el validator para verificar cobertura completa
            if (licenciasCubrenPeriodoCompleto(
                    designacionExistente.getPersona(),
                    designacionGuardada.getCargo(),
                    designacionGuardada.getFechaInicio(),
                    fechaFinNueva)) {
                return designacionExistente.getPersona();
            }
        }

        return null; // No se encontró persona reemplazada
    }


    /**
     * Verifica si las licencias de una persona cubren completamente un período dado
     * (Mismo método que está en DesignacionValidator)
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


    public List<Designacion> findAllByPersonaAndPeriodoVigente(Persona persona, LocalDate pedidoDesde,
            LocalDate pedidoHasta) {
        return designacionRepository.findAllByPersonaAndPeriodoVigente(persona, pedidoDesde, pedidoHasta);
    }

}
