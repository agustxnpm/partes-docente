package unpsjb.labprog.backend.business.validaciones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import unpsjb.labprog.backend.model.Licencia;
import unpsjb.labprog.backend.model.Persona;
import unpsjb.labprog.backend.business.LicenciaRepository;
import unpsjb.labprog.backend.business.PersonaRepository;
import unpsjb.labprog.backend.business.utilidades.ValidadorArticuloRegistry;
import unpsjb.labprog.backend.business.validaciones.LicenciaValidator;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Component
public class LicenciaValidator {

    @Autowired
    private LicenciaRepository licenciaRepository; // Para buscar licencias existentes

    @Autowired
    private PersonaRepository personaRepository; // Para cargar la persona completa

    @Autowired
    private ValidadorArticuloRegistry validadorRegistry;

    public void validateLicencia(Licencia licencia) throws IllegalArgumentException {

        // Validaciones básicas y generales
        if (licencia == null) {
            throw new IllegalArgumentException("La licencia no puede ser nula.");
        }
        if (licencia.getPersona() == null) {
            throw new IllegalArgumentException("La persona de la licencia no puede ser nula.");
        }
        if (licencia.getArticuloLicencia() == null || licencia.getArticuloLicencia().getArticulo() == null
                || licencia.getArticuloLicencia().getArticulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El artículo de la licencia es inválido o no puede ser nulo.");
        }
        if (licencia.getPedidoDesde() == null || licencia.getPedidoHasta() == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin de la licencia son obligatorias.");
        }
        if (licencia.getPedidoDesde().isAfter(licencia.getPedidoHasta())) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }
        // Cargar la persona completa desde la BD para verificar designaciones
        Persona personaCompleta = personaRepository.findById(licencia.getPersona().getId())
                .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada"));

        // Verificar si la persona tiene algún cargo en la institución
        if (personaCompleta.getDesignaciones() == null || personaCompleta.getDesignaciones().isEmpty()) {
            throw new IllegalArgumentException("NO se otorga Licencia artículo " +
                    licencia.getArticuloLicencia().getArticulo() + " a " +
                    licencia.getPersona().getNombre() + " " +
                    licencia.getPersona().getApellido() +
                    " debido a que el agente no posee ningún cargo en la institución");
        }

        if (licencia.getDesignaciones() == null) {
            licencia.setDesignaciones(Collections.emptyList()); // para evitar NullPointerException
        }

        // Validación: Docente debe tener designación activa que cubra el período
        // solicitado.
        validarDesignacionesActivasParaLicencia(licencia);

        // Obtener licencias existentes para la persona en el año de la solicitud
        List<Licencia> licenciasExistentesAnioPersona = licenciaRepository.findByPersonaAndYear(
                licencia.getPersona(),
                licencia.getPedidoDesde().getYear());

        // 1. Validar superposición con CUALQUIER licencia existente
        for (Licencia existente : licenciasExistentesAnioPersona) {
            if (licencia.getId() != 0 && existente.getId() == licencia.getId()) {
                continue; // No compararse consigo misma en caso de actualización
            }
            // Verifica si hay superposición: (InicioA <= FinB) y (FinA >= InicioB)
            boolean haySuperposicion = !licencia.getPedidoDesde().isAfter(existente.getPedidoHasta()) &&
                    !licencia.getPedidoHasta().isBefore(existente.getPedidoDesde());
            if (haySuperposicion) {
                throw new IllegalArgumentException(
                        "NO se otorga Licencia artículo " + existente.getArticuloLicencia().getArticulo() + " a " +  licencia.getPersona().getNombre() + " " +
                                licencia.getPersona().getApellido()
                                + " debido a que ya posee una licencia en el mismo período");
            }
        }

        String codigoArticulo = licencia.getArticuloLicencia().getArticulo();

        if (validadorRegistry.existeValidador(codigoArticulo)) {
            ArticuloLicenciaValidator validador = validadorRegistry.getValidador(codigoArticulo);
            validador.validate(licencia, licenciasExistentesAnioPersona);
        }
    }

    private void validarDesignacionesActivasParaLicencia(Licencia licencia) {
        // Verificar que cada día del período de licencia esté cubierto por al menos una
        // designación
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
}
