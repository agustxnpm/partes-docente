package unpsjb.labprog.backend.business.validaciones;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import unpsjb.labprog.backend.business.utilidades.ValidadorArticulo;
import unpsjb.labprog.backend.model.Licencia;

/**
 * Validador para el artículo 23A de licencias.
 * Este validador asegura que las licencias del artículo 23A cumplan con las restricciones
 * de límite anual de días.
 * Según la normativa, se permite un máximo de 30 días de licencia por año para este artículo.
 * Si se supera este límite, se lanza una excepción IllegalArgumentException.
 */
@ValidadorArticulo(codigoArticulo = "23A")
public class Articulo23AValidator implements ArticuloLicenciaValidator {

    private static final int MAX_DIAS_POR_ANIO= 30;

    @Override
    public void validate(Licencia nuevaLicencia, List<Licencia> licenciasExistentesAnioPersona)
            throws IllegalArgumentException {
        
        validarLimiteAnualDias(nuevaLicencia, licenciasExistentesAnioPersona);
    }

    /**
     * Valida que los días solicitados no superen el límite anual permitido para el artículo 23A
     */
    private void validarLimiteAnualDias(Licencia nuevaLicencia, List<Licencia> licenciasExistentesAnioPersona) {
        List<Licencia> licencias23AExistentes = filtrarLicencias23AExistentes(nuevaLicencia, licenciasExistentesAnioPersona);
        
        long diasSolicitados = calcularDiasSolicitados(nuevaLicencia);
        long diasYaTomados = calcularDiasYaTomados(licencias23AExistentes, nuevaLicencia);

        if (superaLimiteAnual(diasYaTomados, diasSolicitados)) {
            lanzarExcepcionLimiteExcedido(nuevaLicencia);
        }
    }

    /**
     * Filtra las licencias existentes para obtener solo las del artículo 23A del mismo año
     */
    private List<Licencia> filtrarLicencias23AExistentes(Licencia nuevaLicencia, List<Licencia> licenciasExistentes) {
        return licenciasExistentes.stream()
                .filter(l -> esLicencia23A(l))
                .filter(l -> noEsLaMismaLicencia(l, nuevaLicencia))
                .collect(Collectors.toList());
    }

    /**
     * Verifica si una licencia es del artículo 23A
     */
    private boolean esLicencia23A(Licencia licencia) {
        return "23A".equals(licencia.getArticuloLicencia().getArticulo());
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
     * Calcula los días ya tomados en licencias del artículo 23A en el mismo año
     */
    private long calcularDiasYaTomados(List<Licencia> licencias23AExistentes, Licencia nuevaLicencia) {
        return licencias23AExistentes.stream()
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
                "NO se otorga Licencia artículo 23A a " + licencia.getPersona().getNombre() + " " +
                        licencia.getPersona().getApellido() + " debido a que supera el tope de " +
                        MAX_DIAS_POR_ANIO + " días de licencia");
    }
}
