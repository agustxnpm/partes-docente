package unpsjb.labprog.backend.business.validaciones.plugins;

import unpsjb.labprog.backend.business.interfaces.validaciones.IDesignacionRule;
import unpsjb.labprog.backend.model.Designacion;

/**
 * Plugin de validación de prueba para demostrar el sistema dinámico.
 * Esta regla valida que las designaciones no se hagan en fin de semana.
 * 
 * Plugin creado para testing del sistema de carga dinámica.
 */
public class FinDeSemanaDesignacionRule implements IDesignacionRule {

    // Singleton (requerido)
    private static FinDeSemanaDesignacionRule instance = null;
    
    private FinDeSemanaDesignacionRule() {}
    
    public static FinDeSemanaDesignacionRule getInstance() {
        if (instance == null) {
            instance = new FinDeSemanaDesignacionRule();
        }
        return instance;
    }

    @Override
    public void validate(Designacion designacion) {
        // Obtener el día de la semana de la fecha de inicio
        int dayOfWeek = designacion.getFechaInicio().getDayOfWeek().getValue();
        
        // Validar que no sea sábado (6) o domingo (7)
        if (dayOfWeek == 6 || dayOfWeek == 7) {
            throw new IllegalArgumentException(
                "No se pueden crear designaciones con fecha de inicio en fin de semana. " +
                "Fecha proporcionada: " + designacion.getFechaInicio() + 
                " (día de la semana: " + getDayName(dayOfWeek) + ")"
            );
        }
    }

    @Override
    public String getRuleName() {
        return "Validación de Fin de Semana";
    }
    
    /**
     * Método auxiliar para obtener el nombre del día
     */
    private String getDayName(int dayOfWeek) {
        switch (dayOfWeek) {
            case 1: return "Lunes";
            case 2: return "Martes";
            case 3: return "Miércoles";
            case 4: return "Jueves";
            case 5: return "Viernes";
            case 6: return "Sábado";
            case 7: return "Domingo";
            default: return "Desconocido";
        }
    }
}
