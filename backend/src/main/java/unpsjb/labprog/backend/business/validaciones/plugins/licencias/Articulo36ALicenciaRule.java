package unpsjb.labprog.backend.business.validaciones.plugins.licencias;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import unpsjb.labprog.backend.business.interfaces.servicios.ILicenciaService;
import unpsjb.labprog.backend.business.interfaces.validaciones.ILicenciaRule;
import unpsjb.labprog.backend.model.Licencia;

/**
 * Validador para el artículo 36A de licencias.
 * Este validador asegura que las licencias del artículo 36A cumplan con las
 * restricciones
 * de límite mensual y anual de días.
 * Según la normativa, se permite un máximo de 2 días por mes y 6 días por año
 * para este artículo.
 * Si se superan estos límites, se lanza una excepción IllegalArgumentException.
 */
public class Articulo36ALicenciaRule implements ILicenciaRule {

        private static final int MAX_DIAS_POR_MES = 2;
        private static final int MAX_DIAS_POR_ANIO = 6;

        private static Articulo36ALicenciaRule instance;

        private ILicenciaService licenciaService;

        private Articulo36ALicenciaRule() {
        }

        public static Articulo36ALicenciaRule getInstance() {
                if (instance == null) {
                        instance = new Articulo36ALicenciaRule();
                }
                return instance;
        }

        public void setLicenciaService(ILicenciaService licenciaService) {
                this.licenciaService = licenciaService;
        }

        @Override
        public void validate(Licencia nuevaLicencia)
                        throws IllegalArgumentException {
                // Solo aplicar validaciones si es artículo 36A
                if (!"36A".equals(nuevaLicencia.getArticuloLicencia().getArticulo())) {
                        return; // No aplica a este artículo
                }
                
                long diasSolicitados = calcularDiasSolicitados(nuevaLicencia);

                validarTopeMensual(nuevaLicencia, buscarLicenciasExistentesPorAnioYPersona(nuevaLicencia), diasSolicitados);
                validarTopeAnual(nuevaLicencia, buscarLicenciasExistentesPorAnioYPersona(nuevaLicencia), diasSolicitados);
        }

        /**
         * Busca las licencias existentes de la persona en el año de la solicitud.
         */
        private List<Licencia> buscarLicenciasExistentesPorAnioYPersona(Licencia licencia) {
                return licenciaService.findByPersonaAndYear(
                                licencia.getPersona(),
                                licencia.getPedidoDesde().getYear());
        }

        /**
         * Calcula la cantidad de días solicitados en la nueva licencia.
         */
        private long calcularDiasSolicitados(Licencia nuevaLicencia) {
                return ChronoUnit.DAYS.between(nuevaLicencia.getPedidoDesde(), nuevaLicencia.getPedidoHasta()) + 1;
        }

        /**
         * Valida que la nueva licencia no supere el tope mensual de días permitidos.
         */
        private void validarTopeMensual(Licencia nuevaLicencia, List<Licencia> licenciasExistentesAnioPersona,
                        long diasSolicitados) throws IllegalArgumentException {
                List<Licencia> licencias36AEnMes = filtrarLicenciasPorMes(nuevaLicencia,
                                licenciasExistentesAnioPersona);
                long diasYaTomadosEnMes = calcularDiasTomados(licencias36AEnMes);

                if (diasYaTomadosEnMes + diasSolicitados > MAX_DIAS_POR_MES) {
                        throw new IllegalArgumentException(crearMensajeErrorMensual(nuevaLicencia));
                }
        }

        /**
         * Valida que la nueva licencia no supere el tope anual de días permitidos.
         */
        private void validarTopeAnual(Licencia nuevaLicencia, List<Licencia> licenciasExistentesAnioPersona,
                        long diasSolicitados) throws IllegalArgumentException {
                List<Licencia> licencias36AEnAnio = filtrarLicenciasPorAnio(nuevaLicencia,
                                licenciasExistentesAnioPersona);
                long diasYaTomadosEnAnio = calcularDiasTomados(licencias36AEnAnio);

                if (diasYaTomadosEnAnio + diasSolicitados > MAX_DIAS_POR_ANIO) {
                        throw new IllegalArgumentException(crearMensajeErrorAnual(nuevaLicencia));
                }
        }

        /**
         * Filtra las licencias del artículo 36A correspondientes al mismo mes de la
         * nueva licencia.
         */
        private List<Licencia> filtrarLicenciasPorMes(Licencia nuevaLicencia, List<Licencia> licenciasExistentes) {
                return licenciasExistentes.stream()
                                .filter(l -> "36A".equals(l.getArticuloLicencia().getArticulo()) &&
                                                (nuevaLicencia.getId() == 0 || l.getId() != nuevaLicencia.getId()) &&
                                                l.getPedidoDesde().getYear() == nuevaLicencia.getPedidoDesde().getYear()
                                                &&
                                                l.getPedidoDesde().getMonth() == nuevaLicencia.getPedidoDesde()
                                                                .getMonth())
                                .collect(Collectors.toList());
        }

        /**
         * Filtra las licencias del artículo 36A correspondientes al mismo año de la
         * nueva licencia.
         */
        private List<Licencia> filtrarLicenciasPorAnio(Licencia nuevaLicencia, List<Licencia> licenciasExistentes) {
                return licenciasExistentes.stream()
                                .filter(l -> "36A".equals(l.getArticuloLicencia().getArticulo()) &&
                                                (nuevaLicencia.getId() == 0 || l.getId() != nuevaLicencia.getId()) &&
                                                l.getPedidoDesde().getYear() == nuevaLicencia.getPedidoDesde()
                                                                .getYear())
                                .collect(Collectors.toList());
        }

        /**
         * Calcula el total de días tomados en una lista de licencias.
         */
        private long calcularDiasTomados(List<Licencia> licencias) {
                return licencias.stream()
                                .mapToLong(l -> ChronoUnit.DAYS.between(l.getPedidoDesde(), l.getPedidoHasta()) + 1)
                                .sum();
        }

        /**
         * Crea el mensaje de error para cuando se supera el tope mensual.
         */
        private String crearMensajeErrorMensual(Licencia nuevaLicencia) {
                return "NO se otorga Licencia artículo 36A a " + nuevaLicencia.getPersona().getNombre()
                                + " " + nuevaLicencia.getPersona().getApellido() +
                                " debido a que supera el tope de " + MAX_DIAS_POR_MES
                                + " días de licencia por mes";
        }

        /**
         * Crea el mensaje de error para cuando se supera el tope anual.
         */
        private String crearMensajeErrorAnual(Licencia nuevaLicencia) {
                return "NO se otorga Licencia artículo 36A a " + nuevaLicencia.getPersona().getNombre()
                                + " " + nuevaLicencia.getPersona().getApellido() +
                                " debido a que supera el tope de " + MAX_DIAS_POR_ANIO
                                + " días de licencia por año";
        }

        @Override
        public String getRuleName() {
                return "Validación de Licencia Artículo 36A";
        }
}
