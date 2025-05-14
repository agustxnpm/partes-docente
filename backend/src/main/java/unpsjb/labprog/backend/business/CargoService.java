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
import unpsjb.labprog.backend.model.Cargo;
import unpsjb.labprog.backend.model.Division;

@Service
public class CargoService {

    @Autowired
    private CargoRepository cargoRepository;

    @Autowired
    private MensajeBuilder mensajeBuilder;

    @Autowired
    private Validator validator;

    @Transactional
    public Cargo save(Cargo cargo) {
        validator.validarCargo(cargo);
        return cargoRepository.save(cargo);
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
            division.getTurno()
        );
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
}
