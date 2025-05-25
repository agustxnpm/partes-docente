package unpsjb.labprog.backend.business.validaciones;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import unpsjb.labprog.backend.business.utilidades.ValidadorArticulo;
import unpsjb.labprog.backend.model.Licencia;

@ValidadorArticulo(codigoArticulo = "23A")
public class Articulo23AValidator implements ArticuloLicenciaValidator {

    private static final int MAX_DIAS_POR_ANIO= 30;

    @Override
    public void validate(Licencia nuevaLicencia, List<Licencia> licenciasExistentesAnioPersona)
            throws IllegalArgumentException {
        

        // Validar tope anual de 30 días para el artículo 23A
        List<Licencia> licencias23AExistentes = licenciasExistentesAnioPersona.stream()
                .filter(l -> "23A".equals(l.getArticuloLicencia().getArticulo()) &&
                        (nuevaLicencia.getId() == 0 || l.getId() != nuevaLicencia.getId()))
                .collect(Collectors.toList());

        long diasSolicitados = ChronoUnit.DAYS.between(nuevaLicencia.getPedidoDesde(), nuevaLicencia.getPedidoHasta())
                + 1;
        long diasYaTomados23A = licencias23AExistentes.stream()
                .filter(l -> l.getPedidoDesde().getYear() == nuevaLicencia.getPedidoDesde().getYear())
                .mapToLong(l -> ChronoUnit.DAYS.between(l.getPedidoDesde(), l.getPedidoHasta()) + 1)
                .sum();

        if (diasYaTomados23A + diasSolicitados > MAX_DIAS_POR_ANIO) {
            throw new IllegalArgumentException(
                    "NO se otorga Licencia artículo 23A a " + nuevaLicencia.getPersona().getNombre() + " " +
                            nuevaLicencia.getPersona().getApellido() + " debido a que supera el tope de " +
                            MAX_DIAS_POR_ANIO+ " días de licencia"
                            );
        }
    }
}
