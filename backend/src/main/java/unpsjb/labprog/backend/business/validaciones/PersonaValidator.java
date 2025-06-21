package unpsjb.labprog.backend.business.validaciones;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import unpsjb.labprog.backend.business.interfaces.servicios.ILicenciaService;
import unpsjb.labprog.backend.business.interfaces.servicios.IPersonaService;
import unpsjb.labprog.backend.business.interfaces.validaciones.IPersonaValidator;
import unpsjb.labprog.backend.model.EstadoLicencia;
import unpsjb.labprog.backend.model.Licencia;
import unpsjb.labprog.backend.model.Persona;

/**
 * Validador para operaciones relacionadas con la entidad Persona.
 * 
 * Esta clase implementa el Principio de Inversión de Dependencias (DIP) del SOLID,
 * dependiendo de abstracciones (interfaces) en lugar de clases concretas:
 * - IPersonaService: Interface para operaciones de persona
 * - ILicenciaService: Interface para operaciones de licencia
 * 
 * Esto mejora la testabilidad, flexibilidad y mantenibilidad del código.
 * 
 */
@Component
public class PersonaValidator implements IPersonaValidator {

    /**
     * Servicio de persona inyectado mediante interfaz (DIP).
     * Se usa @Lazy para evitar dependencias circulares.
     */
    @Autowired
    @Lazy
    private IPersonaService personaService;

    /**
     * Servicio de licencia inyectado mediante interfaz (DIP).
     * Se usa @Lazy para evitar dependencias circulares.
     */
    @Autowired
    @Lazy
    private ILicenciaService licenciaService;

    public void validarPersona(Persona persona) {
        validarDni(persona);
        validarNombre(persona);
        validarApellido(persona);
        validarUnicidadDni(persona);
        validarUnicidadCuil(persona);
    }

    /**
     * Valida que el DNI sea un número positivo.
     */
    private void validarDni(Persona persona) {
        if (persona.getDni() <= 0) {
            throw new IllegalArgumentException("El DNI debe ser un número positivo");
        }
    }

    /**
     * Valida que el nombre no esté vacío o nulo.
     */
    private void validarNombre(Persona persona) {
        if (persona.getNombre() == null || persona.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
    }

    /**
     * Valida que el apellido no esté vacío o nulo.
     */
    private void validarApellido(Persona persona) {
        if (persona.getApellido() == null || persona.getApellido().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío");
        }
    }

    /**
     * Valida que no exista otra persona con el mismo DNI.
     */
    private void validarUnicidadDni(Persona persona) {
        Optional<Persona> existeDni = personaService.findByDni(persona.getDni());
        if (existeDni.isPresent() && !esMismaPersona(existeDni.get(), persona)) {
            throw new IllegalArgumentException("Ya existe una persona con el DNI: " + persona.getDni());
        }
    }

    /**
     * Valida que no exista otra persona con el mismo CUIL.
     */
    private void validarUnicidadCuil(Persona persona) {
        Optional<Persona> existeCuil = personaService.findByCuil(persona.getCuil());
        if (existeCuil.isPresent() && !esMismaPersona(existeCuil.get(), persona)) {
            throw new IllegalArgumentException("Ya existe una persona con el CUIL: " + persona.getCuil());
        }
    }

    /**
     * Verifica si dos personas son la misma (mismo ID).
     */
    private boolean esMismaPersona(Persona persona1, Persona persona2) {
        return persona1.getId().equals(persona2.getId());
    }

    public void validarDni(Long dni) {
        Persona persona = personaService.findByDni(dni).orElse(null);
        if (persona == null) {
            throw new IllegalArgumentException("No se encontró una persona con el DNI: " + dni);
        }
    }

    public void validarBorradoPersona(Persona persona) {
        validarDesignacionesAsociadas(persona);
        validarLicenciasAsociadas(persona);
    }

    /**
     * Valida que la persona no tenga designaciones asociadas.
     */
    private void validarDesignacionesAsociadas(Persona persona) {
        if (tieneDesignacionesAsociadas(persona)) {
            String mensajeError = construirMensajeErrorDesignaciones(persona);
            throw new IllegalArgumentException(mensajeError);
        }
    }

    /**
     * Verifica si la persona tiene designaciones asociadas.
     */
    private boolean tieneDesignacionesAsociadas(Persona persona) {
        return persona.getDesignaciones() != null && !persona.getDesignaciones().isEmpty();
    }

    /**
     * Construye el mensaje de error para designaciones asociadas.
     */
    private String construirMensajeErrorDesignaciones(Persona persona) {
        return "No se puede eliminar a " + persona.getNombre() + " " + persona.getApellido()
                + " porque tiene designaciones asociadas.";
    }

    /**
     * Valida que la persona no tenga licencias asociadas.
     */
    private void validarLicenciasAsociadas(Persona persona) {
        List<Licencia> todasLasLicencias = obtenerLicenciasPersona(persona);
        
        if (!todasLasLicencias.isEmpty()) {
            ContadorLicencias contadores = contarLicenciasPorEstado(todasLasLicencias);
            String mensajeError = construirMensajeErrorLicencias(persona, contadores);
            throw new IllegalArgumentException(mensajeError);
        }
    }

    /**
     * Obtiene todas las licencias de la persona.
     */
    private List<Licencia> obtenerLicenciasPersona(Persona persona) {
        return licenciaService.findByPersona(persona);
    }

    /**
     * Cuenta las licencias por estado.
     */
    private ContadorLicencias contarLicenciasPorEstado(List<Licencia> licencias) {
        long licenciasValidas = contarLicenciasPorEstado(licencias, EstadoLicencia.VALIDA);
        long licenciasInvalidas = contarLicenciasPorEstado(licencias, EstadoLicencia.INVALIDA);
        
        return new ContadorLicencias(licenciasValidas, licenciasInvalidas);
    }

    /**
     * Cuenta las licencias que tienen un estado específico.
     */
    private long contarLicenciasPorEstado(List<Licencia> licencias, EstadoLicencia estado) {
        return licencias.stream()
                .filter(l -> l.getEstado() == estado)
                .count();
    }

    /**
     * Construye el mensaje de error para licencias asociadas.
     */
    private String construirMensajeErrorLicencias(Persona persona, ContadorLicencias contadores) {
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("No se puede eliminar a ").append(persona.getNombre())
                .append(" ").append(persona.getApellido()).append(" porque tiene ");

        if (contadores.tieneAmbosEstados()) {
            mensaje.append(contadores.getLicenciasValidas()).append(" licencia(s) otorgada(s) y ")
                    .append(contadores.getLicenciasInvalidas()).append(" licencia(s) en proceso.");
        } else if (contadores.tieneLicenciasValidas()) {
            mensaje.append(contadores.getLicenciasValidas()).append(" licencia(s) otorgada(s).");
        } else {
            mensaje.append(contadores.getLicenciasInvalidas()).append(" licencia(s) en proceso de validación.");
        }

        return mensaje.toString();
    }

    /**
     * Clase interna para encapsular los contadores de licencias por estado.
     */
    private static class ContadorLicencias {
        private final long licenciasValidas;
        private final long licenciasInvalidas;

        public ContadorLicencias(long licenciasValidas, long licenciasInvalidas) {
            this.licenciasValidas = licenciasValidas;
            this.licenciasInvalidas = licenciasInvalidas;
        }

        public long getLicenciasValidas() {
            return licenciasValidas;
        }

        public long getLicenciasInvalidas() {
            return licenciasInvalidas;
        }

        public boolean tieneAmbosEstados() {
            return licenciasValidas > 0 && licenciasInvalidas > 0;
        }

        public boolean tieneLicenciasValidas() {
            return licenciasValidas > 0;
        }
    }
}
