package unpsjb.labprog.backend.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParteDiarioDTO {
    
    @JsonProperty("Fecha")
    private LocalDate fecha;
    
    @JsonProperty("Docentes")
    private List<DocenteLicenciaDTO> docentes;
}
