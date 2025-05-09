package unpsjb.labprog.backend.business;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import unpsjb.labprog.backend.model.Cargo;
import unpsjb.labprog.backend.model.Division;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, Long> {
  
    Optional<Cargo> findByNombre(String nombre);
    Optional<Cargo> findByNombreAndDivision(String nombre, Division division);

}
