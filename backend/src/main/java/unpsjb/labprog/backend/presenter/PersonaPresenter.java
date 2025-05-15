package unpsjb.labprog.backend.presenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import unpsjb.labprog.backend.Response;
import unpsjb.labprog.backend.business.PersonaService;
import unpsjb.labprog.backend.model.Persona;

@RestController
@RequestMapping("personas")
public class PersonaPresenter {

    @Autowired
    private PersonaService personaService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> createPersona(@RequestBody Persona persona) {

        try {
            personaService.save(persona);
            return Response.ok(persona, personaService.getMensajeExito(persona));
        } catch (IllegalArgumentException e) {
            return Response.badRequest(persona, e.getMessage());
        }

    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> listarPersonas() {
        try {
            return Response.ok(personaService.findAll());
        } catch (IllegalArgumentException e) {
            return Response.badRequest(null, e.getMessage());
        }
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public ResponseEntity<Object> findByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Response.ok(personaService.findByPage(page, size));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updatePersona(@PathVariable("id") Long id, @RequestBody Persona persona) {
        try {
            persona.setId(id);
            Persona updatedPersona = personaService.update(persona);
            return Response.ok(updatedPersona, personaService.getMensajeExitoActualizacion(updatedPersona));
        } catch (IllegalArgumentException e) {
            return Response.badRequest(persona, e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deletePersona(@PathVariable("id") Long id) {
        try {
            Persona persona = personaService.findById(id);
            personaService.delete(persona);
            return Response.ok(persona, personaService.getMensajeExitoBorrado(persona));
        } catch (IllegalArgumentException e) {
            return Response.badRequest(null, e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getPersonaById(@PathVariable("id") Long id) {
        try {
            Persona persona = personaService.findById(id);
            return Response.ok(persona);
        } catch (IllegalArgumentException e) {
            return Response.badRequest(null, e.getMessage());
        }
    }

}
