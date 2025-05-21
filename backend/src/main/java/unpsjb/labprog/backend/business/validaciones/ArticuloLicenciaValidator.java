package unpsjb.labprog.backend.business.validaciones;

import java.util.List;

import unpsjb.labprog.backend.model.Licencia;

public interface ArticuloLicenciaValidator {
    /**
     * Valida una nueva solicitud de licencia contra las reglas específicas del
     * artículo
     * y las licencias existentes de la persona.
     *
     * @param nuevaLicencia                  La nueva licencia que se está
     *                                       solicitando.
     * @param licenciasExistentesAnioPersona Lista de todas las licencias existentes
     *                                       para la persona en el año de la
     *                                       solicitud.
     * @throws IllegalArgumentException Si la validación falla.
     */
    void validate(Licencia nuevaLicencia, List<Licencia> licenciasExistentesAnioPersona)
            throws IllegalArgumentException;
}
