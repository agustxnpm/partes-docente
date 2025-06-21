package unpsjb.labprog.backend.business.interfaces.servicios;

import java.time.LocalDate;

import unpsjb.labprog.backend.dto.ParteDiarioDTO;

/**
 * Interfaz para el servicio de parte diario de licencias.
 * Define las operaciones disponibles para la generación de partes diarios.
 */
public interface IParteDiarioService {

    /**
     * Genera el parte diario de licencias para una fecha específica.
     * @param fecha La fecha para la cual generar el parte diario
     * @return ParteDiarioDTO con las licencias activas para esa fecha
     */
    ParteDiarioDTO generarParteDiario(LocalDate fecha);
}
