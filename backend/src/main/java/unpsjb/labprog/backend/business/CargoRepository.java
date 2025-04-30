package unpsjb.labprog.backend.business;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import unpsjb.labprog.backend.model.Cargo;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, Long> {
  
}
