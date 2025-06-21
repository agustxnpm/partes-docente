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
     * Obtiene todas las divisiones disponibles para el mapa de horarios.
     * 
     * @return Lista de divisiones
     */
    List<Division> obtenerDivisionesParaMapa();
}
