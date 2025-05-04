package unpsjb.labprog.backend.business;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import unpsjb.labprog.backend.model.Persona;

@Component
public class PersonaValidator {

    @Autowired
    private PersonaRepository personaRepository;

    public void validarPersona(Persona persona) {
        if (persona.getDni() <= 0)
            throw new IllegalArgumentException("El DNI debe ser un número positivo");

        if (persona.getNombre() == null || persona.getNombre().isEmpty())
            throw new IllegalArgumentException("El nombre no puede estar vacío");

        if (persona.getApellido() == null || persona.getApellido().isEmpty())
            throw new IllegalArgumentException("El apellido no puede estar vacío");

        Optional<Persona> existeDni = personaRepository.findByDni(persona.getDni());
        if (existeDni.isPresent() && !existeDni.get().getId().equals(persona.getId())) {
            throw new IllegalArgumentException("Ya existe una persona con el DNI: " + persona.getDni());
        }

        Optional<Persona> existeCuil = personaRepository.findByCuil(persona.getCuil());
        if (existeCuil.isPresent() && !existeCuil.get().getId().equals(persona.getId())) {
            throw new IllegalArgumentException("Ya existe una persona con el CUIL: " + persona.getCuil());
        }
    }

    public void validarDni(Long dni) {
        Persona persona = personaRepository.findByDni(dni).orElse(null);
        if (persona == null) {
            throw new IllegalArgumentException("No se encontró una persona con el DNI: " + dni);
        }
    }
}
