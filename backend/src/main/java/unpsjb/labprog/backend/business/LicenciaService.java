package unpsjb.labprog.backend.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import unpsjb.labprog.backend.business.utilidades.MensajeBuilder;
import unpsjb.labprog.backend.business.validaciones.Validator;
import unpsjb.labprog.backend.model.Licencia;

@Service
public class LicenciaService {

    @Autowired
    private LicenciaRepository licenciaRepository;

    @Autowired
    private MensajeBuilder mensajeBuilder;

    @Autowired
    private Validator validator;

    @Transactional
    public Licencia createLicencia(Licencia licencia) {
        validator.validarLicencia(licencia);
        return licenciaRepository.save(licencia);
    }
}
