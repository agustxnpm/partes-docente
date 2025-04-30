package unpsjb.labprog.backend.business;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import unpsjb.labprog.backend.model.Division;
import unpsjb.labprog.backend.model.Turno;

public interface DivisionRepository extends JpaRepository<Division, Long> { 

    Optional<Division> findByAnioAndNumDivisionAndOrientacionAndTurno(
        int anio, int numDivision, String orientacion, Turno turno
    );
}
