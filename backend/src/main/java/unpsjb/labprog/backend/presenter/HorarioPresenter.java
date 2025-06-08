package unpsjb.labprog.backend.presenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import unpsjb.labprog.backend.Response;
import unpsjb.labprog.backend.business.interfaces.IHorarioService;
import unpsjb.labprog.backend.model.Horario;

@RestController
@RequestMapping("horarios")
public class HorarioPresenter {

    @Autowired
    private IHorarioService horarioService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> createHorario(@RequestBody Horario horario) {
        try {
            horarioService.save(horario);
            return Response.ok(horario, horarioService.getMensajeExito(horario));
        } catch (DataIntegrityViolationException e) {
            return Response.duplicateError(horario, e.getMessage());
        } catch (IllegalArgumentException e) {
            return Response.notImplemented(horario, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> listarHorarios() {
        return Response.ok(horarioService.findAll());

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateHorario(@RequestBody Horario horario) {
        try {
            horarioService.save(horario);
            return Response.ok(horario, horarioService.getMensajeExitoActualizacion(horario));
        } catch (IllegalArgumentException e) {
            return Response.badRequest(horario, e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteHorario(@PathVariable("id") Long id) {
        try {
            Horario horario = horarioService.findById(id);
            horarioService.delete(horario);
            return Response.ok(horario, horarioService.getMensajeExitoBorrado(horario));
        } catch (IllegalArgumentException e) {
            return Response.badRequest(e, e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getHorarioById(@RequestBody Long id) {
        try {
            Horario horario = horarioService.findById(id);
            return Response.ok(horario);
        } catch (IllegalArgumentException e) {
            return Response.badRequest(id, e.getMessage());
        }
    }
}
