package unpsjb.labprog.backend.business.interfaces;

import java.util.List;
import unpsjb.labprog.backend.model.ArticuloLicencia;

/**
 * Interfaz para el servicio de artículos de licencias.
 * Define las operaciones disponibles para la gestión de artículos de licencias.
 */
public interface IArticuloLicenciaService {
    
    /**
     * Busca un artículo de licencia por su ID.
     * @param id El ID del artículo de licencia
     * @return El artículo de licencia encontrado
     */
    ArticuloLicencia findById(Long id);
    
    /**
     * Obtiene todos los artículos de licencias.
     * @return Lista de todos los artículos de licencias
     */
    List<ArticuloLicencia> findAll();
    
    /**
     * Busca un artículo de licencia por su código de artículo.
     * @param codigo El código del artículo
     * @return El artículo de licencia encontrado
     */
    ArticuloLicencia findByArticulo(String codigo);
    
    /**
     * Genera mensaje de éxito para creación de artículo de licencia.
     * @param articuloLicencia El artículo de licencia creado
     * @return Mensaje de éxito
     */
    String getMensajeExito(ArticuloLicencia articuloLicencia);
    
    /**
     * Genera mensaje de éxito para borrado de artículo de licencia.
     * @param articuloLicencia El artículo de licencia borrado
     * @return Mensaje de éxito
     */
    String getMensajeExitoBorrado(ArticuloLicencia articuloLicencia);
    
    /**
     * Genera mensaje de éxito para actualización de artículo de licencia.
     * @param articuloLicencia El artículo de licencia actualizado
     * @return Mensaje de éxito
     */
    String getMensajeExitoActualizacion(ArticuloLicencia articuloLicencia);
}
