package unpsjb.labprog.backend.business.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import unpsjb.labprog.backend.model.Persona;

/**
 * Interfaz para el servicio de personas.
 * Aplica el principio DIP (Dependency Inversion Principle) proporcionando una abstracción
 * que permite desacoplar las clases cliente de la implementación concreta.
 */
public interface IPersonaService {
    
    /**
     * Busca personas con paginación
     */
    Page<Persona> findByPage(int page, int size);
    
    /**
     * Busca todas las personas
     */
    List<Persona> findAll();
    
    /**
     * Guarda una persona
     */
    Persona save(Persona persona);
    
    /**
     * Actualiza una persona
     */
    Persona update(Persona personaActualizada);
    
    /**
     * Elimina una persona
     */
    void delete(Persona persona);
    
    /**
     * Busca persona por ID
     */
    Persona findById(Long id);
    
    /**
     * Busca persona por DNI
     */
    Optional<Persona> findByDni(Long dni);
    
    /**
     * Busca persona por CUIL
     */
    Optional<Persona> findByCuil(String cuil);
    
    /**
     * Busca personas por término de búsqueda
     */
    List<Persona> search(String term);
    
    /**
     * Obtiene mensaje de éxito para persona creada
     */
    String getMensajeExito(Persona persona);
    
    /**
     * Obtiene mensaje de éxito para persona actualizada
     */
    String getMensajeExitoActualizacion(Persona persona);
    
    /**
     * Obtiene mensaje de éxito para persona borrada
     */
    String getMensajeExitoBorrado(Persona persona);
}
