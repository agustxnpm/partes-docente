package unpsjb.labprog.backend.business;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import unpsjb.labprog.backend.model.Designacion;
import unpsjb.labprog.backend.model.Persona;

@Repository
public interface DesignacionRepository extends JpaRepository<Designacion, Long> {

        /**
         * Busca designaciones que se solapen con el periodo especificado para el mismo
         * cargo.
         * 
         * La consulta maneja casos donde la fecha de fin puede ser nula, lo que indica
         * un período indefinido. La función utiliza la lógica de negación de la no
         * superposición para determinar si dos períodos se solapan.
         *
         * @param cargoId       El ID del cargo a verificar
         * @param fechaInicio   Fecha de inicio del periodo a verificar
         * @param fechaFin      Fecha de fin del periodo a verificar (puede ser null
         *                      para periodos indefinidos)
         * @param designacionId ID de la designación a excluir (útil para
         *                      actualizaciones, puede ser null para nuevas)
         * @return Lista de designaciones que se solapan con el periodo especificado
         */
        @Query(value = "SELECT d FROM Designacion d WHERE d.cargo.id = :cargo " +
                        "AND (:designacionId IS NULL OR d.id != :designacionId) " +
                        "AND (" +
                        "  (d.fechaInicio <= COALESCE(:fechaFin, d.fechaInicio) " +
                        "   AND COALESCE(d.fechaFin, :fechaFin) >= :fechaInicio)" +
                        "  OR (d.fechaFin IS NULL AND CAST(:fechaFin AS java.time.LocalDate) IS NULL)" +
                        ")")
        List<Designacion> findDesignacionesSuperpuestas(
                        @Param("cargo") Long cargoId,
                        @Param("fechaInicio") LocalDate fechaInicio,
                        @Param("fechaFin") LocalDate fechaFin,
                        @Param("designacionId") Long designacionId);

        /**
         * Encuentra todas las designaciones vigentes de una persona en un período
         * específico.
         * 
         * @param persona    persona que se toma la licencia
         * @param fechaDesde inico de la designacion
         * @param fechaHasta fin de la designacion (puede ser null si es indefinida)
         * @return lista de designaciones vigentes en ese periodo
         */
        @Query("SELECT d FROM Designacion d WHERE d.persona = :persona " +
                        "AND d.fechaInicio <= :fechaHasta " +
                        "AND (d.fechaFin >= :fechaDesde OR d.fechaFin IS NULL)")
        List<Designacion> findAllByPersonaAndPeriodoVigente(
                        @Param("persona") Persona persona,
                        @Param("fechaDesde") LocalDate fechaDesde,
                        @Param("fechaHasta") LocalDate fechaHasta);

        /**
         * Busca designaciones que actúen como suplencias para un cargo específico en un
         * período.
         * Excluye designaciones de la persona titular (que tiene licencia).
         * 
         * @param cargoId        El ID del cargo a verificar
         * @param fechaInicio    Fecha de inicio del periodo a verificar
         * @param fechaFin       Fecha de fin del periodo a verificar
         * @param personaTitular Persona titular del cargo (a excluir de la búsqueda)
         * @param designacionId  ID de la designación a excluir (para actualizaciones)
         * @return Lista de designaciones suplentes que se solapan con el periodo
         */
        @Query("SELECT d FROM Designacion d WHERE d.cargo.id = :cargo " +
                        "AND d.persona != :personaTitular " +
                        "AND (:designacionId IS NULL OR d.id != :designacionId) " +
                        "AND (" +
                        "  (d.fechaInicio <= COALESCE(:fechaFin, d.fechaInicio) " +
                        "   AND COALESCE(d.fechaFin, :fechaFin) >= :fechaInicio)" +
                        "  OR (d.fechaFin IS NULL AND CAST(:fechaFin AS java.time.LocalDate) IS NULL)" +
                        ")")
        List<Designacion> findSuplenciasEnPeriodo(
                        @Param("cargo") Long cargoId,
                        @Param("fechaInicio") LocalDate fechaInicio,
                        @Param("fechaFin") LocalDate fechaFin,
                        @Param("personaTitular") Persona personaTitular,
                        @Param("designacionId") Long designacionId);
}
