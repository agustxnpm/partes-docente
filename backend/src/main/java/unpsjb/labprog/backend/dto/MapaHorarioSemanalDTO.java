package unpsjb.labprog.backend.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MapaHorarioSemanalDTO {
    private LocalDate fechaInicio; // Lunes de la semana
    private LocalDate fechaFin;    // Viernes de la semana
    private String turno;
    private int anio;
    private int numDivision;
    private List<HorarioMapaDTO> horarios;
}
