package unpsjb.labprog.backend.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import unpsjb.labprog.backend.model.Persona;

@Service
public class PersonaService  {
   
    @Autowired
    private PersonaRepository personaRepository;

    public Persona findById(Long id) {
        return personaRepository.findById(id).orElseThrow(() -> 
            new IllegalArgumentException("No se encontró una persona con el ID " + id));
    }

    public List<Persona> findAll() {
        List<Persona> result = new ArrayList<>();
        personaRepository.findAll().forEach(e -> result.add(e));
        return result;
    }

    /*public Persona findByNombre(String nombre) {
        return personaRepository.findByNombre(nombre).orElse(null);
    }*/

    public Persona save(Persona persona) {
        if (existsByDni(persona.getDni())) {
            throw new IllegalArgumentException("Ya existe una persona con el DNI: " + persona.getDni());
        }
        return personaRepository.save(persona);
    }

    public void deleteByDni(Long dni) {
        if (!existsByDni(dni)) {
            throw new IllegalArgumentException("No se encontró una persona con el DNI " + dni);
        }
        personaRepository.deleteByDni(dni);
    }

    private boolean existsByDni(long dni) {
        return personaRepository.findByDni(dni).isPresent();
    }
    
}
