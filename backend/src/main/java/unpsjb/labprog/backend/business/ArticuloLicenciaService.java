package unpsjb.labprog.backend.business;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import unpsjb.labprog.backend.business.interfaces.servicios.IArticuloLicenciaService;
import unpsjb.labprog.backend.model.ArticuloLicencia;

@Service
public class ArticuloLicenciaService implements IArticuloLicenciaService {

    private static final Logger logger = LoggerFactory.getLogger(ArticuloLicenciaService.class);
    private static final String ARTICULOS_CONFIG_FILE = "articulos-licencia.properties";

    @Autowired
    private ArticuloLicenciaRepository articuloLicenciaRepository;

    // Aplicando DIP: Dependemos de la abstracción específica IArticuloLicenciaMensajeBuilder

    public ArticuloLicencia findById(Long id) {
        return articuloLicenciaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ArticuloLicencia no encontrado"));
    }

    public List<ArticuloLicencia> findAll() {
        return articuloLicenciaRepository.findAll();
    }

    public ArticuloLicencia findByArticulo(String codigo) {
        return articuloLicenciaRepository.findByArticulo(codigo)
                .orElseThrow(() -> new IllegalArgumentException("ArticuloLicencia no encontrado"));
    }

    public ArticuloLicencia save(ArticuloLicencia articulo) {
        return articuloLicenciaRepository.save(articulo);
    }

    public boolean existePorCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return false;
        }
        return articuloLicenciaRepository.findByArticulo(codigo.trim()).isPresent();
    }

    public void cargarArticulosDesdeConfiguracion() {
        logger.info("Iniciando carga de artículos de licencia desde configuración");
        
        Properties articulosConfig = cargarConfiguracionArticulos();
        if (articulosConfig.isEmpty()) {
            logger.warn("No se encontraron artículos para cargar en {}", ARTICULOS_CONFIG_FILE);
            return;
        }
        
        int articulosRegistrados = 0;
        int articulosExistentes = 0;
        
        for (String codigoArticulo : articulosConfig.stringPropertyNames()) {
            try {
                String descripcion = articulosConfig.getProperty(codigoArticulo);
                boolean existia = existePorCodigo(codigoArticulo);
                
                if (!existia) {
                    ArticuloLicencia articulo = new ArticuloLicencia();
                    articulo.setArticulo(codigoArticulo);
                    articulo.setDescripcion(descripcion);
                    
                    articuloLicenciaRepository.save(articulo);
                    articulosRegistrados++;
                    logger.info("Artículo {} registrado: {}", codigoArticulo, descripcion);
                } else {
                    articulosExistentes++;
                    logger.debug("Artículo {} ya existe en el sistema", codigoArticulo);
                }
                
            } catch (Exception e) {
                logger.error("Error registrando artículo {}: {}", codigoArticulo, e.getMessage(), e);
            }
        }
        
        logger.info("Carga de artículos completada. Nuevos: {}, Existentes: {}", 
            articulosRegistrados, articulosExistentes);
    }
    
    /**
     * Carga la configuración de artículos desde el archivo articulos-licencia.properties.
     * 
     * @return Properties con la configuración de artículos (código=descripción)
     */
    private Properties cargarConfiguracionArticulos() {
        Properties props = new Properties();
        
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(ARTICULOS_CONFIG_FILE)) {
            if (input == null) {
                logger.error("Archivo de configuración {} no encontrado", ARTICULOS_CONFIG_FILE);
                return props;
            }
            
            props.load(input);
            logger.debug("Configuración de artículos cargada: {} artículos encontrados", props.size());
            
        } catch (IOException e) {
            logger.error("Error cargando configuración desde {}: {}", ARTICULOS_CONFIG_FILE, e.getMessage(), e);
        }
        
        return props;
    }

}
