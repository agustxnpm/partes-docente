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
import unpsjb.labprog.backend.business.utilidades.MensajeBuilder;
import unpsjb.labprog.backend.business.validaciones.Validator;
import unpsjb.labprog.backend.model.Persona;

@Service
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private MensajeBuilder mensajeBuilder;

    @Autowired
    private Validator validator;

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
        validator.validarPersona(persona);
        return personaRepository.save(persona);
    }

    @Transactional
    public Persona update(Persona personaActualizada) {
        validator.validarPersona(personaActualizada);
        // Obtener la persona existente con todas sus designaciones
        Persona personaExistente = findById(personaActualizada.getId());

        // Actualizar los campos simples
        personaExistente.setNombre(personaActualizada.getNombre());
        personaExistente.setApellido(personaActualizada.getApellido());
        personaExistente.setDni(personaActualizada.getDni());
        personaExistente.setCuil(personaActualizada.getCuil());
        personaExistente.setTitulo(personaActualizada.getTitulo());
        personaExistente.setSexo(personaActualizada.getSexo());
        personaExistente.setDomicilio(personaActualizada.getDomicilio());
        personaExistente.setTelefono(personaActualizada.getTelefono());

        // sin modificar las designaciones
        // personaExistente.setDesignaciones(...)

        return personaRepository.save(personaExistente);
    }

    @Transactional
    public void delete(Persona persona) {
        validator.validarBorradoPersona(persona);
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

    public String getMensajeExito(Persona persona) {
        return mensajeBuilder.generarMensajeExitoPersonaCreada(persona);
    }

    public String getMensajeExitoActualizacion(Persona persona) {
        return mensajeBuilder.generarMensajeExitoPersonaActualizada(persona);
    }

    public String getMensajeExitoBorrado(Persona persona) {
        return mensajeBuilder.generarMensajeExitoPersonaBorrada(persona);
    }

}
