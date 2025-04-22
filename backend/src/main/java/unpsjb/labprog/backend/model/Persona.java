package unpsjb.labprog.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Persona {
    
    @Id
    private long dni;
    
    @Column (unique = true)
    private String cuil;

    private String nombre;
    private String apellido;
    private String titulo;
    private String sexo;
    private String domicilio;
    private String telefono;
}
