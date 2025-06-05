package unpsjb.labprog.backend.business.validaciones;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import unpsjb.labprog.backend.business.LicenciaService;
import unpsjb.labprog.backend.business.PersonaService;
import unpsjb.labprog.backend.model.EstadoLicencia;
import unpsjb.labprog.backend.model.Licencia;
import unpsjb.labprog.backend.model.Persona;

@Component
public class PersonaValidator {

    @Autowired
    @Lazy
    private PersonaService personaService;

    @Autowired
    @Lazy
    private LicenciaService licenciaService;

    public void validarPersona(Persona persona) {
        if (persona.getDni() <= 0)
            throw new IllegalArgumentException("El DNI debe ser un número positivo");

        if (persona.getNombre() == null || persona.getNombre().isEmpty())
            throw new IllegalArgumentException("El nombre no puede estar vacío");

        if (persona.getApellido() == null || persona.getApellido().isEmpty())
            throw new IllegalArgumentException("El apellido no puede estar vacío");

        Optional<Persona> existeDni = personaService.findByDni(persona.getDni());
        if (existeDni.isPresent() && !existeDni.get().getId().equals(persona.getId())) {
            throw new IllegalArgumentException("Ya existe una persona con el DNI: " + persona.getDni());
        }

        Optional<Persona> existeCuil = personaService.findByCuil(persona.getCuil());
        if (existeCuil.isPresent() && !existeCuil.get().getId().equals(persona.getId())) {
            throw new IllegalArgumentException("Ya existe una persona con el CUIL: " + persona.getCuil());
        }
    }

    public void validarDni(Long dni) {
        Persona persona = personaService.findByDni(dni).orElse(null);
        if (persona == null) {
            throw new IllegalArgumentException("No se encontró una persona con el DNI: " + dni);
        }
    }

    public void validarBorrado(Persona persona) {
        if (persona.getDesignaciones() != null && !persona.getDesignaciones().isEmpty()) {
            throw new IllegalArgumentException(
                    "No se puede eliminar a " + persona.getNombre() + " " + persona.getApellido()
                            + " porque tiene designaciones asociadas.");
        }

        // Obtener todas las licencias de la persona
        List<Licencia> todasLasLicencias = licenciaService.findByPersona(persona);

        if (!todasLasLicencias.isEmpty()) {
            long licenciasValidas = todasLasLicencias.stream()
                    .filter(l -> l.getEstado() == EstadoLicencia.VALIDA)
                    .count();

            long licenciasInvalidas = todasLasLicencias.stream()
                    .filter(l -> l.getEstado() == EstadoLicencia.INVALIDA)
                    .count();

            StringBuilder mensaje = new StringBuilder();
            mensaje.append("No se puede eliminar a ").append(persona.getNombre())
                    .append(" ").append(persona.getApellido()).append(" porque tiene ");

            if (licenciasValidas > 0 && licenciasInvalidas > 0) {
                mensaje.append(licenciasValidas).append(" licencia(s) otorgada(s) y ")
                        .append(licenciasInvalidas).append(" licencia(s) en proceso.");
            } else if (licenciasValidas > 0) {
                mensaje.append(licenciasValidas).append(" licencia(s) otorgada(s).");
            } else {
                mensaje.append(licenciasInvalidas).append(" licencia(s) en proceso de validación.");
            }

            throw new IllegalArgumentException(mensaje.toString());
        }
    }
}
