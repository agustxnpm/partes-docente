package unpsjb.labprog.backend.business;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import unpsjb.labprog.backend.model.Cargo;
import unpsjb.labprog.backend.model.Division;
import unpsjb.labprog.backend.model.Turno;

@Repository
public interface  CargoRepository extends JpaRepository<Cargo, Long> {

    Optional<Cargo> findByNombre(String nombre);

    Optional<Cargo> findByNombreAndDivision(String nombre, Division division);

    @Query("SELECT c FROM Cargo c WHERE c.nombre = :nombre " +
            "AND c.division.anio = :anio " +
            "AND c.division.numDivision = :numDivision " +
            "AND c.division.turno = :turno")
    Optional<Cargo> findByNombreAndDivisionDetalle(
            @Param("nombre") String nombre,
            @Param("anio") int anio,
            @Param("numDivision") int numDivision,
            @Param("turno") Turno turno);

    boolean existsByDivisionId(Long divisionId);

    @Query("SELECT c FROM Cargo c LEFT JOIN c.division d WHERE " +
            "LOWER(c.nombre) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "CAST(d.anio AS string) LIKE CONCAT('%', :term, '%') OR " +
            "CAST(d.numDivision AS string) LIKE CONCAT('%', :term, '%') OR " +
            "LOWER(d.turno) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(d.orientacion) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<Cargo> search(@Param("term") String term);
}
