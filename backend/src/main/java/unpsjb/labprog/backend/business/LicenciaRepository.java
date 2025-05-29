package unpsjb.labprog.backend.business;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import unpsjb.labprog.backend.model.Cargo;
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

        /*
         * @Query("SELECT l FROM Licencia l JOIN l.designaciones d " +
         * "WHERE d = :designacionEspecifica " +
         * "AND l.persona = :personaConLicencia " +
         * "AND l.pedidoDesde <= :fechaFinSuplencia " +
         * "AND l.pedidoHasta >= :fechaInicioSuplencia " +
         * "AND :fechaInicioSuplencia >= l.pedidoDesde " +
         * "AND :fechaFinSuplencia <= l.pedidoHasta")
         * List<Licencia> findLicenciasActivasCubriendoDesignacionEnPeriodo(
         * 
         * @Param("personaConLicencia") Persona personaConLicencia,
         * 
         * @Param("designacionEspecifica") Designacion designacionEspecifica,
         * 
         * @Param("fechaInicioSuplencia") LocalDate fechaInicioSuplencia,
         * 
         * @Param("fechaFinSuplencia") LocalDate fechaFinSuplencia);
         */

        @Query("SELECT l FROM Licencia l JOIN l.designaciones d " +
                        "WHERE d.cargo = :cargo " +
                        "AND l.persona = :personaConLicencia " +
                        "AND l.pedidoDesde <= :fechaInicioSuplencia " +
                        "AND l.pedidoHasta >= :fechaFinSuplencia")
        List<Licencia> findLicenciasActivasCubriendoCargoEnPeriodo(
                        @Param("cargo") Cargo cargo,
                        @Param("personaConLicencia") Persona personaConLicencia,
                        @Param("fechaInicioSuplencia") LocalDate fechaInicioSuplencia,
                        @Param("fechaFinSuplencia") LocalDate fechaFinSuplencia);

        /**
         * Busca licencias VÁLIDAS que se solapen con el periodo especificado para
         * la misma persona.
         *
         * La consulta verifica si hay solapamiento entre dos períodos, es decir, si
         * la fecha de inicio de una licencia es menor o igual a la fecha de fin de
         * otra, y la fecha de fin de una licencia es mayor o igual a la fecha de
         * inicio de otra.
         *
         * @param personaDni  El DNI de la persona a verificar
         * @param pedidoDesde Fecha de inicio del periodo a verificar
         * @param pedidoHasta Fecha de fin del periodo a verificar
         * @param licenciaId  ID de la licencia a excluir (útil para actualizaciones,
         *                    puede ser null para nuevas)
         * @return Lista de licencias que se solapan con el periodo especificado
         */
        /*
         * @Query(value = "SELECT l FROM Licencia l WHERE l.persona.dni = :personaDni "
         * + "AND (:licenciaId IS NULL OR l.id != :licenciaId) "
         * + "AND (l.pedidoDesde <= :pedidoHasta AND l.pedidoHasta >= :pedidoDesde) "
         * + "AND l.estado = unpsjb.labprog.backend.model.enums.Estado.VALIDO")
         * List<Licencia> findLicenciasSuperPuestas(
         * 
         * @Param("personaDni") Long personaDni,
         * 
         * @Param("pedidoDesde") LocalDateTime pedidoDesde,
         * 
         * @Param("pedidoHasta") LocalDateTime pedidoHasta,
         * 
         * @Param("licenciaId") Integer licenciaId);
         */



        /**
         * Verifica si un período está completamente cubierto por licencias de una
         * persona
         * para un cargo específico.
         * Se asume que el parámetro :fechaFin siempre será una fecha concreta,
         * incluso si representa una designación "abierta" (en cuyo caso será una fecha muy lejana).
         * Las licencias siempre tienen una fecha de fin (pedidoHasta).
         */
        @Query("SELECT l FROM Licencia l JOIN l.designaciones d " +
                        "WHERE d.cargo = :cargo " +
                        "AND l.persona = :persona " +
                        "AND l.pedidoDesde <= :fechaInicio " +
                        "AND l.pedidoHasta >= :fechaFin")
        List<Licencia> findLicenciasQueCubrenPeriodoCompleto(
                        @Param("cargo") Cargo cargo,
                        @Param("persona") Persona persona,
                        @Param("fechaInicio") LocalDate fechaInicio,
                        @Param("fechaFin") LocalDate fechaFin);

}

