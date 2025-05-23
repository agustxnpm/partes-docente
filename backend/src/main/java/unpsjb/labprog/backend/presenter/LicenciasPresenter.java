package unpsjb.labprog.backend.presenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import unpsjb.labprog.backend.Response;
import unpsjb.labprog.backend.business.LicenciaService;
import unpsjb.labprog.backend.model.Licencia;

@RestController
@RequestMapping("licencias")
public class LicenciasPresenter {

    @Autowired
    private LicenciaService licenciaService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> createLicencia(@RequestBody Licencia licencia) {
        try {
            licenciaService.createLicencia(licencia);
            return Response.ok(licencia, licenciaService.getMensajeExitoLicenciaOtorgada(licencia));
        } catch (IllegalArgumentException e) {
            return Response.internalServerError(licencia, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> listarLicencias() {
        return Response.ok(licenciaService.getAllLicencias());

    }
}
