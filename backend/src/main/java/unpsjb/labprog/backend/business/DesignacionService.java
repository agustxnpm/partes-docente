package unpsjb.labprog.backend.business;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import unpsjb.labprog.backend.business.utilidades.MensajeBuilder;
import unpsjb.labprog.backend.business.validaciones.Validator;
import unpsjb.labprog.backend.model.Designacion;
import unpsjb.labprog.backend.model.Persona;

@Service
public class DesignacionService {

    @Autowired
    private DesignacionRepository designacionRepository;

    @Autowired
    private MensajeBuilder mensajeBuilder;

    @Autowired
    private Validator validator;

    @Transactional
    public Designacion save(Designacion designacion) {
        validator.validarDesignacion(designacion);
        return designacionRepository.save(designacion);

    }

    @Transactional
    public void delete(Designacion designacion) {
        designacionRepository.delete(designacion);
    }

    public List<Designacion> findAll() {
        return designacionRepository.findAll();
    }

    public Designacion findById(Long id) {
        return designacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Designacion no encontrada"));
    }

    public List<Designacion> findDesignacionesSuperpuestas(Long cargoId, LocalDate fechaInicio, LocalDate fechaFin,
            Long designacionId) {
        return designacionRepository.findDesignacionesSuperpuestas(cargoId, fechaInicio, fechaFin, designacionId);
    }

    public Page<Designacion> findByPage(int page, int size) {
        return designacionRepository.findAll(
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
    }

    public List<Designacion> findSuplenciasEnPeriodo(Long cargoId, LocalDate fechaInicio, LocalDate fechaFin,
            Persona personaTitular, Long designacionId) {
        return designacionRepository.findSuplenciasEnPeriodo(cargoId, fechaInicio, fechaFin, personaTitular,
                designacionId);
    }

    public String getMensajeExitoActualizacion(Designacion designacion) {
        return mensajeBuilder.generarMensajeExitoDesignacionActualizada(designacion);
    }

    public String getMensajeExitoBorrado(Designacion designacion) {
        return mensajeBuilder.generarMensajeExitoDesignacionBorrada(designacion);
    }

    public String getMensajeExito(Designacion designacion) {
        return mensajeBuilder.generarMensajeExitoDesignacionCreada(designacion);
    }

    public String getMensajeExitoDesignacionSuplencia(Designacion designacionSuplantada, Persona personaSuplantada) {
        return mensajeBuilder.generarMensajeExitoDesignacionSuplencia(designacionSuplantada, personaSuplantada);
    }

}
