package unpsjb.labprog.backend.presenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import unpsjb.labprog.backend.Response;
import unpsjb.labprog.backend.business.DivisionService;
import unpsjb.labprog.backend.model.Division;

@RestController
@RequestMapping("divisiones")
public class DivisionPresenter {

    @Autowired
    private DivisionService divisionService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> createDivision(@RequestBody Division division) {
        try {
            divisionService.save(division);
            return Response.ok(division, divisionService.getMensajeExito(division));
        } catch (DataIntegrityViolationException e) {
            return Response.duplicateError(division, divisionService.getMensajeDivisionDuplicada(division));
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> listarDivisiones() {
        try {
            return Response.ok(divisionService.findAll());
        } catch (IllegalArgumentException e) {
            return Response.badRequest(e, e.getMessage());
        }
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public ResponseEntity<Object> findByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Response.ok(divisionService.findByPage(page, size));
    }

     @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateDivision(@PathVariable("id") Long id, @RequestBody Division division) {
        try {
            divisionService.save(division);
            return Response.ok(division, divisionService.getMensajeExitoActualizacion(division));
        } catch (IllegalArgumentException e) {
            return Response.badRequest(division, e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteDivision(@PathVariable("id") Long id) {
        try {
            Division division = divisionService.findById(id);
            divisionService.delete(division);
            return Response.ok(division, divisionService.getMensajeExitoBorrado(division));
        } catch (IllegalArgumentException e) {
            return Response.badRequest(null, e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getDivisionById(@PathVariable("id") Long id) {
        try {
            Division division = divisionService.findById(id);
            return Response.ok(division);
        } catch (IllegalArgumentException e) {
            return Response.badRequest(e, e.getMessage());
        }
    }

}
