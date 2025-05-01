package unpsjb.labprog.backend.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
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

    @OneToMany(mappedBy = "persona", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Designacion> designaciones;
    
}
