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
        MapaHorarioSemanalDTO mapa = crearMapaHorarioBase(division, fechaInicio, fechaFin);
        List<HorarioMapaDTO> horariosSemanales = generarHorariosParaTodaLaSemana(division, fechaInicio, fechaFin);
        mapa.setHorarios(horariosSemanales);
        return mapa;
    }

    /**
     * Crea la estructura base del mapa horario semanal con la información de la división
     */
    private MapaHorarioSemanalDTO crearMapaHorarioBase(Division division, LocalDate fechaInicio, LocalDate fechaFin) {
        MapaHorarioSemanalDTO mapa = new MapaHorarioSemanalDTO();
        mapa.setFechaInicio(fechaInicio);
        mapa.setFechaFin(fechaFin);
        mapa.setTurno(division.getTurno().toString());
        mapa.setAnio(division.getAnio());
        mapa.setNumDivision(division.getNumDivision());
        return mapa;
    }

    /**
     * Genera todos los horarios para toda la semana (días y horas)
     */
    private List<HorarioMapaDTO> generarHorariosParaTodaLaSemana(Division division, LocalDate fechaInicio, LocalDate fechaFin) {
        List<HorarioMapaDTO> horariosSemanales = new ArrayList<>();
        String[] diasDeLaSemana = obtenerDiasDeLaSemana();
        
        for (String dia : diasDeLaSemana) {
            List<HorarioMapaDTO> horariosDelDia = generarHorariosParaDia(division, dia, fechaInicio, fechaFin);
            horariosSemanales.addAll(horariosDelDia);
        }
        
        return horariosSemanales;
    }

    /**
     * Obtiene los días de la semana laborables
     */
    private String[] obtenerDiasDeLaSemana() {
        return new String[]{"LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES"};
    }

    /**
     * Genera todos los horarios para un día específico (todas las horas)
     */
    private List<HorarioMapaDTO> generarHorariosParaDia(Division division, String dia, LocalDate fechaInicio, LocalDate fechaFin) {
        List<HorarioMapaDTO> horariosDelDia = new ArrayList<>();
        int[] horasDelDia = obtenerHorasDelDia();
        
        for (int hora : horasDelDia) {
            HorarioMapaDTO horarioMapa = generarHorarioParaDiaYHora(division, dia, hora, fechaInicio, fechaFin);
            horariosDelDia.add(horarioMapa);
        }
        
        return horariosDelDia;
    }

    /**
     * Obtiene las horas del día escolar (1 a 8)
     */
    private int[] obtenerHorasDelDia() {
        return new int[]{1, 2, 3, 4, 5, 6, 7, 8};
    }
    
    @Override
    public List<Division> obtenerDivisionesParaMapa() {
        return divisionService.findAll();
    }
    
    private HorarioMapaDTO generarHorarioParaDiaYHora(Division division, String dia, int hora, 
                                                     LocalDate fechaInicio, LocalDate fechaFin) {
        HorarioMapaDTO horarioMapa = crearHorarioMapaBasico(division, dia, hora);
        
        try {
            List<Cargo> cargosEspaciosCurriculares = buscarCargosParaHorario(division, dia, hora);
            
            if (cargosEspaciosCurriculares.isEmpty()) {
                configurarHoraLibre(horarioMapa);
            } else {
                Cargo cargo = cargosEspaciosCurriculares.get(0);
                procesarCargoConHorario(horarioMapa, cargo, fechaInicio, fechaFin);
            }
            
        } catch (Exception e) {
            configurarHoraLibreConError(horarioMapa);
        }
        
        return horarioMapa;
    }

    /**
     * Crea un HorarioMapaDTO con la información básica
     */
    private HorarioMapaDTO crearHorarioMapaBasico(Division division, String dia, int hora) {
        HorarioMapaDTO horarioMapa = new HorarioMapaDTO();
        horarioMapa.setDia(dia);
        horarioMapa.setHora(hora);
        horarioMapa.setDivisionId((int) division.getId());
        horarioMapa.setTurno(division.getTurno().toString());
        return horarioMapa;
    }

    /**
     * Busca los cargos de espacios curriculares que corresponden a una división, día y hora específicos
     */
    private List<Cargo> buscarCargosParaHorario(Division division, String dia, int hora) {
        return cargoService.findAll().stream()
            .filter(cargo -> cargo.getTipoDesignacion() == TipoDesignacion.ESPACIO_CURRICULAR)
            .filter(cargo -> cargo.getDivision() != null && cargo.getDivision().getId() == division.getId())
            .filter(cargo -> cargo.getHorario() != null && 
                cargo.getHorario().stream().anyMatch(h -> h.getDia().equals(dia) && h.getHora() == hora))
            .toList();
    }

    /**
     * Configura el horario como hora libre
     */
    private void configurarHoraLibre(HorarioMapaDTO horarioMapa) {
        horarioMapa.setHoraLibre(true);
        horarioMapa.setEspacioCurricular("Hora libre");
        horarioMapa.setDocente("");
        horarioMapa.setEsSuplencia(false);
    }

    /**
     * Configura el horario como hora libre cuando ocurre un error
     */
    private void configurarHoraLibreConError(HorarioMapaDTO horarioMapa) {
        horarioMapa.setHoraLibre(true);
        horarioMapa.setEspacioCurricular("Hora libre");
        horarioMapa.setDocente("");
        horarioMapa.setEsSuplencia(false);
    }

    /**
     * Procesa un cargo que tiene horario asignado
     */
    private void procesarCargoConHorario(HorarioMapaDTO horarioMapa, Cargo cargo, 
                                       LocalDate fechaInicio, LocalDate fechaFin) {
        horarioMapa.setEspacioCurricular(cargo.getNombre());
        horarioMapa.setHoraLibre(false);
        
        Optional<Designacion> designacionActiva = buscarDesignacionTitular(cargo, fechaInicio, fechaFin);
        
        if (designacionActiva.isPresent()) {
            procesarDesignacionActiva(horarioMapa, designacionActiva.get(), cargo, fechaInicio);
        } else {
            configurarSinDocenteAsignado(horarioMapa);
        }
    }

    /**
     * Busca la designación titular activa para un cargo en el período dado
     */
    private Optional<Designacion> buscarDesignacionTitular(Cargo cargo, LocalDate fechaInicio, LocalDate fechaFin) {
        return designacionService.findAll().stream()
            .filter(d -> d.getCargo().getId() == cargo.getId())
            .filter(d -> !d.getFechaInicio().isAfter(fechaFin))
            .filter(d -> d.getFechaFin() == null || !d.getFechaFin().isBefore(fechaInicio))
            .filter(d -> "Titular".equalsIgnoreCase(d.getSituacionRevista()))
            .findFirst();
    }

    /**
     * Procesa una designación activa verificando licencias y suplencias
     */
    private void procesarDesignacionActiva(HorarioMapaDTO horarioMapa, Designacion designacion, 
                                         Cargo cargo, LocalDate fechaInicio) {
        Persona docente = designacion.getPersona();
        
        if (docenteTieneLicencia(docente.getId(), fechaInicio)) {
            procesarDocenteConLicencia(horarioMapa, cargo, fechaInicio);
        } else {
            configurarDocenteTitular(horarioMapa, docente);
        }
    }

    /**
     * Procesa el caso cuando el docente titular tiene licencia
     */
    private void procesarDocenteConLicencia(HorarioMapaDTO horarioMapa, Cargo cargo, LocalDate fechaInicio) {
        String suplente = obtenerSuplenteParaCargo(cargo.getId(), fechaInicio);
        
        if (suplente != null && !suplente.isEmpty()) {
            configurarSuplente(horarioMapa, suplente);
        } else {
            configurarHoraLibrePorLicencia(horarioMapa);
        }
    }

    /**
     * Configura el horario con un docente titular
     */
    private void configurarDocenteTitular(HorarioMapaDTO horarioMapa, Persona docente) {
        horarioMapa.setDocente(docente.getNombre() + " " + docente.getApellido());
        horarioMapa.setEsSuplencia(false);
    }

    /**
     * Configura el horario con un suplente
     */
    private void configurarSuplente(HorarioMapaDTO horarioMapa, String suplente) {
        horarioMapa.setDocente(suplente);
        horarioMapa.setSuplente(suplente);
        horarioMapa.setEsSuplencia(true);
    }

    /**
     * Configura el horario como libre cuando el docente tiene licencia y no hay suplente
     */
    private void configurarHoraLibrePorLicencia(HorarioMapaDTO horarioMapa) {
        horarioMapa.setHoraLibre(true);
        horarioMapa.setDocente("Hora libre (docente con licencia)");
        horarioMapa.setEsSuplencia(false);
    }

    /**
     * Configura el horario cuando no hay docente asignado
     */
    private void configurarSinDocenteAsignado(HorarioMapaDTO horarioMapa) {
        horarioMapa.setHoraLibre(true);
        horarioMapa.setDocente("Sin docente asignado");
        horarioMapa.setEsSuplencia(false);
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
