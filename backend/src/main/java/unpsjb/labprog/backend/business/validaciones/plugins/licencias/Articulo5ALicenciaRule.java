package unpsjb.labprog.backend.business.validaciones.plugins.licencias;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import unpsjb.labprog.backend.business.interfaces.servicios.ILicenciaService;
import unpsjb.labprog.backend.business.interfaces.validaciones.ILicenciaRule;
import unpsjb.labprog.backend.model.Licencia;

/**
 * Validador para el artículo 5A de licencias.
 * Este validador asegura que las licencias del artículo 5A cumplan con las restricciones
 * de certificado médico y el límite anual de días.
 */
public class Articulo5ALicenciaRule implements ILicenciaRule {

    private static final int MAX_DIAS_POR_ANIO = 30;
    private static Articulo5ALicenciaRule instance;
    private ILicenciaService licenciaService;

    private Articulo5ALicenciaRule() {}
    
    public static Articulo5ALicenciaRule getInstance() {
        if (instance == null) {
            instance = new Articulo5ALicenciaRule();
        }
        return instance;
    }

    public void setLicenciaService(ILicenciaService licenciaService) {
        this.licenciaService = licenciaService;
    }

    @Override
    public void validate(Licencia nuevaLicencia) {
        // Solo aplicar validaciones si es artículo 5A
        if (!"5A".equals(nuevaLicencia.getArticuloLicencia().getArticulo())) {
            return; // No aplica a este artículo
        }
        
        validarCertificadoMedico(nuevaLicencia);
        if (licenciaService == null) {
            throw new IllegalStateException("LicenciaService no está disponible para la validación");
        }
        validarLimiteAnualDias(nuevaLicencia, buscarLicenciasExistentesPorAnioYPersona(nuevaLicencia));
    }

    /**
     * Valida que la licencia tenga el certificado médico requerido para el artículo 5A
     */
    private void validarCertificadoMedico(Licencia licencia) {
        if (!licencia.isCertificadoMedico()) {
            throw new IllegalArgumentException(
                    "NO se otorga Licencia artículo 5A a " + licencia.getPersona().getNombre() +
                          " debido a que no presenta certificado médico requerido para este tipo de licencia.");
        }
    }

    /**
     * Busca las licencias existentes de la persona en el año de la solicitud.
     */
    private List<Licencia> buscarLicenciasExistentesPorAnioYPersona (Licencia licencia) {
        return licenciaService.findByPersonaAndYear(
                licencia.getPersona(),
                licencia.getPedidoDesde().getYear());
    }

    /**
     * Valida que los días solicitados no superen el límite anual permitido
     */
    private void validarLimiteAnualDias(Licencia nuevaLicencia, List<Licencia> licenciasExistentesAnioPersona) {
        List<Licencia> licencias5AExistentes = filtrarLicencias5AExistentes(nuevaLicencia, licenciasExistentesAnioPersona);
        
        long diasSolicitados = calcularDiasSolicitados(nuevaLicencia);
        long diasYaTomados = calcularDiasYaTomados(licencias5AExistentes, nuevaLicencia);

        if (superaLimiteAnual(diasYaTomados, diasSolicitados)) {
            lanzarExcepcionLimiteExcedido(nuevaLicencia);
        }
    }

    /**
     * Filtra las licencias existentes para obtener solo las del artículo 5A del mismo año
     */
    private List<Licencia> filtrarLicencias5AExistentes(Licencia nuevaLicencia, List<Licencia> licenciasExistentes) {
        return licenciasExistentes.stream()
                .filter(l -> esLicencia5A(l))
                .filter(l -> noEsLaMismaLicencia(l, nuevaLicencia))
                .collect(Collectors.toList());
    }

    /**
     * Verifica si una licencia es del artículo 5A
     */
    private boolean esLicencia5A(Licencia licencia) {
        return "5A".equals(licencia.getArticuloLicencia().getArticulo());
    }

    /**
     * Verifica que no sea la misma licencia (para casos de actualización)
     */
    private boolean noEsLaMismaLicencia(Licencia licenciaExistente, Licencia nuevaLicencia) {
        return nuevaLicencia.getId() == 0 || licenciaExistente.getId() != nuevaLicencia.getId();
    }

    /**
     * Calcula los días solicitados en la nueva licencia
     */
    private long calcularDiasSolicitados(Licencia licencia) {
        return ChronoUnit.DAYS.between(licencia.getPedidoDesde(), licencia.getPedidoHasta()) + 1;
    }

    /**
     * Calcula los días ya tomados en licencias del artículo 5A en el mismo año
     */
    private long calcularDiasYaTomados(List<Licencia> licencias5AExistentes, Licencia nuevaLicencia) {
        return licencias5AExistentes.stream()
                .filter(l -> esMismoAnio(l, nuevaLicencia))
                .mapToLong(this::calcularDiasSolicitados)
                .sum();
    }

    /**
     * Verifica si la licencia existente es del mismo año que la nueva licencia
     */
    private boolean esMismoAnio(Licencia licenciaExistente, Licencia nuevaLicencia) {
        return licenciaExistente.getPedidoDesde().getYear() == nuevaLicencia.getPedidoDesde().getYear();
    }

    /**
     * Verifica si los días totales superan el límite anual
     */
    private boolean superaLimiteAnual(long diasYaTomados, long diasSolicitados) {
        return diasYaTomados + diasSolicitados > MAX_DIAS_POR_ANIO;
    }

    /**
     * Lanza la excepción cuando se supera el límite anual de días
     */
    private void lanzarExcepcionLimiteExcedido(Licencia licencia) {
        throw new IllegalArgumentException(
                "NO se otorga Licencia artículo 5A a " + licencia.getPersona().getNombre() +
                        " " + licencia.getPersona().getApellido() + " debido a que supera el tope de " +
                        MAX_DIAS_POR_ANIO + " días de licencia");
    }


    @Override
    public String getRuleName() {
        return "Validación de Licencia Artículo 5A";
    }
}
