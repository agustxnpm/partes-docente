package unpsjb.labprog.backend.presenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
            return Response.ok(personaService.getMensajeExito(persona));
        } catch (IllegalArgumentException e) {
            return Response.badRequest(persona, e.getMessage());    
        }

    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteAllPersonas() {
        personaService.deleteAll();
        return Response.ok("Personas eliminadas correctamente");
    }

}
