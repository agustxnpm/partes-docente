package unpsjb.labprog.backend.business;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import unpsjb.labprog.backend.model.Cargo;

@Service
public class CargoService {
    
    @Autowired
    private CargoRepository cargoRepository;

    @Autowired
    private MensajeBuilder mensajeBuilder;

    @Autowired
    private Validator validator;

    public Cargo save(Cargo cargo) {
        validator.validarCargo(cargo);
        return cargoRepository.save(cargo);
    }

    public String getMensajeExito(Cargo cargo) {
        return mensajeBuilder.generarMensajeExitoCargoCreado(cargo);
    }
}
