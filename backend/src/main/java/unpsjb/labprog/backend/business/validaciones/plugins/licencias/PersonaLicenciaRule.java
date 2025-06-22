package unpsjb.labprog.backend.business.validaciones.plugins.licencias;

import java.util.Collections;

import unpsjb.labprog.backend.business.interfaces.servicios.IPersonaService;
import unpsjb.labprog.backend.business.interfaces.validaciones.ILicenciaRule;
import unpsjb.labprog.backend.model.Licencia;
import unpsjb.labprog.backend.model.Persona;

/**
 * Regla de validación para verificar que la persona esté habilitada para licencias.
 * Verifica que la persona exista y tenga cargos en la institución.
 */
public class PersonaLicenciaRule implements ILicenciaRule {
    private static PersonaLicenciaRule instance;
    private IPersonaService personaService;

    private PersonaLicenciaRule() {}

    public static PersonaLicenciaRule getInstance() {
        if (instance == null) {
            instance = new PersonaLicenciaRule();
        }
        return instance;
    }

    public void setPersonaService(IPersonaService personaService) {
        this.personaService = personaService;
    }

    @Override
    public void validate(Licencia licencia) {
        // Cargar la persona completa desde la BD para verificar designaciones
        Persona personaCompleta = personaService.findById(licencia.getPersona().getId());

        if (personaCompleta == null) {
            throw new IllegalArgumentException("Persona no encontrada");
        }

        // Verificar si la persona tiene algún cargo en la institución
        if (personaCompleta.getDesignaciones() == null || personaCompleta.getDesignaciones().isEmpty()) {
            throw new IllegalArgumentException("NO se otorga Licencia artículo " +
                    licencia.getArticuloLicencia().getArticulo() + " a " +
                    licencia.getPersona().getNombre() + " " +
                    licencia.getPersona().getApellido() +
                    " debido a que el agente no posee ningún cargo en la institución");
        }

        // Inicializar designaciones si es null para evitar NullPointerException en validaciones posteriores
        if (licencia.getDesignaciones() == null) {
            licencia.setDesignaciones(Collections.emptyList());
        }
    }

    @Override
    public String getRuleName() {
        return "Validación de Persona para Licencia";
    }
}
