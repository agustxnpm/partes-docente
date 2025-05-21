package unpsjb.labprog.backend.business.validaciones;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import unpsjb.labprog.backend.model.Licencia;

public class Articulo23AValidator implements ArticuloValidator {

    private static final int MAX_DAYS_PER_YEAR = 30;

    @Override
    public void validate(Licencia nuevaLicencia, List<Licencia> licenciasExistentesAnioPersona)
            throws IllegalArgumentException {
        // 1. Validar superposición con CUALQUIER licencia existente
        for (Licencia existente : licenciasExistentesAnioPersona) {
            if (nuevaLicencia.getId() != 0 && existente.getId() == nuevaLicencia.getId()) {
                continue; // No compararse consigo misma en caso de actualización
            }
            // Verifica si hay superposición: (InicioA <= FinB) y (FinA >= InicioB)
            boolean haySuperposicion = !nuevaLicencia.getPedidoDesde().isAfter(existente.getPedidoHasta()) &&
                    !nuevaLicencia.getPedidoHasta().isBefore(existente.getPedidoDesde());
            if (haySuperposicion) {
                throw new IllegalArgumentException(
                        "NO se otorga Licencia artículo 23A a " + nuevaLicencia.getPersona().getNombre() +
                                " debido a que ya posee una licencia (" + existente.getArticuloLicencia().getArticulo()
                                +
                                " del " + existente.getPedidoDesde() + " al " + existente.getPedidoHasta() +
                                ") en el mismo período.");
            }
        }

        // 2. Validar tope anual de 30 días para el artículo 23A
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

        if (diasYaTomados23A + diasSolicitados > MAX_DAYS_PER_YEAR) {
            throw new IllegalArgumentException(
                    "NO se otorga Licencia artículo 23A a " + nuevaLicencia.getPersona().getNombre() +
                            " debido a que supera el tope de " + MAX_DAYS_PER_YEAR + " días de licencia. " +
                            "Días ya tomados (Art. 23A): " + diasYaTomados23A + ", Días solicitados: " + diasSolicitados
                            + ".");
        }
    }
}
