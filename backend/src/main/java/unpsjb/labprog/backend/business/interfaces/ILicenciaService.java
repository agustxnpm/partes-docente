package unpsjb.labprog.backend.business.interfaces;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;

import unpsjb.labprog.backend.model.Cargo;
import unpsjb.labprog.backend.model.EstadoLicencia;
import unpsjb.labprog.backend.model.Licencia;
import unpsjb.labprog.backend.model.LogLicencia;
import unpsjb.labprog.backend.model.Persona;

/**
 * Interfaz para el servicio de licencias.
 * Aplica el principio DIP (Dependency Inversion Principle) proporcionando una abstracción
 * que permite desacoplar las clases cliente de la implementación concreta.
 */
public interface ILicenciaService {
    
    /**
     * Agrega un log a una licencia
     */
    LogLicencia agregarLog(Licencia licencia, EstadoLicencia estado, String mensaje);
    
    /**
     * Crea una nueva licencia
     */
    Licencia createLicencia(Licencia licencia);
    
    /**
     * Actualiza una licencia existente
     */
    Licencia updateLicencia(Long id, Licencia licenciaActualizar);
    
    /**
     * Busca licencias con paginación
     */
    Page<Licencia> findByPage(int page, int size);
    
    /**
     * Busca licencias por criterios específicos
     */
    List<Licencia> buscarLicenciasPorDni(Long personaDni, String codigoArticulo, LocalDate desde, LocalDate hasta);
    
    /**
     * Busca licencia por ID
     */
    Licencia findById(Long id);
    
    /**
     * Busca licencias por estado
     */
    List<Licencia> findByEstado(EstadoLicencia estado);
    
    /**
     * Busca licencias por persona
     */
    List<Licencia> findByPersona(Persona persona);
    
    /**
     * Obtiene todas las licencias
     */
    List<Licencia> getAllLicencias();
    
    /**
     * Obtiene mensaje de éxito para licencia otorgada
     */
    String getMensajeExitoLicenciaOtorgada(Licencia licencia);
    
    /**
     * Obtiene mensaje de éxito para licencia actualizada
     */
    String getMensajeExitoLicenciaActualizada(Licencia licencia);
    
    /**
     * Busca licencias que cubren un período completo
     */
    List<Licencia> findLicenciasQueCubrenPeriodoCompleto(Cargo cargo, Persona persona, LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Busca licencias por persona y año
     */
    List<Licencia> findByPersonaAndYear(Persona persona, int year);
}
