package unpsjb.labprog.backend.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
    name = "cargo",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"nombre", "cargaHoraria", "fechaInicio", "tipoDesignacion", "division_id"}
    )
)
public class Cargo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   
    private long id;

    private String nombre;

    private int cargaHoraria;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    @ManyToOne
    private Division division;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cargo_id", referencedColumnName = "id")
    @JsonIgnore
    private List<Horario> horario;

    @Enumerated(EnumType.STRING)
    private TipoDesignacion tipoDesignacion;
}
