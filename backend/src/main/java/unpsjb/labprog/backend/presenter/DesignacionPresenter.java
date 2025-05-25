package unpsjb.labprog.backend.presenter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import unpsjb.labprog.backend.Response;
import unpsjb.labprog.backend.business.DesignacionService;
import unpsjb.labprog.backend.model.Designacion;
import unpsjb.labprog.backend.model.Persona;

@RestController
@RequestMapping("designaciones")
public class DesignacionPresenter {

    @Autowired
    private DesignacionService designacionService;

     @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> designarCargo(@RequestBody Designacion designacion) {
        try {
            Designacion designacionGuardada = designacionService.save(designacion); // El servicio ahora solo guarda
            String mensajeExito;

            if ("Suplente".equalsIgnoreCase(designacionGuardada.getSituacionRevista())) {
                // Intentar encontrar a la persona reemplazada
                // Esta lógica asume que la designación superpuesta es la que se está cubriendo.
                // Se excluye la propia designación del suplente (designacionGuardada.getId()) de la búsqueda.
                List<Designacion> posiblesOriginales = designacionService.findDesignacionesSuperpuestas(
                    designacionGuardada.getCargo().getId(),
                    designacionGuardada.getFechaInicio(),
                    designacionGuardada.getFechaFin(),
                    designacionGuardada.getId() // Excluir la designación del suplente recién creada
                );

                if (!posiblesOriginales.isEmpty()) {
                     // Asumimos que la primera superposición encontrada (que no es el suplente) es la persona reemplazada.
                     // En escenarios más complejos, esta lógica podría necesitar ser más robusta.
                     Persona personaReemplazada = posiblesOriginales.get(0).getPersona();
                     mensajeExito = designacionService.getMensajeExitoDesignacionSuplencia(designacionGuardada, personaReemplazada);
                } else {
                    mensajeExito = designacionService.getMensajeExito(designacionGuardada);
                }
            } else {
                mensajeExito = designacionService.getMensajeExito(designacionGuardada);
            }
            return Response.ok(designacionGuardada, mensajeExito);
        } catch (IllegalArgumentException e) {
            return Response.internalServerError(designacion, e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteDesignacion(@PathVariable("id") Long id) {
        try {
            Designacion designacion = designacionService.findById(id);
            designacionService.delete(designacion);
            return Response.ok(designacion,
                    designacionService.getMensajeExitoBorrado(designacion));
        } catch (IllegalArgumentException e) {
            return Response.badRequest(e, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> listarDesignaciones() {
        try {
            return Response.ok(designacionService.findAll());
        } catch (IllegalArgumentException e) {
            return Response.badRequest(e, e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getDesignacionById(@PathVariable("id") Long id) {
        try {
            Designacion designacion = designacionService.findById(id);
            return Response.ok(designacion);
        } catch (IllegalArgumentException e) {
            return Response.badRequest(e, e.getMessage());
        }
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public ResponseEntity<Object> findByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Response.ok(designacionService.findByPage(page, size));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateCargo(@PathVariable("id") Long id, @RequestBody Designacion designacion) {
        try {
            designacionService.save(designacion);
            return Response.ok(designacion, designacionService.getMensajeExitoActualizacion(designacion));
        } catch (IllegalArgumentException e) {
            return Response.badRequest(designacion, e.getMessage());
        }
    }

}
