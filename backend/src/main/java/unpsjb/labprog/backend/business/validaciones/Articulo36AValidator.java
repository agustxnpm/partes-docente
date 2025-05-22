package unpsjb.labprog.backend.business.validaciones;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import unpsjb.labprog.backend.business.utilidades.ValidadorArticulo;
import unpsjb.labprog.backend.model.Licencia;

@ValidadorArticulo(codigoArticulo = "36A")
public class Articulo36AValidator implements ArticuloLicenciaValidator {

        private static final int MAX_DIAS_POR_MES = 2;
        private static final int MAX_DIAS_POR_ANIO = 6;

        @Override
        public void validate(Licencia nuevaLicencia, List<Licencia> licenciasExistentesAnioPersona)
                        throws IllegalArgumentException {
                long diasSolicitados = ChronoUnit.DAYS.between(nuevaLicencia.getPedidoDesde(),
                                nuevaLicencia.getPedidoHasta())
                                + 1;

                // 1. Validar tope mensual
                List<Licencia> licencias36AEnMesSolicitud = licenciasExistentesAnioPersona.stream()
                                .filter(l -> "36A".equals(l.getArticuloLicencia().getArticulo()) &&
                                                (nuevaLicencia.getId() == 0 || l.getId() != nuevaLicencia.getId()) &&
                                                l.getPedidoDesde().getYear() == nuevaLicencia.getPedidoDesde().getYear()
                                                &&
                                                l.getPedidoDesde().getMonth() == nuevaLicencia.getPedidoDesde()
                                                                .getMonth())
                                .collect(Collectors.toList());

                long diasYaTomadosEnMes = licencias36AEnMesSolicitud.stream()
                                .mapToLong(l -> ChronoUnit.DAYS.between(l.getPedidoDesde(), l.getPedidoHasta()) + 1)
                                .sum();

                if (diasYaTomadosEnMes + diasSolicitados > MAX_DIAS_POR_MES) {
                        throw new IllegalArgumentException(
                                        "NO se otorga Licencia artículo 36A a " + nuevaLicencia.getPersona().getNombre()
                                                        + " " +
                                                        nuevaLicencia.getPersona().getApellido() +
                                                        " debido a que supera el tope de " + MAX_DIAS_POR_MES
                                                        + " días de licencia por mes");
                }

                // 2. Validar tope anual
                List<Licencia> licencias36AEnAnio = licenciasExistentesAnioPersona.stream()
                                .filter(l -> "36A".equals(l.getArticuloLicencia().getArticulo()) &&
                                                (nuevaLicencia.getId() == 0 || l.getId() != nuevaLicencia.getId()))
                                .collect(Collectors.toList());

                long diasYaTomadosEnAnio = licencias36AEnAnio.stream()
                                .filter(l -> l.getPedidoDesde().getYear() == nuevaLicencia.getPedidoDesde().getYear())
                                .mapToLong(l -> ChronoUnit.DAYS.between(l.getPedidoDesde(), l.getPedidoHasta()) + 1)
                                .sum();

                if (diasYaTomadosEnAnio + diasSolicitados > MAX_DIAS_POR_ANIO) {
                        throw new IllegalArgumentException(
                                        "NO se otorga Licencia artículo 36A a " + nuevaLicencia.getPersona().getNombre()
                                                        + " " +
                                                        nuevaLicencia.getPersona().getApellido() +
                                                        " debido a que supera el tope de " + MAX_DIAS_POR_ANIO
                                                        + " días de licencia por año");
                }
        }
}
