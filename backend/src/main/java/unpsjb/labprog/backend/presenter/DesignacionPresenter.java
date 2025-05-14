package unpsjb.labprog.backend.presenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import unpsjb.labprog.backend.Response;
import unpsjb.labprog.backend.business.DesignacionService;
import unpsjb.labprog.backend.model.Designacion;

@RestController
@RequestMapping("designaciones")
public class DesignacionPresenter {

    @Autowired
    private DesignacionService designacionService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> designarCargo(@RequestBody Designacion designacion) {

        try {
            designacionService.save(designacion);
            return Response.ok(designacion,
                    designacionService.getMensajeExito(designacion));
        } catch (IllegalArgumentException e) {
            return Response.badRequest(designacion, e.getMessage());
        }
    }

    
}
