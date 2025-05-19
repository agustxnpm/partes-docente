package unpsjb.labprog.backend.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import unpsjb.labprog.backend.model.Horario;

@Service
public class HorarioService {
    
    @Autowired
    private HorarioRepository horarioRepository;

    @Autowired
    private MensajeBuilder mensajeBuilder;

    @Autowired
    private Validator validator;

    @Transactional
    public Horario save(Horario horario) {
        validator.validarHorario(horario);
        return horarioRepository.save(horario);
    }

    @Transactional
    public void delete(Horario horario) {
        horarioRepository.delete(horario);
    }

    public Horario findById(Long id) {
        return horarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Horario no encontrado"));
    }

    public Horario findByDiaAndHora(String dia, int hora) {
        return horarioRepository.findByDiaAndHora(dia, hora)
                .orElseThrow(() -> new IllegalArgumentException("Horario no encontrado"));
    }

    public List<Horario> findAll() {
        return horarioRepository.findAll();
    }

    public String getMensajeExito(Horario horario) {
        return mensajeBuilder.generarMensajeExitoHorarioCreado(horario);
    }

    public String getMensajeExitoBorrado(Horario horario) {
        return mensajeBuilder.generarMensajeExitoHorarioBorrado(horario);
    }

    public String getMensajeExitoActualizacion (Horario horario) {
        return mensajeBuilder.generarMensajeExitoHorarioActualizado(horario);
    }
    
}
