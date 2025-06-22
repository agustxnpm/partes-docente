package unpsjb.labprog.backend.business.validaciones.plugins.licencias;

import java.util.List;
import java.util.stream.Collectors;

import unpsjb.labprog.backend.business.interfaces.servicios.ILicenciaService;
import unpsjb.labprog.backend.business.interfaces.validaciones.ILicenciaRule;
import unpsjb.labprog.backend.model.Licencia;

/**
 * Validador para el artículo 23A de licencias.
 * Este validador asegura que las licencias del artículo 23A cumplan con las restricciones
 * de límite anual de días.
 * Según la normativa, se permite un máximo de 30 días de licencia por año para este artículo.
 * 
 * Implementa el patrón Singleton para ser compatible con el sistema de carga dinámica.
 */
public class Articulo23ALicenciaRule implements ILicenciaRule {

    private static final int MAX_DIAS_POR_ANIO = 30;
    
    private static Articulo23ALicenciaRule instance;
    private ILicenciaService licenciaService;

    // Constructor privado para Singleton
    private Articulo23ALicenciaRule() {
    }
    
    // Patrón Singleton para carga dinámica
    public static Articulo23ALicenciaRule getInstance() {
        if (instance == null) {
            instance = new Articulo23ALicenciaRule();
        }
        return instance;
    }
    
    // Método para inyección de servicios
    public void setLicenciaService(ILicenciaService licenciaService) {
        this.licenciaService = licenciaService;
    }

    @Override
    public void validate(Licencia nuevaLicencia) {
        // Solo validar si es una licencia del artículo 23A
        if (!"23A".equals(nuevaLicencia.getArticuloLicencia().getArticulo())) {
            return; // No aplica a esta licencia
        }
        
        if (licenciaService == null) {
            throw new IllegalStateException("LicenciaService no está disponible para la validación");
        }
        
        List<Licencia> licenciasExistentes = buscarLicenciasExistentesPorAnioYPersona(nuevaLicencia);
        validarLimiteAnualDias(nuevaLicencia, licenciasExistentes);
    }

    /**
     * Busca las licencias existentes de la persona en el año de la solicitud para el artículo 23A.
     */
    private List<Licencia> buscarLicenciasExistentesPorAnioYPersona(Licencia licencia) {
        List<Licencia> todasLasLicencias = licenciaService.findByPersonaAndYear(
                licencia.getPersona(),
                licencia.getPedidoDesde().getYear());
                
        // Filtrar solo las licencias del artículo 23A
        return todasLasLicencias.stream()
                .filter(lic -> "23A".equals(lic.getArticuloLicencia().getArticulo()))
                .collect(Collectors.toList());
    }

    /**
     * Valida que los días solicitados no superen el límite anual permitido para el artículo 23A.
     */
    private void validarLimiteAnualDias(Licencia nuevaLicencia, List<Licencia> licenciasExistentes) {
        long diasYaUsados = licenciasExistentes.stream()
                .filter(licencia -> licencia.getId() != nuevaLicencia.getId()) // Excluir la licencia actual en caso de actualización
                .mapToLong(lic -> java.time.temporal.ChronoUnit.DAYS.between(lic.getPedidoDesde(), lic.getPedidoHasta()) + 1)
                .sum();

        long diasSolicitados = java.time.temporal.ChronoUnit.DAYS.between(
                nuevaLicencia.getPedidoDesde(),
                nuevaLicencia.getPedidoHasta()) + 1;

        if (diasYaUsados + diasSolicitados > MAX_DIAS_POR_ANIO) {
            throw new IllegalArgumentException(
                "NO se otorga Licencia artículo 23A a " + nuevaLicencia.getPersona().getNombre() + " " +
                        nuevaLicencia.getPersona().getApellido() + " debido a que supera el tope de " +
                        MAX_DIAS_POR_ANIO + " días de licencia");
        }
    }

    @Override
    public String getRuleName() {
        return "Validación Artículo 23A";
    }
}
