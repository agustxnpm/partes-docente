package unpsjb.labprog.backend.business.validaciones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import unpsjb.labprog.backend.model.Licencia;
// Asegúrate de que Designacion y Persona estén importados si usas getNombreCompleto o accedes a sus propiedades.
import unpsjb.labprog.backend.model.Designacion;
import unpsjb.labprog.backend.business.LicenciaRepository;
import unpsjb.labprog.backend.business.utilidades.ValidadorArticuloRegistry;
import unpsjb.labprog.backend.business.validaciones.LicenciaValidator;
import java.util.List;

@Component
public class LicenciaValidator {

    @Autowired
    private LicenciaRepository licenciaRepository; // Para buscar licencias existentes

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
          // Verificar si la persona tiene algún cargo en la institución
        if (licencia.getPersona().getDesignaciones() == null || licencia.getPersona().getDesignaciones().isEmpty()) {
            throw new IllegalArgumentException("NO se otorga Licencia artículo " +
                    licencia.getArticuloLicencia().getArticulo() + " a " +
                    licencia.getPersona().getNombre() + " " +
                    licencia.getPersona().getApellido() +
                    " debido a que el agente no posee ningún cargo en la institución");
        }

        if (licencia.getDesignaciones() == null || licencia.getDesignaciones().isEmpty()) {
            throw new IllegalArgumentException("La licencia debe estar asociada al menos a una designación.");
        }

      

        // Validación: Docente debe tener designación activa que cubra el período
        // solicitado.
        validarDesignacionesActivasParaLicencia(licencia);

        // Obtener licencias existentes para la persona en el año de la solicitud
        List<Licencia> licenciasExistentesAnioPersona = licenciaRepository.findByPersonaAndYear(
                licencia.getPersona(),
                licencia.getPedidoDesde().getYear());
 

        String codigoArticulo = licencia.getArticuloLicencia().getArticulo();
        
         if (validadorRegistry.existeValidador(codigoArticulo)) {
            ArticuloLicenciaValidator validador = validadorRegistry.getValidador(codigoArticulo);
            validador.validate(licencia, licenciasExistentesAnioPersona);
        }
    }

    private void validarDesignacionesActivasParaLicencia(Licencia licencia) {
        boolean designacionValidaEncontrada = false;
        for (Designacion designacion : licencia.getDesignaciones()) {
            // Asumimos que 'designacion' tiene fechaInicio y fechaFin (puede ser null)
            // La licencia debe estar completamente contenida dentro del período de la
            // designación.
            if (designacion.getFechaInicio() != null &&
                    !licencia.getPedidoDesde().isBefore(designacion.getFechaInicio()) &&
                    (designacion.getFechaFin() == null
                            || !licencia.getPedidoHasta().isAfter(designacion.getFechaFin()))) {
                designacionValidaEncontrada = true;
                break;
            }
        }

        if (!designacionValidaEncontrada) {
            // Mensaje de ejemplo del feature: "NO se otorga Licencia artículo 36A a Marisa
            // Balaguer debido a que el agente no tiene designación ese día en la
            // institución"
            throw new IllegalArgumentException("NO se otorga Licencia artículo " +
                    licencia.getArticuloLicencia().getArticulo() + " a " +
                    licencia.getPersona().getNombre() + " " +
                    licencia.getPersona().getApellido() +
                    " debido a que el agente no tiene designación ese día en la institución"
                    );
        }

    }
}