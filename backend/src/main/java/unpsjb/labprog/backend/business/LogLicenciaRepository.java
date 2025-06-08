package unpsjb.labprog.backend.business;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import unpsjb.labprog.backend.model.LogLicencia;

@Repository
public interface LogLicenciaRepository extends JpaRepository<LogLicencia, Long> {
    
    @Query("SELECT l FROM LogLicencia l WHERE l.licencia.id = :licenciaId ORDER BY l.FechaLog DESC, l.id DESC")
    List<LogLicencia> findByLicenciaIdOrderByFechaLogDesc(@Param("licenciaId") Long licenciaId);
}