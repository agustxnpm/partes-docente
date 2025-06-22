package unpsjb.labprog.backend.business.validaciones.plugins.licencias;

import unpsjb.labprog.backend.business.interfaces.servicios.ILicenciaService;
import unpsjb.labprog.backend.business.interfaces.validaciones.ILicenciaRule;
import unpsjb.labprog.backend.business.validaciones.locator.ServiceLocator;
import unpsjb.labprog.backend.model.Licencia;

import java.util.List;

/**
 * Regla de validación para verificar superposiciones de licencias.
 * Verifica que no haya conflictos de fechas con licencias existentes para la misma persona.
 * 
 * Implementa el patrón Singleton para ser compatible con el sistema de carga dinámica.
 */
public class SuperposicionLicenciaRule implements ILicenciaRule {
    
    private static SuperposicionLicenciaRule instance;
    private ILicenciaService licenciaService;
    
    // Constructor privado para Singleton
    private SuperposicionLicenciaRule() {
    }
    
    // Patrón Singleton para carga dinámica
    public static SuperposicionLicenciaRule getInstance() {
        if (instance == null) {
            instance = new SuperposicionLicenciaRule();
        }
        return instance;
    }
    
    // Método para inyección de servicios
    public void setLicenciaService(ILicenciaService licenciaService) {
        this.licenciaService = licenciaService;
    }

    @Override
    public void validate(Licencia licencia) {
        if (licenciaService == null) {
            licenciaService = ServiceLocator.getLicenciaService();
        }
        
        if (licenciaService == null) {
            throw new IllegalStateException("LicenciaService no está disponible para la validación");
        }
        
        // Obtener licencias existentes para la persona en el año de la solicitud
        List<Licencia> licenciasExistentesAnioPersona = licenciaService.findByPersonaAndYear(
                licencia.getPersona(),
                licencia.getPedidoDesde().getYear());

        // Validar superposición con CUALQUIER licencia existente
        for (Licencia existente : licenciasExistentesAnioPersona) {
            if (licencia.getId() != 0 && existente.getId() == licencia.getId()) {
                continue; // No compararse consigo misma en caso de actualización
            }
            
            // Verifica si hay superposición: (InicioA <= FinB) y (FinA >= InicioB)
            boolean haySuperposicion = !licencia.getPedidoDesde().isAfter(existente.getPedidoHasta()) &&
                    !licencia.getPedidoHasta().isBefore(existente.getPedidoDesde());
            
            if (haySuperposicion) {
                throw new IllegalArgumentException(
                        "NO se otorga Licencia artículo " + existente.getArticuloLicencia().getArticulo() + " a " + 
                        licencia.getPersona().getNombre() + " " +
                        licencia.getPersona().getApellido() +
                        " debido a que ya posee una licencia en el mismo período");
            }
        }
    }

    @Override
    public String getRuleName() {
        return "Validación de Superposición de Licencias";
    }
}
