package unpsjb.labprog.backend.business;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import unpsjb.labprog.backend.model.Licencia;
import unpsjb.labprog.backend.model.Persona;

@Repository
public interface LicenciaRepository extends JpaRepository<Licencia, Long> {

    /**
     * Busca todas las licencias de una persona en un año específico.
     * El año se determina por la fecha de inicio de la licencia (pedidoDesde).
     */
    @Query("SELECT l FROM Licencia l WHERE l.persona = :persona AND FUNCTION('YEAR', l.pedidoDesde) = :year")
    List<Licencia> findByPersonaAndYear(@Param("persona") Persona persona, @Param("year") int year);
}
