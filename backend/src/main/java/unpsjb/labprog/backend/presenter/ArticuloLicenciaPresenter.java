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
import unpsjb.labprog.backend.business.ArticuloLicenciaService;
import unpsjb.labprog.backend.model.ArticuloLicencia;

@RestController
@RequestMapping("articulos-licencias")
public class ArticuloLicenciaPresenter {

    @Autowired
    private ArticuloLicenciaService articuloLicenciaService;

    /*
     * @RequestMapping(method = RequestMethod.POST)
     * public ResponseEntity<Object> createArticuloLicencia(@RequestBody
     * ArticuloLicencia articuloLicencia) {
     * try {
     * articuloLicenciaService.save(articuloLicencia);
     * return Response.ok(articuloLicencia,
     * articuloLicenciaService.getMensajeExito(articuloLicencia));
     * } catch (DataIntegrityViolationException e) {
     * return Response.duplicateError(articuloLicencia, e.getMessage());
     * } catch (IllegalArgumentException e) {
     * return Response.notImplemented(articuloLicencia, e.getMessage());
     * }
     * }
     */

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> listarArticulosLicencias() {
        return Response.ok(articuloLicenciaService.findAll());
    }

    /*
     * @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
     * public ResponseEntity<Object> updateArticuloLicencia(@RequestBody
     * ArticuloLicencia articuloLicencia) {
     * try {
     * articuloLicenciaService.save(articuloLicencia);
     * return Response.ok(articuloLicencia,
     * articuloLicenciaService.getMensajeExitoActualizacion(articuloLicencia));
     * } catch (IllegalArgumentException e) {
     * return Response.badRequest(articuloLicencia, e.getMessage());
     * }
     * }
     */

    /*
     * @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
     * public ResponseEntity<Object> deleteArticuloLicencia(@PathVariable("id") Long
     * id) {
     * try {
     * ArticuloLicencia articuloLicencia = articuloLicenciaService.findById(id);
     * articuloLicenciaService.delete(articuloLicencia);
     * return Response.ok(articuloLicencia,
     * articuloLicenciaService.getMensajeExitoBorrado(articuloLicencia));
     * } catch (IllegalArgumentException e) {
     * return Response.badRequest(e, e.getMessage());
     * }
     * }
     */

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getArticuloLicenciaById(@PathVariable("id") Long id) {
        try {
            ArticuloLicencia articuloLicencia = articuloLicenciaService.findById(id);
            return Response.ok(articuloLicencia);
        } catch (IllegalArgumentException e) {
            return Response.badRequest(e, e.getMessage());
        }
    }
}
