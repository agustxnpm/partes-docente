package unpsjb.labprog.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LicenciaReporteDTO {
    
    @JsonProperty("ArticuloLicencia")
    private String articuloLicencia;
    
    @JsonProperty("Descripcion")
    private String descripcion;
    
    @JsonProperty("FechaDesde")
    private String fechaDesde;
    
    @JsonProperty("FechaHasta")
    private String fechaHasta;
    
    @JsonProperty("DiasTomados")
    private int diasTomados;
    
    @JsonProperty("Estado")
    private String estado;
}
