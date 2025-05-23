package unpsjb.labprog.backend.business;

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

    public List<Licencia> getAllLicencias() {
        return licenciaRepository.findAll();
    }

    public String getMensajeExitoLicenciaOtorgada(Licencia licencia) {
        return mensajeBuilder.generarMensajeExitoLicenciaOtorgada(licencia);
    }

}
