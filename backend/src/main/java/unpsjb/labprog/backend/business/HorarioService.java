package unpsjb.labprog.backend.business;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import unpsjb.labprog.backend.business.interfaces.mensajes.IHorarioMensajeBuilder;
import unpsjb.labprog.backend.business.interfaces.servicios.ICargoService;
import unpsjb.labprog.backend.business.interfaces.servicios.IDesignacionService;
import unpsjb.labprog.backend.business.interfaces.servicios.IDivisionService;
import unpsjb.labprog.backend.business.interfaces.servicios.IHorarioService;
import unpsjb.labprog.backend.business.interfaces.servicios.ILicenciaService;
import unpsjb.labprog.backend.business.interfaces.validaciones.IHorarioValidator;
import unpsjb.labprog.backend.dto.HorarioMapaDTO;
import unpsjb.labprog.backend.dto.MapaHorarioSemanalDTO;
import unpsjb.labprog.backend.model.Cargo;
import unpsjb.labprog.backend.model.Designacion;
import unpsjb.labprog.backend.model.Division;
import unpsjb.labprog.backend.model.Horario;
import unpsjb.labprog.backend.model.Licencia;
import unpsjb.labprog.backend.model.Persona;
import unpsjb.labprog.backend.model.TipoDesignacion;

/**
 * Implementación del servicio de horarios.
 * Aplica el principio DIP (Dependency Inversion Principle) dependiendo de abstracciones
 * en lugar de implementaciones concretas.
 */
@Service
public class HorarioService implements IHorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

    // Aplicando DIP: Dependemos de la abstracción específica IHorarioMensajeBuilder
    @Autowired
    private IHorarioMensajeBuilder horarioMensajeBuilder;

    // Aplicando DIP e ISP: Dependemos de la abstracción específica IHorarioValidator
    @Autowired
    private IHorarioValidator horarioValidator;
    
    // Dependencias para el mapa de horarios
    @Autowired
    private IDivisionService divisionService;
    
    @Autowired
    private ICargoService cargoService;
    
    @Autowired
    private IDesignacionService designacionService;
    
    @Autowired
    private ILicenciaService licenciaService;

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
        return horarioMensajeBuilder.generarMensajeExitoCreacion(horario);
    }

    public String getMensajeExitoBorrado(Horario horario) {
        return horarioMensajeBuilder.generarMensajeExitoBorrado(horario);
    }

    public String getMensajeExitoActualizacion(Horario horario) {
        return horarioMensajeBuilder.generarMensajeExitoActualizacion(horario);
    }
    
    // Métodos para el mapa de horarios
    
    @Override
    public MapaHorarioSemanalDTO generarMapaHorarioSemanal(Division division, LocalDate fechaInicio, LocalDate fechaFin) {
        MapaHorarioSemanalDTO mapa = new MapaHorarioSemanalDTO();
        mapa.setFechaInicio(fechaInicio);
        mapa.setFechaFin(fechaFin);
        mapa.setTurno(division.getTurno().toString());
        mapa.setAnio(division.getAnio());
        mapa.setNumDivision(division.getNumDivision());
        
        List<HorarioMapaDTO> horariosSemanales = new ArrayList<>();
        
        // Días de la semana
        String[] dias = {"LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES"};
        
        // Para cada día y cada hora (1 a 8)
        for (String dia : dias) {
            for (int hora = 1; hora <= 8; hora++) {
                HorarioMapaDTO horarioMapa = generarHorarioParaDiaYHora(division, dia, hora, fechaInicio, fechaFin);
                horariosSemanales.add(horarioMapa);
            }
        }
        
        mapa.setHorarios(horariosSemanales);
        return mapa;
    }
    
    @Override
    public List<Division> obtenerDivisionesParaMapa() {
        return divisionService.findAll();
    }
    
    private HorarioMapaDTO generarHorarioParaDiaYHora(Division division, String dia, int hora, 
                                                     LocalDate fechaInicio, LocalDate fechaFin) {
        HorarioMapaDTO horarioMapa = new HorarioMapaDTO();
        horarioMapa.setDia(dia);
        horarioMapa.setHora(hora);
        horarioMapa.setDivisionId((int) division.getId());
        horarioMapa.setTurno(division.getTurno().toString());
        
        // Buscar el horario específico
        try {
            // Buscar todos los cargos de espacios curriculares para esta división que tengan este día y hora
            List<Cargo> cargosEspaciosCurriculares = cargoService.findAll().stream()
                .filter(cargo -> cargo.getTipoDesignacion() == TipoDesignacion.ESPACIO_CURRICULAR)
                .filter(cargo -> cargo.getDivision() != null && cargo.getDivision().getId() == division.getId())
                .filter(cargo -> cargo.getHorario() != null && 
                    cargo.getHorario().stream().anyMatch(h -> h.getDia().equals(dia) && h.getHora() == hora))
                .toList();
            
            if (cargosEspaciosCurriculares.isEmpty()) {
                // Hora libre
                horarioMapa.setHoraLibre(true);
                horarioMapa.setEspacioCurricular("Hora libre");
                horarioMapa.setDocente("");
                horarioMapa.setEsSuplencia(false);
            } else {
                // Tomar el primer cargo encontrado (debería ser único por división/horario)
                Cargo cargo = cargosEspaciosCurriculares.get(0);
                horarioMapa.setEspacioCurricular(cargo.getNombre());
                
                // Buscar la designación activa para este cargo en el período
                Optional<Designacion> designacionActiva = designacionService.findAll().stream()
                    .filter(d -> d.getCargo().getId() == cargo.getId())
                    .filter(d -> !d.getFechaInicio().isAfter(fechaFin))
                    .filter(d -> d.getFechaFin() == null || !d.getFechaFin().isBefore(fechaInicio))
                    .filter(d -> "Titular".equalsIgnoreCase(d.getSituacionRevista()))
                    .findFirst();
                
                if (designacionActiva.isPresent()) {
                    Persona docente = designacionActiva.get().getPersona();
                    
                    // Verificar si el docente tiene licencia en este período
                    boolean tieneLicencia = docenteTieneLicencia(docente.getId(), fechaInicio);
                    
                    if (tieneLicencia) {
                        // Buscar suplente
                        String suplente = obtenerSuplenteParaCargo(cargo.getId(), fechaInicio);
                        if (suplente != null && !suplente.isEmpty()) {
                            horarioMapa.setDocente(suplente);
                            horarioMapa.setSuplente(suplente);
                            horarioMapa.setEsSuplencia(true);
                        } else {
                            horarioMapa.setHoraLibre(true);
                            horarioMapa.setDocente("Hora libre (docente con licencia)");
                            horarioMapa.setEsSuplencia(false);
                        }
                    } else {
                        horarioMapa.setDocente(docente.getNombre() + " " + docente.getApellido());
                        horarioMapa.setEsSuplencia(false);
                    }
                } else {
                    horarioMapa.setHoraLibre(true);
                    horarioMapa.setDocente("Sin docente asignado");
                    horarioMapa.setEsSuplencia(false);
                }
                
                horarioMapa.setHoraLibre(false);
            }
            
        } catch (Exception e) {
            // Si no se encuentra el horario, marcar como hora libre
            horarioMapa.setHoraLibre(true);
            horarioMapa.setEspacioCurricular("Hora libre");
            horarioMapa.setDocente("");
            horarioMapa.setEsSuplencia(false);
        }
        
        return horarioMapa;
    }
    
    private boolean docenteTieneLicencia(Long personaId, LocalDate fecha) {
        List<Licencia> licencias = licenciaService.getAllLicencias().stream()
            .filter(l -> l.getPersona().getId() == personaId)
            .filter(l -> !l.getPedidoDesde().isAfter(fecha))
            .filter(l -> !l.getPedidoHasta().isBefore(fecha))
            .filter(l -> l.getEstado() != null && "VALIDA".equalsIgnoreCase(l.getEstado().toString()))
            .toList();
        
        return !licencias.isEmpty();
    }
    
    private String obtenerSuplenteParaCargo(Long cargoId, LocalDate fecha) {
        Optional<Designacion> suplencia = designacionService.findAll().stream()
            .filter(d -> d.getCargo().getId() == cargoId)
            .filter(d -> "Suplente".equalsIgnoreCase(d.getSituacionRevista()))
            .filter(d -> !d.getFechaInicio().isAfter(fecha))
            .filter(d -> d.getFechaFin() == null || !d.getFechaFin().isBefore(fecha))
            .findFirst();
        
        if (suplencia.isPresent()) {
            Persona suplente = suplencia.get().getPersona();
            return suplente.getNombre() + " " + suplente.getApellido();
        }
        
        return null;
    }

}
