package unpsjb.labprog.backend.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import unpsjb.labprog.backend.business.interfaces.mensajes.IDivisionMensajeBuilder;
import unpsjb.labprog.backend.business.interfaces.servicios.IDivisionService;
import unpsjb.labprog.backend.business.interfaces.validaciones.IDivisionValidator;
import unpsjb.labprog.backend.model.Division;

/**
 * Implementación del servicio de divisiones.
 * Aplica el principio DIP (Dependency Inversion Principle) dependiendo de abstracciones
 * en lugar de implementaciones concretas.
 */
@Service
public class DivisionService implements IDivisionService {

    @Autowired
    private DivisionRepository divisionRepository;

    // Aplicando DIP: Dependemos de la abstracción específica IDivisionMensajeBuilder
    @Autowired
    private IDivisionMensajeBuilder divisionMensajeBuilder;

    // Aplicando DIP e ISP: Dependemos de la abstracción específica IDivisionValidator
    @Autowired
    private IDivisionValidator divisionValidator;

    @Transactional
    public Division save(Division division) {
        divisionValidator.validarDivision(division);
        return divisionRepository.save(division);
    }

    @Transactional
    public void delete(Division division) {
        divisionValidator.validarBorradoDivision(division);
        divisionRepository.delete(division);
    }

    public Page<Division> findByPage(int page, int size) {
        return divisionRepository.findAll(
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
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
        return divisionMensajeBuilder.generarMensajeExitoCreacion(division);
    }

    public String getMensajeExitoActualizacion(Division division) {
        return divisionMensajeBuilder.generarMensajeExitoActualizacion(division);
    }

    public String getMensajeExitoBorrado(Division division) {
        return divisionMensajeBuilder.generarMensajeExitoBorrado(division);
    }

    public Division buscarDivisionExistente(Division division) {
        return divisionRepository
                .findByAnioAndNumDivisionAndTurnoAndOrientacion(
                        division.getAnio(),
                        division.getNumDivision(),
                        division.getTurno(),
                        division.getOrientacion())
                .orElse(null);
    }

    public Division buscarDivisionExistente(
            int anio,
            int numDivision) {
        return divisionRepository
                .findByAnioAndNumDivision(anio, numDivision).orElse(null);
    }
}
