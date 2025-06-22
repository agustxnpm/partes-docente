package unpsjb.labprog.backend.business.validaciones.plugins;

import java.util.Optional;

import unpsjb.labprog.backend.business.interfaces.servicios.ICargoService;
import unpsjb.labprog.backend.business.interfaces.validaciones.IDesignacionRule;
import unpsjb.labprog.backend.model.Cargo;
import unpsjb.labprog.backend.model.Designacion;
import unpsjb.labprog.backend.model.Division;
import unpsjb.labprog.backend.model.TipoDesignacion;

/**
 * Plugin de validación para tipos de designación.
 * Verifica que la designación cumpla con las reglas específicas de cada tipo.
 */
public class TipoDesignacionDesignacionRule implements IDesignacionRule {

    // Singleton
    private static TipoDesignacionDesignacionRule instance = null;
    private ICargoService cargoService;
    
    private TipoDesignacionDesignacionRule() {}
    
    public static TipoDesignacionDesignacionRule getInstance() {
        if (instance == null) {
            instance = new TipoDesignacionDesignacionRule();
        }
        return instance;
    }

    @Override
    public void validate(Designacion designacion) {
        // Validación para cargo tipo CARGO
        if (designacion.getCargo().getTipoDesignacion() == TipoDesignacion.CARGO
                && designacion.getCargo().getDivision() != null) {
            throw new IllegalArgumentException("Un cargo tipo CARGO no debe tener división asignada");
        }

        // Validación específica para ESPACIO_CURRICULAR
        if (designacion.getCargo().getTipoDesignacion() == TipoDesignacion.ESPACIO_CURRICULAR) {
            validateEspacioCurricular(designacion);
        }
    }

    private void validateEspacioCurricular(Designacion designacion) {
        Division division = designacion.getCargo().getDivision();
        if (division == null) {
            throw new IllegalArgumentException("Un ESPACIO_CURRICULAR requiere una división");
        }

        // Si el servicio está disponible, hacer la validación completa
        if (cargoService != null) {
            // Buscar el cargo específico que coincida con nombre y división
            Optional<Cargo> cargoExacto = cargoService.findByNombreAndDivisionExacta(
                    designacion.getCargo().getNombre(),
                    division);

            if (!cargoExacto.isPresent()) {
                throw new IllegalArgumentException(
                        String.format("No existe el espacio curricular %s para la división %dº %dº turno %s",
                                designacion.getCargo().getNombre(),
                                division.getAnio(),
                                division.getNumDivision(),
                                division.getTurno()));
            }

            // Actualizar el cargo en la designación con el cargo exacto encontrado
            designacion.setCargo(cargoExacto.get());
        } else {
            // Validación básica sin acceso al servicio
            System.err.println("Advertencia: CargoService no disponible en plugin. Validación parcial aplicada.");
        }
    }

    @Override
    public String getRuleName() {
        return "Validación de Tipo de Designación";
    }
    
    /**
     * Método para inyectar el servicio de cargo.
     */
    public void setCargoService(ICargoService cargoService) {
        this.cargoService = cargoService;
    }
}
