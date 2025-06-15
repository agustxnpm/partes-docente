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
public class PersonaConceptoDTO {
    
    @JsonProperty("DNI")
    private long dni;
    
    @JsonProperty("Nombre")
    private String nombre;
    
    @JsonProperty("Apellido")
    private String apellido;
    
    @JsonProperty("CUIL")
    private String cuil;
    
    @JsonProperty("Titulo")
    private String titulo;
    
    @JsonProperty("TotalDiasLicencia")
    private int totalDiasLicencia;
    
    @JsonProperty("Licencias")
    private List<LicenciaReporteDTO> licencias;
}
