package unpsjb.labprog.backend.presenter;

import java.util.List;

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
import unpsjb.labprog.backend.business.interfaces.mensajes.ICargoMensajeBuilder;
import unpsjb.labprog.backend.business.interfaces.servicios.ICargoService;
import unpsjb.labprog.backend.model.Cargo;

/**
 * Presenter para la gestión de cargos.
 * Aplica el principio DIP (Dependency Inversion Principle)
 */
@RestController
@RequestMapping("cargos")
public class CargoPresenter {

    @Autowired
    private ICargoService cargoService;

    @Autowired
    private ICargoMensajeBuilder cargoMensajeBuilder;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> createCargo(@RequestBody Cargo cargo) {

        try {
            cargoService.save(cargo);
            return Response.ok(cargo, cargoMensajeBuilder.generarMensajeExitoCreacion(cargo));
        } catch (DataIntegrityViolationException e) {
            return Response.duplicateError(cargo, e.getMessage());
        } catch (IllegalArgumentException e) {
            return Response.notImplemented(cargo, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> listarCargos() {
        try {
            return Response.ok(cargoService.findAll());
        } catch (IllegalArgumentException e) {
            return Response.badRequest(e, e.getMessage());
        }
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public ResponseEntity<Object> findByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Response.ok(cargoService.findByPage(page, size));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateCargo(@PathVariable("id") Long id, @RequestBody Cargo cargo) {
        try {
            cargoService.save(cargo);
            return Response.ok(cargo, cargoMensajeBuilder.generarMensajeExitoActualizacion(cargo));
        } catch (IllegalArgumentException e) {
            return Response.badRequest(cargo, e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteCargo(@PathVariable("id") Long id) {
        try {
            Cargo cargo = cargoService.findById(id);
            cargoService.delete(cargo);
            return Response.ok(cargo, cargoMensajeBuilder.generarMensajeExitoBorrado(cargo));
        } catch (IllegalArgumentException e) {
            return Response.badRequest(e, e.getMessage());
        } catch (DataIntegrityViolationException e) {
            return Response.duplicateError(e, "No se puede eliminar el cargo porque esta asociado a una designación");
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getCargoById(@PathVariable("id") Long id) {
        try {
            Cargo cargo = cargoService.findById(id);
            return Response.ok(cargo);
        } catch (IllegalArgumentException e) {
            return Response.badRequest(e, e.getMessage());
        }
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<Object> search(@RequestParam("term") String term) {
        try {
            List<Cargo> cargos = cargoService.search(term);
            return Response.ok(cargos, "Búsqueda completada exitosamente");
        } catch (Exception e) {
            return Response.internalServerError(
                    "Error en la búsqueda: " + e.getMessage(), null);
        }
    }
}
