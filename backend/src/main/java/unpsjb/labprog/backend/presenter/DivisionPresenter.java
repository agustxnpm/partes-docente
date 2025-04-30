package unpsjb.labprog.backend.presenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
            return Response.ok("División " + division.getAnio() + "º" + " " + division.getNumDivision() + "º turno " + division.getTurno() + " ingresada correctamente");
        } catch (IllegalArgumentException e) {
            return Response.badRequest(division, e.getMessage());
        }
    }
}
