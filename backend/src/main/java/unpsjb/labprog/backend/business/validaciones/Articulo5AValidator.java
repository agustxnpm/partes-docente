package unpsjb.labprog.backend.business.validaciones;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import unpsjb.labprog.backend.business.utilidades.ValidadorArticulo;
import unpsjb.labprog.backend.model.Licencia;

/**
 * Validador para el artículo 5A de licencias.
 * Este validador asegura que las licencias del artículo 5A cumplan con las restricciones
 * de certificado médico y el límite anual de días.
 */
@ValidadorArticulo(codigoArticulo = "5A")
public class Articulo5AValidator implements ArticuloLicenciaValidator {

    private static final int MAX_DIAS_POR_ANIO = 30;

    @Override
    public void validate(Licencia nuevaLicencia, List<Licencia> licenciasExistentesAnioPersona)
            throws IllegalArgumentException {

        if (!nuevaLicencia.isCertificadoMedico()) {
            throw new IllegalArgumentException(
                    "NO se otorga Licencia artículo 5A a " + nuevaLicencia.getPersona().getNombre() +
                          " debido a que no presenta certificado médico requerido para este tipo de licencia.");
        }
        
        List<Licencia> licencias5AExistentes = licenciasExistentesAnioPersona.stream()
                .filter(l -> "5A".equals(l.getArticuloLicencia().getArticulo()) &&
                        (nuevaLicencia.getId() == 0 || l.getId() != nuevaLicencia.getId())) // Excluirse a sí misma si
                                                                                            // es una actualización
                .collect(Collectors.toList());

        long diasSolicitados = ChronoUnit.DAYS.between(nuevaLicencia.getPedidoDesde(), nuevaLicencia.getPedidoHasta())
                + 1;
        long diasYaTomados = licencias5AExistentes.stream()
                .filter(l -> l.getPedidoDesde().getYear() == nuevaLicencia.getPedidoDesde().getYear())
                .mapToLong(l -> ChronoUnit.DAYS.between(l.getPedidoDesde(), l.getPedidoHasta()) + 1)
                .sum();

        if (diasYaTomados + diasSolicitados > MAX_DIAS_POR_ANIO) {
            throw new IllegalArgumentException(
                    "NO se otorga Licencia artículo 5A a " + nuevaLicencia.getPersona().getNombre() +
                            " " + nuevaLicencia.getPersona().getApellido() + " debido a que supera el tope de " +
                            MAX_DIAS_POR_ANIO + " días de licencia");
        }
    }

}
