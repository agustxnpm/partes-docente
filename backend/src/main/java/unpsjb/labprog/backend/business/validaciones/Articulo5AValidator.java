package unpsjb.labprog.backend.business.validaciones;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import unpsjb.labprog.backend.model.Licencia;

public class Articulo5AValidator implements ArticuloValidator {

    private static final int MAX_DAYS_PER_YEAR = 30;

    @Override
    public void validate(Licencia nuevaLicencia, List<Licencia> licenciasExistentesAnioPersona)
            throws IllegalArgumentException {
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

        if (diasYaTomados + diasSolicitados > MAX_DAYS_PER_YEAR) {
            throw new IllegalArgumentException(
                    "NO se otorga Licencia artículo 5A a " + nuevaLicencia.getPersona().getNombre() +
                            " debido a que supera el tope de " + MAX_DAYS_PER_YEAR + " días de licencia. " +
                            "Días ya tomados: " + diasYaTomados + ", Días solicitados: " + diasSolicitados + ".");
        }
    }

}
