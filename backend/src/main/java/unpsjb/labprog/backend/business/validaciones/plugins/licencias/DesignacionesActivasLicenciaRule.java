package unpsjb.labprog.backend.business.validaciones.plugins.licencias;

import java.time.LocalDate;


import unpsjb.labprog.backend.business.interfaces.validaciones.ILicenciaRule;
import unpsjb.labprog.backend.model.Licencia;

/**
 * Regla de validación para verificar que la persona tenga designaciones activas
 * durante todo el período de la licencia solicitada.
 */
public class DesignacionesActivasLicenciaRule implements ILicenciaRule {
    private static DesignacionesActivasLicenciaRule instance;

    private DesignacionesActivasLicenciaRule() {}

    public static DesignacionesActivasLicenciaRule getInstance() {
        if (instance == null) {
            instance = new DesignacionesActivasLicenciaRule();
        }
        return instance;
    }

    @Override
    public void validate(Licencia licencia) {
        validarDesignacionesActivasParaLicencia(licencia);
    }

    private void validarDesignacionesActivasParaLicencia(Licencia licencia) {
        // Verificar que cada día del período de licencia esté cubierto por al menos una designación
        LocalDate currentDate = licencia.getPedidoDesde();

        while (!currentDate.isAfter(licencia.getPedidoHasta())) {
            final LocalDate date = currentDate;
            boolean isDayCovered = licencia.getDesignaciones().stream()
                    .anyMatch(d -> !date.isBefore(d.getFechaInicio()) &&
                            (d.getFechaFin() == null || !date.isAfter(d.getFechaFin())));

            if (!isDayCovered) {
                throw new IllegalArgumentException("NO se otorga Licencia artículo " +
                        licencia.getArticuloLicencia().getArticulo() + " a " +
                        licencia.getPersona().getNombre() + " " +
                        licencia.getPersona().getApellido() +
                        " debido a que el agente no tiene designación ese día en la institución");
            }

            currentDate = currentDate.plusDays(1);
        }
    }

    @Override
    public String getRuleName() {
        return "Validación de Designaciones Activas para Licencia";
    }
}
