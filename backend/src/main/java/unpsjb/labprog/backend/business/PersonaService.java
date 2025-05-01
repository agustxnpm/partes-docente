package unpsjb.labprog.backend.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import unpsjb.labprog.backend.model.Persona;

@Service
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private MensajeBuilder mensajeBuilder;

    @Autowired
    private Validator validator;

    public Persona findById(Long id) {
        return personaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró una persona con el ID " + id));
    }

    public List<Persona> findAll() {
        List<Persona> result = new ArrayList<>();
        personaRepository.findAll().forEach(e -> result.add(e));
        return result;
    }

    /*
     * public Persona findByNombre(String nombre) {
     * return personaRepository.findByNombre(nombre).orElse(null);
     * }
     */

    public Persona save(Persona persona) {
        validator.validarPersona(persona);
        return personaRepository.save(persona);
    }

    public void deleteByDni(Long dni) {
        validator.validarDni(dni);
        personaRepository.deleteByDni(dni);
    }

    public String getMensajeExito(Persona persona) {
        return mensajeBuilder.generarMensajeExitoPersona(persona);
    }
}
