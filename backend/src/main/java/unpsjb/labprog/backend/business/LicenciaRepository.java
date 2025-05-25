package unpsjb.labprog.backend.business;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import unpsjb.labprog.backend.model.Designacion;
import unpsjb.labprog.backend.model.Licencia;
import unpsjb.labprog.backend.model.Persona;

@Repository
public interface LicenciaRepository extends JpaRepository<Licencia, Long> {

    /**
     * Busca todas las licencias de una persona en un año específico.
     * El año se determina por la fecha de inicio de la licencia (pedidoDesde).
     */
    @Query("SELECT l FROM Licencia l WHERE l.persona = :persona AND EXTRACT(YEAR FROM l.pedidoDesde) = :year")
    List<Licencia> findByPersonaAndYear(@Param("persona") Persona persona, @Param("year") int year);

    @Query("SELECT l FROM Licencia l WHERE l.persona.dni = :personaDni " +
            "AND l.articuloLicencia.articulo = :codigoArticulo " +
            "AND l.pedidoDesde = :desde " +
            "AND l.pedidoHasta = :hasta")
    List<Licencia> findByPersonaDniArticuloYFechas(
            @Param("personaDni") Long personaDni,
            @Param("codigoArticulo") String codigoArticulo,
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta);

    @Query("SELECT l FROM Licencia l JOIN l.designaciones d " +
            "WHERE d = :designacionEspecifica " +
            "AND l.persona = :personaConLicencia " +
            "AND l.pedidoDesde <= :fechaFinSuplencia " +
            "AND l.pedidoHasta >= :fechaInicioSuplencia " +
            "AND :fechaInicioSuplencia >= l.pedidoDesde " +
            "AND :fechaFinSuplencia <= l.pedidoHasta")
    List<Licencia> findLicenciasActivasCubriendoDesignacionEnPeriodo(
            @Param("personaConLicencia") Persona personaConLicencia,
            @Param("designacionEspecifica") Designacion designacionEspecifica,
            @Param("fechaInicioSuplencia") LocalDate fechaInicioSuplencia,
            @Param("fechaFinSuplencia") LocalDate fechaFinSuplencia);
}
