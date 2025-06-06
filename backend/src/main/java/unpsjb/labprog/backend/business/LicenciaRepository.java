package unpsjb.labprog.backend.business;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import unpsjb.labprog.backend.model.Cargo;
import unpsjb.labprog.backend.model.EstadoLicencia;
import unpsjb.labprog.backend.model.Licencia;
import unpsjb.labprog.backend.model.Persona;

@Repository
public interface LicenciaRepository extends JpaRepository<Licencia, Long> {

        /**
         * Busca todas las licencias VÁLIDAS de una persona en un año específico.
         * Solo se consideran licencias con estado VALIDA para las validaciones.
         */
        @Query("SELECT l FROM Licencia l WHERE l.persona = :persona " +
                        "AND EXTRACT(YEAR FROM l.pedidoDesde) = :year " +
                        "AND l.estado = 'VALIDA'")
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
                        "WHERE d.cargo = :cargo " +
                        "AND l.persona = :personaConLicencia " +
                        "AND l.pedidoDesde <= :fechaInicioSuplencia " +
                        "AND l.pedidoHasta >= :fechaFinSuplencia " +
                        "AND l.estado = 'VALIDA'")
        List<Licencia> findLicenciasActivasCubriendoCargoEnPeriodo(
                        @Param("cargo") Cargo cargo,
                        @Param("personaConLicencia") Persona personaConLicencia,
                        @Param("fechaInicioSuplencia") LocalDate fechaInicioSuplencia,
                        @Param("fechaFinSuplencia") LocalDate fechaFinSuplencia);

        List<Licencia> findByEstado(EstadoLicencia estado);

        /**
         * Verifica si un período está completamente cubierto por licencias de una
         * persona
         * para un cargo específico.
         * Se asume que el parámetro :fechaFin siempre será una fecha concreta,
         * incluso si representa una designación "abierta" (en cuyo caso será una fecha
         * muy lejana).
         * Las licencias siempre tienen una fecha de fin (pedidoHasta).
         */
        @Query("SELECT DISTINCT l FROM Licencia l " +
                        "WHERE l.persona = :persona " +
                        "AND l.estado = 'VALIDA' " +
                        "AND EXISTS (" +
                        "    SELECT d FROM l.designaciones d " +
                        "    WHERE d.cargo = :cargo" +
                        ") " +
                        "AND l.pedidoDesde <= :fechaInicio " +
                        "AND l.pedidoHasta >= :fechaFin")
        List<Licencia> findLicenciasQueCubrenPeriodoCompleto(
                        @Param("cargo") Cargo cargo,
                        @Param("persona") Persona persona,
                        @Param("fechaInicio") LocalDate fechaInicio,
                        @Param("fechaFin") LocalDate fechaFin);

        @Query("SELECT l FROM Licencia l " +
                        "WHERE l.persona = :persona " +
                        "AND l.estado = 'VALIDA' " +
                        "AND EXISTS (" +
                        "    SELECT d FROM l.designaciones d " +
                        "    WHERE d.cargo = :cargo" +
                        ") " +
                        "AND l.pedidoHasta >= :fechaInicio " +
                        "AND l.pedidoDesde <= :fechaFin " +
                        "ORDER BY l.pedidoDesde")
        List<Licencia> findLicenciasEnPeriodo(
                        @Param("cargo") Cargo cargo,
                        @Param("persona") Persona persona,
                        @Param("fechaInicio") LocalDate fechaInicio,
                        @Param("fechaFin") LocalDate fechaFin);

        /**
         * Busca todas las licencias de una persona específica sin filtrar por estado
         */
        @Query("SELECT l FROM Licencia l WHERE l.persona = :persona")
        List<Licencia> findByPersona(@Param("persona") Persona persona);

}
