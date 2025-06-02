package unpsjb.labprog.backend.presenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> listarArticulosLicencias() {
        return Response.ok(articuloLicenciaService.findAll());
    }

    @RequestMapping(value = "/codigo/{codigo}", method = RequestMethod.GET)
    public ResponseEntity<Object> getArticuloLicenciaByCodigo(@PathVariable("codigo") String codigo) {
        try {
            ArticuloLicencia articuloLicencia = articuloLicenciaService.findByArticulo(codigo);
            return Response.ok(articuloLicencia);
        } catch (IllegalArgumentException e) {
            return Response.badRequest(e, e.getMessage());
        }
    }
}
