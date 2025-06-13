package unpsjb.labprog.backend.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocenteLicenciaDTO {
    
    @JsonProperty("DNI")
    private Long dni;
    
    @JsonProperty("Nombre")
    private String nombre;
    
    @JsonProperty("Apellido")
    private String apellido;
    
    @JsonProperty("Artículo")
    private String articulo;
    
    @JsonProperty("Descripción")
    private String descripcion;
    
    @JsonProperty("Desde")
    private LocalDate desde;
    
    @JsonProperty("Hasta")
    private LocalDate hasta;
}
