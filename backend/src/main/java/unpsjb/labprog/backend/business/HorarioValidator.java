package unpsjb.labprog.backend.business;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import unpsjb.labprog.backend.model.Horario;

@Component
public class HorarioValidator {

    @Autowired
    @Lazy
    private HorarioService horarioService;
    
    private static final Set<String> DIAS_SEMANA_VALIDOS = Set.of(
            "Lunes", "Martes", "Miércoles", "Jueves", "Viernes");

    private static final int HORA_MINIMA_PERMITIDA = 1;
    private static final int HORA_MAXIMA_PERMITIDA = 8;

    public void validarHorario(Horario horario) {
        if (horario == null) {
            throw new IllegalArgumentException("El objeto Horario no puede ser nulo.");
        }

        validarDia(horario.getDia());
        validarHora(horario.getHora());

        
    }

    private void validarDia(String dia) {
        if (dia == null || dia.trim().isEmpty()) {
            throw new IllegalArgumentException("El día del horario no puede estar vacío.");
        }
        if (!DIAS_SEMANA_VALIDOS.contains(dia)) {
            throw new IllegalArgumentException("El día '" + dia
                    + "' no es un día válido para el horario. Días permitidos: " + DIAS_SEMANA_VALIDOS);
        }
    }

    private void validarHora(int hora) {
        if (hora < HORA_MINIMA_PERMITIDA || hora > HORA_MAXIMA_PERMITIDA) {
            throw new IllegalArgumentException("La hora del horario debe estar entre " + HORA_MINIMA_PERMITIDA + " y "
                    + HORA_MAXIMA_PERMITIDA + ". Valor proporcionado: " + hora);
        }
    }

     public void validarBorrado(Horario horario) {
        if (horario == null || horario.getId() == 0) {
            throw new IllegalArgumentException("El horario a validar para borrado no puede ser nulo o no tener ID.");
        }
        if (horarioService.isHorarioAsignadoACargo(horario.getId())) {
            throw new IllegalArgumentException("No se puede borrar el horario de " + horario.getDia() + " a las "
                    + horario.getHora() + " hs. porque está asignado a uno o más cargos.");
        }
    }
}
