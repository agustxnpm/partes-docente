package unpsjb.labprog.backend.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import unpsjb.labprog.backend.model.Division;

@Service
public class DivisionService {
    
    @Autowired
    private DivisionRepository divisionRepository;

    @Autowired
    private MensajeBuilder mensajeBuilder;

    public Division save(Division division) {

        return divisionRepository.save(division);
    }

    public String getMensajeExito(Division division) {
        return mensajeBuilder.generarMensajeExitoDivisionCreada(division);
    }

    public String getMensajeDivisionDuplicada(Division division) {
        return mensajeBuilder.generarMensajeDivisionDuplicada(division);
    }

    public Division buscarDivisionExistente(Division division) {
        return divisionRepository
            .findByAnioAndNumDivisionAndTurnoAndOrientacion(
                division.getAnio(),
                division.getNumDivision(),
                division.getTurno(),
                division.getOrientacion()
            ).orElse(null);
    }
}
