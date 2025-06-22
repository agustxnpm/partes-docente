package unpsjb.labprog.backend.business.interfaces.servicios;

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
     * Guarda o actualiza un artículo de licencia en el sistema.
     * @param articulo El artículo de licencia a guardar
     * @return El artículo de licencia guardado
     */
    ArticuloLicencia save(ArticuloLicencia articulo);
    
    /**
     * Verifica si existe un artículo de licencia con el código especificado.
     * @param codigo El código del artículo a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existePorCodigo(String codigo);
    
    /**
     * Carga automáticamente todos los artículos de licencia definidos
     * en el archivo de configuración articulos-licencia.properties.
     * Esta operación se ejecuta al inicializar el sistema.
     */
    void cargarArticulosDesdeConfiguracion();

}
