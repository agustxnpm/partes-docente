package unpsjb.labprog.backend.presenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import unpsjb.labprog.backend.Response;
import unpsjb.labprog.backend.business.validaciones.DesignacionValidator;

/**
 * Presenter para operaciones administrativas del sistema.
 * Aplica el principio DIP (Dependency Inversion Principle)
 */
@RestController
@RequestMapping("admin")
public class AdminPresenter {

    @Autowired
    private DesignacionValidator designacionValidator;

    /**
     * Recarga las reglas de validación de designaciones desde el archivo de configuración.
     * 
     * Este endpoint permite agregar nuevas reglas de validación sin necesidad de
     * reiniciar la aplicación. Útil para deployment en caliente de nuevas validaciones.
     * 
     */
    @RequestMapping(value = "validador/designaciones/recargar-controles", method = RequestMethod.POST)
    public ResponseEntity<Object> reloadValidationRules() {
        try {
            designacionValidator.reloadRules();
            return Response.ok(null, "Reglas de validación de designaciones recargadas exitosamente");
        } catch (Exception e) {
            return Response.internalServerError(null, "Error recargando reglas de validación: " + e.getMessage());
        }
    }


}
