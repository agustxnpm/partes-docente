package unpsjb.labprog.backend.business;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import unpsjb.labprog.backend.business.interfaces.IDesignacionService;
import unpsjb.labprog.backend.business.interfaces.ILicenciaService;
import unpsjb.labprog.backend.business.interfaces.ILicenciaValidator;
import unpsjb.labprog.backend.business.utilidades.MensajeBuilder;
import unpsjb.labprog.backend.model.Cargo;
import unpsjb.labprog.backend.model.Designacion;
import unpsjb.labprog.backend.model.EstadoLicencia;
import unpsjb.labprog.backend.model.Licencia;
import unpsjb.labprog.backend.model.LogLicencia;
import unpsjb.labprog.backend.model.Persona;

/**
 * Implementación del servicio de licencias.
 * Aplica el principio DIP (Dependency Inversion Principle) dependiendo de abstracciones
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

    @Autowired
    private MensajeBuilder mensajeBuilder;

    // Aplicando DIP e ISP: Dependemos de la abstracción específica ILicenciaValidator
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

        // Buscar todas las designaciones vigentes para la persona en el período de la
        // licencia
        List<Designacion> designacionesVigentes = designacionService.findAllByPersonaAndPeriodoVigente(
                licencia.getPersona(),
                licencia.getPedidoDesde(),
                licencia.getPedidoHasta());

        // Asociar estas designaciones a la licencia
        licencia.setDesignaciones(designacionesVigentes);

        return validarLicencia(licencia);

    }

    private Licencia validarLicencia(Licencia licencia) {

        try {
            licenciaValidator.validarLicencia(licencia);

            licencia.setEstado(EstadoLicencia.VALIDA);
            licenciaRepository.save(licencia);
            agregarLog(licencia, EstadoLicencia.VALIDA, getMensajeExitoLicenciaOtorgada(licencia));
            return licencia;
        } catch (IllegalArgumentException e) {
            licencia.setEstado(EstadoLicencia.INVALIDA);
            licenciaRepository.save(licencia);
            agregarLog(licencia, EstadoLicencia.INVALIDA, e.getMessage());
            return licencia;
        }
    }

    @Transactional
    public Licencia updateLicencia(Long id, Licencia licenciaActualizar) {
        // 1. Verificar si la licencia existe
        Licencia licenciaExistente = licenciaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Licencia con ID " + id + " no encontrada."));

        // 2. Actualizar los campos de licenciaExistente con los de licenciaActualizar
        licenciaExistente.setPedidoDesde(licenciaActualizar.getPedidoDesde());
        licenciaExistente.setPedidoHasta(licenciaActualizar.getPedidoHasta());
        licenciaExistente.setDomicilio(licenciaActualizar.getDomicilio());
        licenciaExistente.setCertificadoMedico(licenciaActualizar.isCertificadoMedico());
        licenciaExistente.setArticuloLicencia(licenciaActualizar.getArticuloLicencia());

        // Re-asociar designaciones si es necesario y validar
        List<Designacion> designacionesVigentes = designacionService.findAllByPersonaAndPeriodoVigente(
                licenciaExistente.getPersona(), // Usar la persona de la licencia existente
                licenciaExistente.getPedidoDesde(),
                licenciaExistente.getPedidoHasta());
        licenciaExistente.setDesignaciones(designacionesVigentes);

        return validarLicencia(licenciaExistente);

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

    public String getMensajeExitoLicenciaOtorgada(Licencia licencia) {
        return mensajeBuilder.generarMensajeExitoLicenciaOtorgada(licencia);
    }

    public String getMensajeExitoLicenciaActualizada(Licencia licencia) {
        return mensajeBuilder.generarMensajeExitoLicenciaActualizada(licencia);
    }

    public List<Licencia> findLicenciasQueCubrenPeriodoCompleto(Cargo cargo, Persona persona, LocalDate fechaInicio,
            LocalDate fechaFin) {
        return licenciaRepository.findLicenciasQueCubrenPeriodoCompleto(cargo, persona, fechaInicio, fechaFin);
    }

    public List<Licencia> findByPersonaAndYear(Persona persona, int year) {
        return licenciaRepository.findByPersonaAndYear(persona, year);
    }

}
