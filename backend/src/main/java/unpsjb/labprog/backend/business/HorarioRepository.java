package unpsjb.labprog.backend.business;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import unpsjb.labprog.backend.model.Horario;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, Long> {

    Optional<Horario> findByDiaAndHora(String dia, int hora);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END FROM Cargo c JOIN c.horario h WHERE h.id = :horarioId")
    boolean isHorarioAsignadoACargo(@Param("horarioId") Long horarioId);
}
