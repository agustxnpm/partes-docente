package unpsjb.labprog.backend.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import unpsjb.labprog.backend.business.interfaces.IHorarioService;
import unpsjb.labprog.backend.business.interfaces.IHorarioValidator;
import unpsjb.labprog.backend.business.utilidades.MensajeBuilder;
import unpsjb.labprog.backend.model.Horario;

/**
 * Implementación del servicio de horarios.
 * Aplica el principio DIP (Dependency Inversion Principle) dependiendo de abstracciones
 * en lugar de implementaciones concretas.
 */
@Service
public class HorarioService implements IHorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

    @Autowired
    private MensajeBuilder mensajeBuilder;

    // Aplicando DIP e ISP: Dependemos de la abstracción específica IHorarioValidator
    @Autowired
    private IHorarioValidator horarioValidator;

    @Transactional
    public Horario save(Horario horario) {
        horarioValidator.validarHorario(horario);
        return horarioRepository.save(horario);
    }

    @Transactional
    public void delete(Horario horario) {
        horarioValidator.validarBorradoHorario(horario);
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

    public boolean isHorarioAsignadoACargo(Long horarioId) {
        return horarioRepository.isHorarioAsignadoACargo(horarioId);
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

    public String getMensajeExitoActualizacion(Horario horario) {
        return mensajeBuilder.generarMensajeExitoHorarioActualizado(horario);
    }

}
