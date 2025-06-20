package unpsjb.labprog.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HorarioMapaDTO {
    private String dia;
    private int hora;
    private String espacioCurricular;
    private String docente;
    private String suplente;
    private boolean esSuplencia;
    private boolean horaLibre;
    private int divisionId;
    private String turno;
}
