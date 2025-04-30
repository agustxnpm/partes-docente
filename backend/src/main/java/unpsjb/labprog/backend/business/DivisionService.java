package unpsjb.labprog.backend.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import unpsjb.labprog.backend.model.Division;

@Service
public class DivisionService {
    
    @Autowired
    private DivisionRepository divisionRepository;

    public Division save(Division division) {

        return divisionRepository.save(division);
    }

}
