package unpsjb.labprog.backend.business;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import unpsjb.labprog.backend.model.Cargo;
import unpsjb.labprog.backend.model.Designacion;
import unpsjb.labprog.backend.model.Division;
import unpsjb.labprog.backend.model.TipoDesignacion;

@Component
public class DesignacionValidator {

    @Autowired
    @Lazy
    private CargoService cargoService;

    @Autowired
    @Lazy
    private DesignacionService designacionService;

    public void validarDesignacion(Designacion designacion) {

        if (designacion == null) {
            throw new IllegalArgumentException("La designación no puede ser nula");
        }
        if (designacion.getCargo() == null) {
            throw new IllegalArgumentException("El cargo no puede ser nulo");
        }

        if (designacion.getPersona() == null) {
            throw new IllegalArgumentException("La persona no puede ser nula");
        }

        if (designacion.getCargo().getTipoDesignacion() == TipoDesignacion.CARGO
                && designacion.getCargo().getDivision() != null) {
            throw new IllegalArgumentException("Un cargo tipo CARGO no debe tener división asignada");
        }

        // Validación específica para ESPACIO_CURRICULAR
        if (designacion.getCargo().getTipoDesignacion() == TipoDesignacion.ESPACIO_CURRICULAR) {
            Division division = designacion.getCargo().getDivision();
            if (division == null) {
                throw new IllegalArgumentException("Un ESPACIO_CURRICULAR requiere una división");
            }

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
        }

        if (designacion.getFechaInicio() == null) {
            throw new IllegalArgumentException("La fecha de inicio es obligatoria");
        }

        if (designacion.getFechaFin() != null &&
                designacion.getFechaFin().isBefore(designacion.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha fin no puede ser anterior a la fecha inicio");
        }

        // Validar superposición de fechas
        Long designacionId = designacion.getId() == 0 ? null : designacion.getId(); // Si es nuevo, id es null

        List<Designacion> superpuestas = designacionService.findDesignacionesSuperpuestas(
                designacion.getCargo().getId(),
                designacion.getFechaInicio(),
                designacion.getFechaFin(),
                designacionId);

        if (!superpuestas.isEmpty()) {
            Designacion designacionExistente = superpuestas.get(0);
            String mensaje;

            if (designacion.getCargo().getTipoDesignacion() == TipoDesignacion.CARGO) {
                // Formato para cargos institucionales
                mensaje = String.format(
                        "%s %s NO ha sido designado/a como %s. pues el cargo solicitado lo ocupa %s %s para el período",
                        designacion.getPersona().getNombre(),
                        designacion.getPersona().getApellido(),
                        designacion.getCargo().getNombre().toLowerCase(),
                        designacionExistente.getPersona().getNombre(),
                        designacionExistente.getPersona().getApellido());
            } else {
                // Formato para espacios curriculares
                Division division = designacion.getCargo().getDivision();
                mensaje = String.format(
                        "%s %s NO ha sido designado/a debido a que la asignatura %s de la división %dº %dº turno %s lo ocupa %s %s para el período",
                        designacion.getPersona().getNombre(),
                        designacion.getPersona().getApellido(),
                        designacion.getCargo().getNombre(),
                        division.getAnio(),
                        division.getNumDivision(),
                        division.getTurno(),
                        designacionExistente.getPersona().getNombre(),
                        designacionExistente.getPersona().getApellido());
            }

            throw new IllegalArgumentException(mensaje);
        }
    }
}
