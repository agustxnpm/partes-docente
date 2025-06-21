package unpsjb.labprog.backend.business.interfaces.servicios;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;

import unpsjb.labprog.backend.model.Designacion;
import unpsjb.labprog.backend.model.Persona;

/**
 * Interfaz para el servicio de designaciones.
 * Aplica el principio DIP (Dependency Inversion Principle) proporcionando una abstracción
 * que permite desacoplar las clases cliente de la implementación concreta.
 * 
 */
public interface IDesignacionService {
    
    /**
     * Guarda una designación
     */
    Designacion save(Designacion designacion);
    
    /**
     * Elimina una designación
     */
    void delete(Designacion designacion);
    
    /**
     * Busca todas las designaciones
     */
    List<Designacion> findAll();
    
    /**
     * Busca designación por ID
     */
    Designacion findById(Long id);
    
    /**
     * Busca designaciones superpuestas
     */
    List<Designacion> findDesignacionesSuperpuestas(Long cargoId, LocalDate fechaInicio, LocalDate fechaFin, Long designacionId);
    
    /**
     * Busca designaciones con paginación
     */
    Page<Designacion> findByPage(int page, int size);
    
    /**
     * Busca suplencias en un período
     */
    List<Designacion> findSuplenciasEnPeriodo(Long cargoId, LocalDate fechaInicio, LocalDate fechaFin, Persona personaTitular, Long designacionId);
    
    /**
     * Encuentra la persona que está siendo reemplazada por una suplencia
     */
    Persona encontrarPersonaReemplazada(Designacion designacionGuardada);

    /**
     * Busca todas las designaciones vigentes de una persona en un período específico
     */
    List<Designacion> findAllByPersonaAndPeriodoVigente(Persona persona, LocalDate pedidoDesde, LocalDate pedidoHasta);
}
