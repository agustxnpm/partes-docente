package unpsjb.labprog.backend.business.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import unpsjb.labprog.backend.model.Cargo;
import unpsjb.labprog.backend.model.Division;

/**
 * Interfaz para el servicio de cargos.
 * Aplica el principio DIP (Dependency Inversion Principle) proporcionando una abstracción
 * que permite desacoplar las clases cliente de la implementación concreta.
 */
public interface ICargoService {
    
    /**
     * Guarda un cargo
     */
    Cargo save(Cargo cargo);
    
    /**
     * Elimina un cargo
     */
    void delete(Cargo cargo);
    
    /**
     * Busca cargos con paginación
     */
    Page<Cargo> findByPage(int page, int size);
    
    /**
     * Busca todos los cargos
     */
    List<Cargo> findAll();
    
    /**
     * Busca cargo por ID
     */
    Cargo findById(Long id);
    
    /**
     * Busca cargo por nombre
     */
    Optional<Cargo> findByNombre(String nombre);
    
    /**
     * Busca cargo por nombre y división
     */
    Optional<Cargo> findByNombreAndDivision(String nombre, Division division);
    
    /**
     * Busca cargo por nombre y división exacta
     */
    Optional<Cargo> findByNombreAndDivisionExacta(String nombre, Division division);
    
    /**
     * Obtiene mensaje de éxito para cargo creado
     */
    String getMensajeExito(Cargo cargo);
    
    /**
     * Obtiene mensaje de éxito para cargo actualizado
     */
    String getMensajeExitoActualizacion(Cargo cargo);
    
    /**
     * Obtiene mensaje de éxito para cargo borrado
     */
    String getMensajeExitoBorrado(Cargo cargo);
    
    /**
     * Verifica si existen cargos para una división
     */
    boolean existsByDivisionId(long id);
    
    /**
     * Busca cargos por término de búsqueda
     */
    List<Cargo> search(String term);
}
