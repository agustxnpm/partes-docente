package unpsjb.labprog.backend.presenter;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

    @RequestMapping(value = "/buscar", method = RequestMethod.GET)
    public ResponseEntity<Object> buscarLicencias(
            @RequestParam("personaDni") Long personaDni,
            @RequestParam("articulo") String articulo,
            @RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        try {
            List<Licencia> licencias = licenciaService.buscarLicenciasPorDni(personaDni, articulo, desde, hasta);
            if (licencias.isEmpty()) {
                return Response.notFound("No se encontraron licencias con los criterios especificados.");
            }
            return Response.ok(licencias);
        } catch (Exception e) {
            return Response.internalServerError(null, "Error al buscar licencias: " + e.getMessage());
        }
    }
}
