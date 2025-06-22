package unpsjb.labprog.backend.business.validaciones.plugins.licencias;

import java.time.temporal.ChronoUnit;

import unpsjb.labprog.backend.business.interfaces.validaciones.ILicenciaRule;
import unpsjb.labprog.backend.model.Licencia;

/**
 * Plugin simplificado para validar licencias por matrimonio (Artículo 19).
 * 
 * Validaciones:
 * - Solo para artículo 19
 * - Máximo 10 días
 * - Requiere documentación (certificado)
 * 
 * El artículo 19 se registra automáticamente en el sistema mediante el
 * archivo de configuración articulos-licencia.properties.
 */
public class Articulo19LicenciaRule implements ILicenciaRule {

    private static final int MAX_DIAS = 10;
    
    private static Articulo19LicenciaRule instance;

    private Articulo19LicenciaRule() {}
    
    public static Articulo19LicenciaRule getInstance() {
        if (instance == null) {
            instance = new Articulo19LicenciaRule();
        }
        return instance;
    }

    @Override
    public void validate(Licencia licencia) throws IllegalArgumentException {
        if (!"19".equals(licencia.getArticuloLicencia().getArticulo())) {
            return; // No aplica
        }

        // Validar duración máxima
        long dias = ChronoUnit.DAYS.between(
            licencia.getPedidoDesde(), 
            licencia.getPedidoHasta()) + 1;
            
        if (dias > MAX_DIAS) {
            throw new IllegalArgumentException(
                "No se otorga Licencia artículo 19 a " +
                licencia.getPersona().getNombre() + " " +
                licencia.getPersona().getApellido() +
                " debido a que la duración máxima es de " + MAX_DIAS + " días.");
        }
    }

    @Override
    public String getRuleName() {
        return "Artículo 19 - Licencia por Matrimonio";
    }
}
