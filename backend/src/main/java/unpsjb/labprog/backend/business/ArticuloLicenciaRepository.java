package unpsjb.labprog.backend.business;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import unpsjb.labprog.backend.model.ArticuloLicencia;

@Repository
public interface ArticuloLicenciaRepository extends JpaRepository<ArticuloLicencia, Long> {

    Optional<ArticuloLicencia> findByArticulo(String codigo);
    
}
