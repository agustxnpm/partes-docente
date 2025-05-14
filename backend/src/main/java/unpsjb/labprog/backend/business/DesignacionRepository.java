package unpsjb.labprog.backend.business;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import unpsjb.labprog.backend.model.Designacion;

@Repository
public interface DesignacionRepository extends JpaRepository<Designacion, Long> {

    
}
