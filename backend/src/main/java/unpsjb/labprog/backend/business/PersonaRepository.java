package unpsjb.labprog.backend.business;

import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import unpsjb.labprog.backend.model.Persona;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {

    Optional<Persona> findByDni(long dni);
    Optional<Persona> findByNombre(String nombre);
    void deleteByDni(long dni);
}
 
    
