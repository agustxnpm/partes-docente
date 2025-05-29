package unpsjb.labprog.backend.business;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import unpsjb.labprog.backend.business.utilidades.MensajeBuilder;
import unpsjb.labprog.backend.business.validaciones.Validator;
import unpsjb.labprog.backend.model.Designacion;
import unpsjb.labprog.backend.model.Licencia;
import unpsjb.labprog.backend.model.Persona;

@Service
public class DesignacionService {

    @Autowired
    private DesignacionRepository designacionRepository;

    @Autowired
    private MensajeBuilder mensajeBuilder;

    @Autowired
    private Validator validator;

    @Autowired
    private LicenciaService licenciaService;

    @Transactional
    public Designacion save(Designacion designacion) {
        validator.validarDesignacion(designacion);
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
     * basándose en la licencia más específica que cubra el período
     */
    public Persona encontrarPersonaReemplazada(Designacion designacionGuardada) {
        
        List<Designacion> posiblesOriginales = findDesignacionesSuperpuestas(
                designacionGuardada.getCargo().getId(),
                designacionGuardada.getFechaInicio(),
                designacionGuardada.getFechaFin(),
                designacionGuardada.getId());

        Persona personaReemplazada = null;
        Licencia licenciaMasEspecifica = null;

        for (Designacion designacionExistente : posiblesOriginales) {
            // Verificar si esta persona tiene licencia que cubra el período de la suplencia
            List<Licencia> licenciasQueCubren = licenciaService.findLicenciasQueCubrenPeriodoCompleto(
                    designacionGuardada.getCargo(),
                    designacionExistente.getPersona(),
                    designacionGuardada.getFechaInicio(),
                    designacionGuardada.getFechaFin() != null ? designacionGuardada.getFechaFin()
                            : LocalDate.now().plusYears(100));

            if (!licenciasQueCubren.isEmpty()) {
                // Encontrar la licencia más corta (la ultima) de esta persona
                Licencia licenciaMasCorta = licenciasQueCubren.stream()
                        .min((l1, l2) -> {
                            long duracion1 = java.time.temporal.ChronoUnit.DAYS.between(l1.getPedidoDesde(),
                                    l1.getPedidoHasta()) + 1;
                            long duracion2 = java.time.temporal.ChronoUnit.DAYS.between(l2.getPedidoDesde(),
                                    l2.getPedidoHasta()) + 1;
                            return Long.compare(duracion1, duracion2);
                        }).orElse(null);

                if (licenciaMasEspecifica == null ||
                        (licenciaMasCorta != null &&
                                java.time.temporal.ChronoUnit.DAYS.between(licenciaMasCorta.getPedidoDesde(),
                                        licenciaMasCorta.getPedidoHasta()) < java.time.temporal.ChronoUnit.DAYS
                                                .between(licenciaMasEspecifica.getPedidoDesde(),
                                                        licenciaMasEspecifica.getPedidoHasta()))) {
                    licenciaMasEspecifica = licenciaMasCorta;
                    personaReemplazada = designacionExistente.getPersona();
                }
            }
        }

        return personaReemplazada;
    }

}
