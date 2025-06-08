package unpsjb.labprog.backend.business.interfaces;

import java.util.List;
import unpsjb.labprog.backend.model.LogLicencia;
import unpsjb.labprog.backend.model.Licencia;
import unpsjb.labprog.backend.model.EstadoLicencia;

/**
 * Interfaz para el servicio de logs de licencias.
 * Define las operaciones disponibles para la gestión de logs de licencias.
 */
public interface ILogLicenciaService {
    
    /**
     * Crea un log para una licencia.
     * @param licencia La licencia asociada al log
     * @param estado El estado de la licencia
     * @param mensaje El mensaje del log
     * @return El log creado
     */
    LogLicencia crearLog(Licencia licencia, EstadoLicencia estado, String mensaje);
    
    /**
     * Obtiene el último log de una licencia.
     * @param licencia La licencia de la cual obtener el último log
     * @return El último log de la licencia
     */
    LogLicencia obtenerUltimoLog(Licencia licencia);
    
    /**
     * Obtiene todos los logs de una licencia ordenados por fecha descendente.
     * @param id El ID de la licencia
     * @return Lista de logs ordenados por fecha descendente
     */
    List<LogLicencia> obtenerLicenciasEnOrdenPorFechaDesc(Long id);
}
