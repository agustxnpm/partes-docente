package unpsjb.labprog.backend.business;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import unpsjb.labprog.backend.model.Division;

@Repository
public interface DivisionRepository extends JpaRepository<Division, Long> { 

}
