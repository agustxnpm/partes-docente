package unpsjb.labprog.backend.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import unpsjb.labprog.backend.business.interfaces.servicios.IPersonaService;
import unpsjb.labprog.backend.business.interfaces.validaciones.IPersonaValidator;
import unpsjb.labprog.backend.model.Persona;

/**
 * Implementación del servicio de personas.
 * Aplica el principio DIP (Dependency Inversion Principle) dependiendo de abstracciones
 * en lugar de implementaciones concretas.
 * 
 */
@Service
public class PersonaService implements IPersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    // Aplicando DIP e ISP: Dependemos de la abstracción específica IPersonaValidator
    @Autowired
    private IPersonaValidator personaValidator;

    public Page<Persona> findByPage(int page, int size) {
        return personaRepository.findAll(
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
    }

    public List<Persona> findAll() {
        List<Persona> result = new ArrayList<>();
        personaRepository.findAll().forEach(e -> result.add(e));
        return result;
    }

    @Transactional
    public Persona save(Persona persona) {
        personaValidator.validarPersona(persona);
        return personaRepository.save(persona);
    }

    @Transactional
    public Persona update(Persona personaActualizada) {
        personaValidator.validarPersona(personaActualizada);
        Persona personaExistente = findById(personaActualizada.getId());
        actualizarCamposPersona(personaExistente, personaActualizada);
        return personaRepository.save(personaExistente);
    }


    /**
     * Actualiza todos los campos de la persona existente con los valores de la persona actualizada
     */
    private void actualizarCamposPersona(Persona personaExistente, Persona personaActualizada) {
        personaExistente.setNombre(personaActualizada.getNombre());
        personaExistente.setApellido(personaActualizada.getApellido());
        personaExistente.setDni(personaActualizada.getDni());
        personaExistente.setCuil(personaActualizada.getCuil());
        personaExistente.setTitulo(personaActualizada.getTitulo());
        personaExistente.setSexo(personaActualizada.getSexo());
        personaExistente.setDomicilio(personaActualizada.getDomicilio());
        personaExistente.setTelefono(personaActualizada.getTelefono());
    }


    @Transactional
    public void delete(Persona persona) {
        personaValidator.validarBorradoPersona(persona);
        personaRepository.delete(persona);
    }

    public Persona findById(Long id) {
        return personaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada"));
    }

    public Optional<Persona> findByDni(Long dni) {
        return personaRepository.findByDni(dni);
    }

    public Optional<Persona> findByCuil(String cuil) {
        return personaRepository.findByCuil(cuil);
    }

    public List<Persona> search(String term) {
        return personaRepository.search(term.trim());
    }

}
