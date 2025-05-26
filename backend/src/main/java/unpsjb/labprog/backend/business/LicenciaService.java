package unpsjb.labprog.backend.business;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import unpsjb.labprog.backend.business.utilidades.MensajeBuilder;
import unpsjb.labprog.backend.business.validaciones.Validator;
import unpsjb.labprog.backend.model.Designacion;
import unpsjb.labprog.backend.model.Licencia;

@Service
public class LicenciaService {

    @Autowired
    private LicenciaRepository licenciaRepository;

    @Autowired
    private DesignacionRepository designacionRepository;

    @Autowired
    private MensajeBuilder mensajeBuilder;

    @Autowired
    private Validator validator;

    @Transactional
    public Licencia createLicencia(Licencia licencia) {

        // Buscar todas las designaciones vigentes para la persona en el período de la
        // licencia
        List<Designacion> designacionesVigentes = designacionRepository.findAllByPersonaAndPeriodoVigente(
                licencia.getPersona(),
                licencia.getPedidoDesde(),
                licencia.getPedidoHasta());

        // Asociar estas designaciones a la licencia
        licencia.setDesignaciones(designacionesVigentes);

        validator.validarLicencia(licencia);
        return licenciaRepository.save(licencia);
    }

    @Transactional
    public Licencia updateLicencia(Long id, Licencia licenciaActualizar) {
        // 1. Verificar si la licencia existe
        Licencia licenciaExistente = licenciaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Licencia con ID " + id + " no encontrada."));

        // 2. Actualizar los campos de licenciaExistente con los de licenciaActualizar
        // Evita cambiar el ID.
        licenciaExistente.setPedidoDesde(licenciaActualizar.getPedidoDesde());
        licenciaExistente.setPedidoHasta(licenciaActualizar.getPedidoHasta());
        licenciaExistente.setDomicilio(licenciaActualizar.getDomicilio());
        licenciaExistente.setCertificadoMedico(licenciaActualizar.isCertificadoMedico());
        licenciaExistente.setArticuloLicencia(licenciaActualizar.getArticuloLicencia());
        licenciaExistente.setPersona(licenciaActualizar.getPersona()); // Cuidado si la persona no debería cambiar

        // Re-asociar designaciones si es necesario y validar
        List<Designacion> designacionesVigentes = designacionRepository.findAllByPersonaAndPeriodoVigente(
                licenciaExistente.getPersona(), // Usar la persona de la licencia existente
                licenciaExistente.getPedidoDesde(),
                licenciaExistente.getPedidoHasta());
        licenciaExistente.setDesignaciones(designacionesVigentes);

        // 3. Validar la licencia actualizada (excluyendo la validación de superposición
        // consigo misma si es necesario)
        validator.validarLicencia(licenciaExistente);

        // 4. Guardar
        return licenciaRepository.save(licenciaExistente);
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
    public List<Licencia> buscarLicenciasPorDni(Long personaDni, String codigoArticulo, LocalDate desde,
            LocalDate hasta) {
        return licenciaRepository.findByPersonaDniArticuloYFechas(personaDni, codigoArticulo, desde, hasta);
    }
}
