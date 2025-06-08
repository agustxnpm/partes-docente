package unpsjb.labprog.backend.presenter;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import unpsjb.labprog.backend.Response;
import unpsjb.labprog.backend.business.interfaces.ILicenciaService;
import unpsjb.labprog.backend.business.interfaces.ILogLicenciaService;
import unpsjb.labprog.backend.model.EstadoLicencia;
import unpsjb.labprog.backend.model.Licencia;
import unpsjb.labprog.backend.model.LogLicencia;

@RestController
@RequestMapping("licencias")
public class LicenciasPresenter {

    @Autowired
    private ILicenciaService licenciaService;

    @Autowired
    private ILogLicenciaService logLicenciaService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> createLicencia(@RequestBody Licencia licencia) {
        try {
            Licencia licenciaGuardada = licenciaService.createLicencia(licencia);
            return buildLicenciaResponse(licenciaGuardada);
        } catch (Exception e) {
            return Response.internalServerError(licencia, "Error al procesar la licencia: " + e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateLicencia(@PathVariable("id") Long id, @RequestBody Licencia licencia) {
        try {
            Licencia licenciaActualizada = licenciaService.updateLicencia(id, licencia);
            return buildLicenciaResponse(licenciaActualizada);
        } catch (Exception e) {
            return Response.internalServerError(licencia, "Error al procesar la licencia: " + e.getMessage());
        }
    }

    /**
     * Construye la respuesta HTTP basada en el estado de la licencia.
     * Aplica el principio SRP (Single Responsibility Principle).
     */
    private ResponseEntity<Object> buildLicenciaResponse(Licencia licencia) {
        if (licencia.getEstado() == EstadoLicencia.VALIDA) {
            return Response.ok(licencia,
                    logLicenciaService.obtenerUltimoLog(licencia).getMensaje());
        } else {
            return Response.internalServerError(licencia,
                    logLicenciaService.obtenerUltimoLog(licencia).getMensaje());
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

    @RequestMapping(value = "/estado/{estado}", method = RequestMethod.GET)
    public ResponseEntity<Object> obtenerLicenciasPorEstado(@PathVariable EstadoLicencia estado) {
        try {
            List<Licencia> licencias = licenciaService.findByEstado(estado);
            return Response.ok(licencias);
        } catch (Exception e) {
            return Response.badRequest(null, "Error al obtener licencias por estado: " + e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}/logs", method = RequestMethod.GET)
    public ResponseEntity<Object> obtenerLogsLicencia(@PathVariable Long id) {
        try {
            List<LogLicencia> logs = logLicenciaService.obtenerLicenciasEnOrdenPorFechaDesc(id);
            return Response.ok(logs);
        } catch (Exception e) {
            return Response.badRequest(null, "Error al obtener logs: " + e.getMessage());
        }
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public ResponseEntity<Object> findByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Response.ok(licenciaService.findByPage(page, size));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> findById(@PathVariable("id") Long id) {
        try {
            Licencia licencia = licenciaService.findById(id);
            return Response.ok(licencia);
        } catch (IllegalArgumentException e) {
            return Response.notFound("Licencia con ID " + id + " no encontrada.");
        }
    }

}
