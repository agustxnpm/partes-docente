package unpsjb.labprog.backend.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import unpsjb.labprog.backend.model.Division;

@Service
public class DivisionService {
    
    @Autowired
    private DivisionRepository divisionRepository;

    public Division save(Division division) {
        if (exists(division)) {
            throw new IllegalArgumentException("Ya existe la division: " + division.getAnio() + "º" + division.getNumDivision());  
        }

        return divisionRepository.save(division);
    }

    // validador de divisiones
    private boolean exists(Division division) {
        return divisionRepository.findByAnioAndNumDivisionAndOrientacionAndTurno(
            division.getAnio(), 
            division.getNumDivision(), 
            division.getOrientacion(), 
            division.getTurno()
        ).isPresent();
    }

}
