package unpsjb.labprog.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    uniqueConstraints = @UniqueConstraint(columnNames = {"anio", "numDivision"})
)
@Getter
@Setter
@NoArgsConstructor
public class Division {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int anio;
    private int numDivision;
    private String orientacion;
    
    @Enumerated(EnumType.STRING)
    private Turno turno;
}
