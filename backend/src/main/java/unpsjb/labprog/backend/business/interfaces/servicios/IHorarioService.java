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
     * Obtiene todos los horarios.
     * 
     * @return Lista de todos los horarios
     */
    List<Horario> findAll();

    // Métodos para el mapa de horarios

    /**
     * Genera un mapa de horarios semanal para una división específica.
     * 
     * @param division    La división para la cual generar el mapa
     * @param fechaInicio Fecha de inicio de la semana
     * @param fechaFin    Fecha de fin de la semana
     * @return El mapa de horarios semanal
     */
    MapaHorarioSemanalDTO generarMapaHorarioSemanal(Division division, LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Genera un mapa de horarios semanal para una división específica.
     * Este método maneja la conversión de parámetros y la búsqueda de la división.
     * 
     * @param divisionId     ID de la división para la cual generar el mapa
     * @param fechaInicioStr Fecha de inicio en formato String (ISO_LOCAL_DATE)
     * @param fechaFinStr    Fecha de fin en formato String (ISO_LOCAL_DATE)
     * @return El mapa de horarios semanal
     * @throws IllegalArgumentException si la división no se encuentra o las fechas son inválidas
     */
    MapaHorarioSemanalDTO generarMapaHorarioSemanal(Long divisionId, String fechaInicioStr, String fechaFinStr);

    /**
     * Obtiene todas las divisiones disponibles para el mapa de horarios.
     * 
     * @return Lista de divisiones
     */
    List<Division> obtenerDivisionesParaMapa();
}
