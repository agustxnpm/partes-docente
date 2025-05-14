package unpsjb.labprog.backend.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import unpsjb.labprog.backend.model.Designacion;

@Service
public class DesignacionService {
    
    @Autowired
    private DesignacionRepository designacionRepository;

    @Autowired
    private MensajeBuilder mensajeBuilder;
    
    @Autowired
    private Validator validator;




    public Designacion save(Designacion designacion) {
        validator.validarDesignacion(designacion);
        return designacionRepository.save(designacion);
    }










    public String getMensajeExito(Designacion designacion) {
        return mensajeBuilder.generarMensajeExitoDesignacionCreada(designacion);
    }

}
