package unpsjb.labprog.backend.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import unpsjb.labprog.backend.model.Division;

@Service
public class DivisionService {
    
    @Autowired
    private DivisionRepository divisionRepository;

    @Autowired
    private MensajeBuilder mensajeBuilder;

    @Autowired
    private Validator validator;


    @Transactional
    public Division save(Division division) {
        validator.validarDivision(division);
        return divisionRepository.save(division);
    }

    @Transactional
    public void delete(Division division) {
        divisionRepository.delete(division);
    }

    public Page<Division> findByPage(int page, int size) {
        return divisionRepository.findAll(
            PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"))
        );
    }

      public List<Division> findAll() {
        List<Division> result = new ArrayList<>();
        divisionRepository.findAll().forEach(e -> result.add(e));
        return result;
    }

    public Division findById(Long id) {
        return divisionRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Division no encontrada"));
    }

    public String getMensajeExito(Division division) {
        return mensajeBuilder.generarMensajeExitoDivisionCreada(division);
    }

    public String getMensajeDivisionDuplicada(Division division) {
        return mensajeBuilder.generarMensajeDivisionDuplicada(division);
    }

    public String getMensajeExitoActualizacion(Division division) {
        return mensajeBuilder.generarMensajeExitoDivisionActualizada(division);
    }

    public String getMensajeExitoBorrado(Division division) {
        return mensajeBuilder.generarMensajeExitoDivisionBorrada(division);
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
