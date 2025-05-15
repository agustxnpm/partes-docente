package unpsjb.labprog.backend.business;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import unpsjb.labprog.backend.model.Division;
import unpsjb.labprog.backend.model.Turno;

@Repository
public interface DivisionRepository extends JpaRepository<Division, Long> { 

    Optional<Division> findByAnioAndNumDivisionAndTurnoAndOrientacion(
    int anio,
    int numDivision,
    Turno turno,
    String orientacion
);

   Optional<Division> findByAnioAndNumDivision(
    int anio,
    int numDivision
);

}
