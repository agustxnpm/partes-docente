package unpsjb.labprog.backend.business.interfaces.servicios;

import java.time.LocalDate;
import java.util.List;
import unpsjb.labprog.backend.model.Horario;
import unpsjb.labprog.backend.model.Division;
import unpsjb.labprog.backend.dto.MapaHorarioSemanalDTO;

/**
 * Interfaz para el servicio de horarios.
 * Define las operaciones disponibles para la gestión de horarios.
 */
public interface IHorarioService {
    
    /**
     * Guarda un horario en el sistema.
     * @param horario El horario a guardar
     * @return El horario guardado
     */
    Horario save(Horario horario);
    
    /**
     * Elimina un horario del sistema.
     * @param horario El horario a eliminar
     */
    void delete(Horario horario);
    
    /**
     * Busca un horario por su ID.
     * @param id El ID del horario
     * @return El horario encontrado
     */
    Horario findById(Long id);
    
    /**
     * Busca un horario por día y hora.
     * @param dia El día del horario
     * @param hora La hora del horario
     * @return El horario encontrado
     */
    Horario findByDiaAndHora(String dia, int hora);
    
    /**
     * Verifica si un horario está asignado a un cargo.
     * @param horarioId El ID del horario
     * @return true si está asignado, false en caso contrario
     */
    boolean isHorarioAsignadoACargo(Long horarioId);
    
    /**
     * Obtiene todos los horarios.
     * @return Lista de todos los horarios
     */
    List<Horario> findAll();
    
    /**
     * Genera mensaje de éxito para creación de horario.
     * @param horario El horario creado
     * @return Mensaje de éxito
     */
    String getMensajeExito(Horario horario);
    
    /**
     * Genera mensaje de éxito para borrado de horario.
     * @param horario El horario borrado
     * @return Mensaje de éxito
     */
    String getMensajeExitoBorrado(Horario horario);
    
    /**
     * Genera mensaje de éxito para actualización de horario.
     * @param horario El horario actualizado
     * @return Mensaje de éxito
     */
    String getMensajeExitoActualizacion(Horario horario);
    
    // Métodos para el mapa de horarios
    
    /**
     * Genera un mapa de horarios semanal para una división específica.
     * @param division La división para la cual generar el mapa
     * @param fechaInicio Fecha de inicio de la semana
     * @param fechaFin Fecha de fin de la semana
     * @return El mapa de horarios semanal
     */
    MapaHorarioSemanalDTO generarMapaHorarioSemanal(Division division, LocalDate fechaInicio, LocalDate fechaFin);
    
    /**
     * Obtiene todas las divisiones disponibles para el mapa de horarios.
     * @return Lista de divisiones
     */
    List<Division> obtenerDivisionesParaMapa();
}
