package unpsjb.labprog.backend.presenter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import unpsjb.labprog.backend.Response;
import unpsjb.labprog.backend.model.Designacion;
import unpsjb.labprog.backend.model.TipoDesignacion;

@RestController
@RequestMapping("designaciones")
public class DesignacionPresenter {

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> designarCargo(@RequestBody Designacion designacion) {

        System.out.println("Tipo de designación recibido: " + designacion.getCargo().getTipoDesignacion());

        if (designacion.getCargo().getTipoDesignacion() == TipoDesignacion.CARGO) {
            return Response.ok(designacion,
                    designacion.getPersona().getNombre() + " " + designacion.getPersona().getApellido()
                            + " ha sido designado/a como " + designacion.getCargo().getNombre() + " exitosamente");
        }

        if (designacion.getCargo().getTipoDesignacion() == TipoDesignacion.ESPACIO_CURRICULAR) {
            return Response.ok(designacion,
                    designacion.getPersona().getNombre() + " " + designacion.getPersona().getApellido()
                            + " ha sido designado/a a la asignatura " + designacion.getCargo().getNombre()
                            + " a la división " + designacion.getCargo().getDivision().getAnio() + "º "
                            + designacion.getCargo().getDivision().getNumDivision() + "º turno " + designacion.getCargo().getDivision().getTurno() + " exitosamente");
        }

        return Response.notFound();
    }
}
