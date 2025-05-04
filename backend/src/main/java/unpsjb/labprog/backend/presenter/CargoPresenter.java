package unpsjb.labprog.backend.presenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import unpsjb.labprog.backend.Response;
import unpsjb.labprog.backend.business.CargoService;
import unpsjb.labprog.backend.model.Cargo;

@RestController
@RequestMapping("cargos")
public class CargoPresenter {

    @Autowired
    private CargoService cargoService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> createCargo(@RequestBody Cargo cargo) {

        try {
            cargoService.save(cargo);
            return Response.ok(cargoService.getMensajeExito(cargo));
        } catch (DataIntegrityViolationException e) {
            return Response.duplicateError(cargo, "El cargo ya existe");
        } catch (IllegalArgumentException e) {
            return Response.notImplemented(e.getMessage(), null);
        }
    }


}
