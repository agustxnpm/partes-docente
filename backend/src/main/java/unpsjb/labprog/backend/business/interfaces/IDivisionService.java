package unpsjb.labprog.backend.business.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;

import unpsjb.labprog.backend.model.Division;

/**
 * Interfaz para el servicio de divisiones.
 * Aplica el principio DIP (Dependency Inversion Principle) proporcionando una abstracción
 * que permite desacoplar las clases cliente de la implementación concreta.
 */
public interface IDivisionService {
    
    /**
     * Guarda una división
     */
    Division save(Division division);
    
    /**
     * Elimina una división
     */
    void delete(Division division);
    
    /**
     * Busca divisiones con paginación
     */
    Page<Division> findByPage(int page, int size);
    
    /**
     * Busca todas las divisiones
     */
    List<Division> findAll();
    
    /**
     * Busca división por ID
     */
    Division findById(Long id);
    
    /**
     * Obtiene mensaje de éxito para división creada
     */
    String getMensajeExito(Division division);
    
    /**
     * Obtiene mensaje de éxito para división actualizada
     */
    String getMensajeExitoActualizacion(Division division);
    
    /**
     * Obtiene mensaje de éxito para división borrada
     */
    String getMensajeExitoBorrado(Division division);
    
    /**
     * Busca división existente por criterios completos
     */
    Division buscarDivisionExistente(Division division);
    
    /**
     * Busca división existente por año y número
     */
    Division buscarDivisionExistente(int anio, int numDivision);
}
