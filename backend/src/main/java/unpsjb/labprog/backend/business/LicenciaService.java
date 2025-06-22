package unpsjb.labprog.backend.business;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import unpsjb.labprog.backend.business.interfaces.mensajes.ILicenciaMensajeBuilder;
import unpsjb.labprog.backend.business.interfaces.servicios.IDesignacionService;
import unpsjb.labprog.backend.business.interfaces.servicios.ILicenciaService;
import unpsjb.labprog.backend.business.interfaces.validaciones.ILicenciaValidator;
import unpsjb.labprog.backend.model.Cargo;
import unpsjb.labprog.backend.model.Designacion;
import unpsjb.labprog.backend.model.EstadoLicencia;
import unpsjb.labprog.backend.model.Licencia;
import unpsjb.labprog.backend.model.LogLicencia;
import unpsjb.labprog.backend.model.Persona;

/**
 * Implementación del servicio de licencias.
 * Aplica el principio DIP (Dependency Inversion Principle) dependiendo de
 * abstracciones
 * en lugar de implementaciones concretas.
 */
@Service
public class LicenciaService implements ILicenciaService {

    @Autowired
    private LicenciaRepository licenciaRepository;

    @Autowired
    private IDesignacionService designacionService;

    @Autowired
    private LogLicenciaService logLicenciaService;

    // Aplicando DIP: Dependemos de la abstracción específica
    // ILicenciaMensajeBuilder
    @Autowired
    private ILicenciaMensajeBuilder licenciaMensajeBuilder;

    // Aplicando DIP e ISP: Dependemos de la abstracción específica
    // ILicenciaValidator
    @Autowired
    private ILicenciaValidator licenciaValidator;

    @Transactional
    public LogLicencia agregarLog(Licencia licencia, EstadoLicencia estado, String mensaje) {
        LogLicencia log = logLicenciaService.crearLog(licencia, estado, mensaje);
        licencia.getLogs().add(log);
        return log;
    }

    @Transactional
    public Licencia createLicencia(Licencia licencia) {
        asociarDesignacionesVigentes(licencia);
        return validarLicencia(licencia);
    }

    /**
     * Asocia las designaciones vigentes de la persona en el período de la licencia
     */
    private void asociarDesignacionesVigentes(Licencia licencia) {
        List<Designacion> designacionesVigentes = buscarDesignacionesVigentesParaLicencia(licencia);
        licencia.setDesignaciones(designacionesVigentes);
    }

    /**
     * Busca todas las designaciones vigentes para una licencia en su período
     */
    private List<Designacion> buscarDesignacionesVigentesParaLicencia(Licencia licencia) {
        return designacionService.findAllByPersonaAndPeriodoVigente(
                licencia.getPersona(),
                licencia.getPedidoDesde(),
                licencia.getPedidoHasta());
    }

    private Licencia validarLicencia(Licencia licencia) {

        try {
            licenciaValidator.validarLicencia(licencia);

            licencia.setEstado(EstadoLicencia.VALIDA);
            licenciaRepository.save(licencia);
            agregarLog(licencia, EstadoLicencia.VALIDA,
                    licenciaMensajeBuilder.generarMensajeExitoLicenciaOtorgada(licencia));
            return licencia;
        } catch (IllegalArgumentException e) {
            licencia.setEstado(EstadoLicencia.INVALIDA);
            licenciaRepository.save(licencia);
            agregarLog(licencia, EstadoLicencia.INVALIDA, e.getMessage());
            return licencia;
        } catch (IllegalStateException e) {
            // Error de configuración del sistema de validaciones
            licencia.setEstado(EstadoLicencia.INVALIDA);
            licenciaRepository.save(licencia);
            agregarLog(licencia, EstadoLicencia.INVALIDA, "Error en la configuración del sistema de validaciones: " + e.getMessage());
            return licencia;
        }
    }

    @Transactional
    public Licencia updateLicencia(Long id, Licencia licenciaActualizar) {
        Licencia licenciaExistente = buscarLicenciaExistente(id);
        actualizarCamposLicencia(licenciaExistente, licenciaActualizar);
        asociarDesignacionesVigentes(licenciaExistente);
        return validarLicencia(licenciaExistente);
    }

    /**
     * Busca una licencia existente por ID
     */
    private Licencia buscarLicenciaExistente(Long id) {
        return licenciaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Licencia con ID " + id + " no encontrada."));
    }

    /**
     * Actualiza los campos de una licencia existente con los valores de otra
     * licencia
     */
    private void actualizarCamposLicencia(Licencia licenciaExistente, Licencia licenciaActualizar) {
        licenciaExistente.setPedidoDesde(licenciaActualizar.getPedidoDesde());
        licenciaExistente.setPedidoHasta(licenciaActualizar.getPedidoHasta());
        licenciaExistente.setDomicilio(licenciaActualizar.getDomicilio());
        licenciaExistente.setCertificadoMedico(licenciaActualizar.isCertificadoMedico());
        licenciaExistente.setArticuloLicencia(licenciaActualizar.getArticuloLicencia());
    }

    public List<Licencia> findLicenciasEnPeriodo(Cargo cargo, Persona persona, LocalDate fechaInicio,
            LocalDate fechaFin) {
        return licenciaRepository.findLicenciasEnPeriodo(cargo, persona, fechaInicio, fechaFin);
    }

    public Page<Licencia> findByPage(int page, int size) {
        return licenciaRepository.findAll(
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
    }

    public List<Licencia> buscarLicenciasPorDni(Long personaDni, String codigoArticulo, LocalDate desde,
            LocalDate hasta) {
        return licenciaRepository.findByPersonaDniArticuloYFechas(personaDni, codigoArticulo, desde, hasta);
    }

    public Licencia findById(Long id) {
        return licenciaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Licencia con ID " + id + " no encontrada."));
    }

    public List<Licencia> findByEstado(EstadoLicencia estado) {
        return licenciaRepository.findByEstado(estado);
    }

    public List<Licencia> findByPersona(Persona persona) {
        return licenciaRepository.findByPersona(persona);
    }

    public List<Licencia> getAllLicencias() {
        return licenciaRepository.findAll();
    }

    public List<Licencia> findLicenciasQueCubrenPeriodoCompleto(Cargo cargo, Persona persona, LocalDate fechaInicio,
            LocalDate fechaFin) {
        return licenciaRepository.findLicenciasQueCubrenPeriodoCompleto(cargo, persona, fechaInicio, fechaFin);
    }

    public List<Licencia> findByPersonaAndYear(Persona persona, int year) {
        return licenciaRepository.findByPersonaAndYear(persona, year);
    }

    public List<Licencia> findLicenciasActivasEnFecha(LocalDate fecha) {
        return licenciaRepository.findLicenciasActivasEnFecha(fecha);
    }

    public boolean licenciasCubrenPeriodoCompleto(Persona persona, Cargo cargo,
            LocalDate fechaInicio, LocalDate fechaFin) {
        List<Licencia> licenciasEnPeriodo = obtenerLicenciasValidasEnPeriodo(persona, cargo, fechaInicio, fechaFin);

        if (licenciasEnPeriodo.isEmpty()) {
            return false;
        }

        return verificarCoberturaDiaria(licenciasEnPeriodo, fechaInicio, fechaFin);
    }

    /**
     * Obtiene todas las licencias válidas de una persona en un período específico
     */
    private List<Licencia> obtenerLicenciasValidasEnPeriodo(Persona persona, Cargo cargo,
            LocalDate fechaInicio, LocalDate fechaFin) {
        return findLicenciasEnPeriodo(cargo, persona, fechaInicio, fechaFin);
    }

    /**
     * Verifica si las licencias cubren todos los días del período especificado
     */
    private boolean verificarCoberturaDiaria(List<Licencia> licencias, LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDate fechaActual = fechaInicio;

        while (!fechaActual.isAfter(fechaFin)) {
            if (!fechaEstaCubiertaPorLicencias(fechaActual, licencias)) {
                return false; // Encontramos un día no cubierto
            }
            fechaActual = fechaActual.plusDays(1);
        }

        return true; // Todos los días están cubiertos
    }

    /**
     * Verifica si una fecha específica está cubierta por al menos una licencia
     */
    private boolean fechaEstaCubiertaPorLicencias(LocalDate fecha, List<Licencia> licencias) {
        return licencias.stream()
                .anyMatch(licencia -> fechaEstaEnRangoLicencia(fecha, licencia));
    }

    /**
     * Verifica si una fecha está dentro del rango de una licencia
     */
    private boolean fechaEstaEnRangoLicencia(LocalDate fecha, Licencia licencia) {
        return !fecha.isBefore(licencia.getPedidoDesde()) &&
                !fecha.isAfter(licencia.getPedidoHasta());
    }

}
