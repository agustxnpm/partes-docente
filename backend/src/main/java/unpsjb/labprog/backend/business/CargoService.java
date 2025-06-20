package unpsjb.labprog.backend.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import unpsjb.labprog.backend.business.interfaces.ICargoService;
import unpsjb.labprog.backend.business.interfaces.ICargoValidator;
import unpsjb.labprog.backend.business.utilidades.MensajeBuilder;
import unpsjb.labprog.backend.model.Cargo;
import unpsjb.labprog.backend.model.Division;
import unpsjb.labprog.backend.model.Horario;

/**
 * Implementación del servicio de cargos.
 * Aplica el principio DIP (Dependency Inversion Principle) dependiendo de abstracciones
 * en lugar de implementaciones concretas.
 */
@Service
public class CargoService implements ICargoService {

    @Autowired
    private CargoRepository cargoRepository;

    @Autowired
    private MensajeBuilder mensajeBuilder;

    // Aplicando DIP e ISP: Dependemos de la abstracción específica ICargoValidator
    @Autowired
    private ICargoValidator cargoValidator;

    @Transactional
    public Cargo save(Cargo cargo) {
        if (cargo.getId() != 0) {
            return actualizarCargo(cargo);
        } else {
            return crearCargo(cargo);
        }
    }
    
    private Cargo actualizarCargo(Cargo cargo) {
        Cargo cargoExistente = findById(cargo.getId());
        actualizarDatosCargo(cargoExistente, cargo);
        actualizarHorarios(cargoExistente, cargo.getHorario());
        cargoValidator.validarCargo(cargoExistente);
        return cargoRepository.save(cargoExistente);
    }
    
    private Cargo crearCargo(Cargo cargo) {
        cargoValidator.validarCargo(cargo);
        return cargoRepository.save(cargo);
    }
    
    private void actualizarDatosCargo(Cargo cargoExistente, Cargo cargoNuevo) {
        cargoExistente.setNombre(cargoNuevo.getNombre());
        cargoExistente.setCargaHoraria(cargoNuevo.getCargaHoraria());
        cargoExistente.setFechaInicio(cargoNuevo.getFechaInicio());
        cargoExistente.setFechaFin(cargoNuevo.getFechaFin());
        cargoExistente.setTipoDesignacion(cargoNuevo.getTipoDesignacion());
        cargoExistente.setDivision(cargoNuevo.getDivision());
    }
    
    private void actualizarHorarios(Cargo cargoExistente, List<Horario> horariosNuevos) {
        if (horariosNuevos == null) return;
        
        List<Horario> horariosActualizados = procesarHorarios(cargoExistente, horariosNuevos);
        cargoExistente.getHorario().clear();
        cargoExistente.getHorario().addAll(horariosActualizados);
    }
    
    private List<Horario> procesarHorarios(Cargo cargoExistente, List<Horario> horariosNuevos) {
        List<Horario> horariosActualizados = new ArrayList<>();
        
        for (Horario horarioNuevo : horariosNuevos) {
            if (esHorarioExistente(horarioNuevo)) {
                Horario horarioActualizado = actualizarHorarioExistente(cargoExistente, horarioNuevo);
                if (horarioActualizado != null) {
                    horariosActualizados.add(horarioActualizado);
                } else {
                    // Si no se encuentra, crear uno nuevo con los mismos datos
                    horariosActualizados.add(crearNuevoHorario(horarioNuevo));
                }
            } else {
                horariosActualizados.add(crearNuevoHorario(horarioNuevo));
            }
        }
        
        return horariosActualizados;
    }
    
    private boolean esHorarioExistente(Horario horario) {
        return horario.getId() != 0;
    }
    
    private Horario actualizarHorarioExistente(Cargo cargoExistente, Horario horarioNuevo) {
        return cargoExistente.getHorario().stream()
            .filter(h -> h.getId() == horarioNuevo.getId())
            .findFirst()
            .map(horarioExistente -> {
                horarioExistente.setDia(horarioNuevo.getDia());
                horarioExistente.setHora(horarioNuevo.getHora());
                return horarioExistente;
            })
            .orElse(null);
    }
    
    private Horario crearNuevoHorario(Horario horarioBase) {
        Horario nuevoHorario = new Horario();
        nuevoHorario.setDia(horarioBase.getDia());
        nuevoHorario.setHora(horarioBase.getHora());
        return nuevoHorario;
    }

    @Transactional
    public void delete(Cargo cargo) {
        cargoRepository.delete(cargo);
    }

    public Page<Cargo> findByPage(int page, int size) {
        return cargoRepository.findAll(
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
    }

    public List<Cargo> findAll() {
        List<Cargo> result = new ArrayList<>();
        cargoRepository.findAll().forEach(e -> result.add(e));
        return result;
    }

    public Cargo findById(Long id) {
        return cargoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cargo no encontrado"));
    }

    public Optional<Cargo> findByNombre(String nombre) {
        return cargoRepository.findByNombre(nombre);
    }

    public Optional<Cargo> findByNombreAndDivision(String nombre, Division division) {
        return cargoRepository.findByNombreAndDivision(nombre, division);
    }

    public Optional<Cargo> findByNombreAndDivisionExacta(String nombre, Division division) {
        return cargoRepository.findByNombreAndDivisionDetalle(
                nombre,
                division.getAnio(),
                division.getNumDivision(),
                division.getTurno());
    }

    public String getMensajeExito(Cargo cargo) {
        return mensajeBuilder.generarMensajeExitoCargoCreado(cargo);
    }

    public String getMensajeExitoActualizacion(Cargo cargo) {
        return mensajeBuilder.generarMensajeExitoCargoActualizado(cargo);
    }

    public String getMensajeExitoBorrado(Cargo cargo) {
        return mensajeBuilder.generarMensajeExitoCargoBorrado(cargo);
    }

    public boolean existsByDivisionId(long id) {
        return cargoRepository.existsByDivisionId(id);
    }

    public List<Cargo> search(String term) {
        return cargoRepository.search(term.trim());
    }
}
