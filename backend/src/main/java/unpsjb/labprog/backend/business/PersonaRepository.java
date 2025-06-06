package unpsjb.labprog.backend.business;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import unpsjb.labprog.backend.model.Persona;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {

    Optional<Persona> findByDni(long dni);

    Optional<Persona> findByCuil(String cuil);

    Optional<Persona> findByNombre(String nombre);

    void deleteById(long id);

    @Query("SELECT p FROM Persona p WHERE " +
            "LOWER(p.nombre) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(p.apellido) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "CAST(p.dni AS string) LIKE CONCAT('%', :term, '%')")
    List<Persona> search(@Param("term") String term);
}
