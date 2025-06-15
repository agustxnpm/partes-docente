package unpsjb.labprog.backend.dto;

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
public class ReporteConceptoDTO {
    
    @JsonProperty("Anio")
    private int anio;
    
    @JsonProperty("FechaGeneracion")
    private String fechaGeneracion;
    
    @JsonProperty("TotalPersonas")
    private int totalPersonas;
    
    @JsonProperty("TotalLicencias")
    private int totalLicencias;
    
    @JsonProperty("TotalDiasLicencia")
    private int totalDiasLicencia;
    
    @JsonProperty("Personas")
    private List<PersonaConceptoDTO> personas;
}
