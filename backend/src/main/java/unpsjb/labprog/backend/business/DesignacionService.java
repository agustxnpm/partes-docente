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
import unpsjb.labprog.backend.business.interfaces.servicios.IDesignacionService;
import unpsjb.labprog.backend.business.interfaces.servicios.ILicenciaService;
import unpsjb.labprog.backend.business.interfaces.validaciones.IDesignacionValidator;
import unpsjb.labprog.backend.model.Designacion;
import unpsjb.labprog.backend.model.Persona;

/**
 * Implementación del servicio de designaciones.
 * Aplica el principio DIP (Dependency Inversion Principle) dependiendo de abstracciones
 * en lugar de implementaciones concretas.
 * 
 * Nota: Se removieron los métodos de generación de mensajes para eliminar la indirección
 * innecesaria. Los mensajes ahora se generan directamente en el presenter usando
 * IDesignacionMensajeBuilder, aplicando mejor el principio SRP.
 */
@Service
public class DesignacionService implements IDesignacionService {

    @Autowired
    private DesignacionRepository designacionRepository;

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

    /**
     * Encuentra la persona que está siendo reemplazada por una suplencia,
     * basándose en las licencias que cubran el período
     */
    public Persona encontrarPersonaReemplazada(Designacion designacionGuardada) {
        List<Designacion> posiblesOriginales = obtenerDesignacionesSuperpuestas(designacionGuardada);
        return buscarPersonaConLicenciasCompletas(posiblesOriginales, designacionGuardada);
    }

    /**
     * Obtiene las designaciones que se superponen con la designación guardada
     */
    private List<Designacion> obtenerDesignacionesSuperpuestas(Designacion designacionGuardada) {
        return findDesignacionesSuperpuestas(
                designacionGuardada.getCargo().getId(),
                designacionGuardada.getFechaInicio(),
                designacionGuardada.getFechaFin(),
                designacionGuardada.getId());
    }

    /**
     * Busca entre las designaciones existentes aquella persona que tiene licencias que cubren todo el período
     */
    private Persona buscarPersonaConLicenciasCompletas(List<Designacion> designacionesExistentes, 
                                                      Designacion designacionNueva) {
        LocalDate fechaFinNueva = calcularFechaFinEfectiva(designacionNueva);

        for (Designacion designacionExistente : designacionesExistentes) {
            if (personaTieneLicenciasCompletas(designacionExistente, designacionNueva, fechaFinNueva)) {
                return designacionExistente.getPersona();
            }
        }

        return null; // No se encontró persona reemplazada
    }

    /**
     * Calcula la fecha fin efectiva de la designación (maneja null)
     */
    private LocalDate calcularFechaFinEfectiva(Designacion designacion) {
        return designacion.getFechaFin() != null 
                ? designacion.getFechaFin() 
                : LocalDate.now().plusYears(100);
    }

    /**
     * Verifica si una persona tiene licencias que cubren completamente el período de suplencia
     */
    private boolean personaTieneLicenciasCompletas(Designacion designacionExistente, 
                                                  Designacion designacionNueva, 
                                                  LocalDate fechaFinNueva) {
        return licenciaService.licenciasCubrenPeriodoCompleto(
                designacionExistente.getPersona(),
                designacionNueva.getCargo(),
                designacionNueva.getFechaInicio(),
                fechaFinNueva);
    }

    public List<Designacion> findAllByPersonaAndPeriodoVigente(Persona persona, LocalDate pedidoDesde,
            LocalDate pedidoHasta) {
        return designacionRepository.findAllByPersonaAndPeriodoVigente(persona, pedidoDesde, pedidoHasta);
    }

}
