package unpsjb.labprog.backend.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import unpsjb.labprog.backend.model.ArticuloLicencia;

@Component
public class ArticuloLicenciaValidator {

    @Autowired
    @Lazy
    private ArticuloLicenciaService articuloLicenciaService; 

    public void validarArticuloLicencia(ArticuloLicencia articuloLicencia) {
        if (articuloLicencia == null) {
            throw new IllegalArgumentException("El objeto ArticuloLicencia no puede ser nulo.");
        }

        validarCodigoArticulo(articuloLicencia);
        validarDescripcion(articuloLicencia.getDescripcion());
    }

    private void validarCodigoArticulo(ArticuloLicencia articuloLicencia) {
        String codigo = articuloLicencia.getArticulo();
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código del artículo de licencia no puede estar vacío.");
        }
        if (codigo.length() > 10) {
            throw new IllegalArgumentException("El código del artículo de licencia no puede exceder los 10 caracteres. Proporcionado: '" + codigo + "'");
        }

        // Validar unicidad
        ArticuloLicencia existente = articuloLicenciaService.findByArticulo(codigo);
        if (existente.getId() != articuloLicencia.getId()) {
            throw new DataIntegrityViolationException("El código del artículo de licencia ya existe. Proporcionado: '" + codigo + "'");
        }
    }

    private void validarDescripcion(String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del artículo de licencia no puede estar vacía.");
        }
        if (descripcion.length() > 90) {
            throw new IllegalArgumentException("La descripción del artículo de licencia no puede exceder los 90 caracteres.");
        }
    }

    public void validarBorrado(ArticuloLicencia articuloLicencia) {
      
    }
}