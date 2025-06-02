package unpsjb.labprog.backend.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Licencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDate pedidoDesde;
    private LocalDate pedidoHasta;

    @Column(length = 90)
    private String domicilio;

    private boolean certificadoMedico;

    @ManyToOne
    private ArticuloLicencia articuloLicencia;

    @ManyToOne
    private Persona persona;

    @ManyToMany
    @JoinTable(name = "licencia_designacion", joinColumns = @JoinColumn(name = "licencia_id"), inverseJoinColumns = @JoinColumn(name = "designacion_id"))
    private List<Designacion> designaciones;

    @Enumerated(EnumType.STRING)
    private EstadoLicencia estado;

 
    @OneToMany(mappedBy = "licencia", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<LogLicencia> logs = new ArrayList<>();
}
