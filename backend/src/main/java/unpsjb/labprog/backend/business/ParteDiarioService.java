package unpsjb.labprog.backend.business;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import unpsjb.labprog.backend.business.interfaces.servicios.ILicenciaService;
import unpsjb.labprog.backend.business.interfaces.servicios.IParteDiarioService;
import unpsjb.labprog.backend.dto.DocenteLicenciaDTO;
import unpsjb.labprog.backend.dto.ParteDiarioDTO;
import unpsjb.labprog.backend.model.EstadoLicencia;
import unpsjb.labprog.backend.model.Licencia;

/**
 * Implementación del servicio de parte diario de licencias.
 * Se encarga de generar el parte diario de licencias activas para una fecha específica.
 */
@Service
public class ParteDiarioService implements IParteDiarioService {

    @Autowired
    private ILicenciaService licenciaService;

    /**
     * Genera el parte diario de licencias para una fecha específica.
     * Solo incluye licencias válidas que estén activas en la fecha solicitada.
     * 
     * @param fecha La fecha para la cual generar el parte diario
     * @return ParteDiarioDTO con las licencias activas para esa fecha
     */
    @Override
    public ParteDiarioDTO generarParteDiario(LocalDate fecha) {
        List<Licencia> licenciasValidas = obtenerLicenciasValidasParaFecha(fecha);
        List<DocenteLicenciaDTO> docentesDTO = convertirLicenciasADTOs(licenciasValidas);
        return crearParteDiarioDTO(fecha, docentesDTO);
    }

    /**
     * Obtiene todas las licencias válidas activas para una fecha específica
     */
    private List<Licencia> obtenerLicenciasValidasParaFecha(LocalDate fecha) {
        List<Licencia> licenciasActivas = obtenerLicenciasActivasEnFecha(fecha);
        return filtrarLicenciasValidas(licenciasActivas);
    }

    /**
     * Obtiene todas las licencias activas en una fecha específica
     */
    private List<Licencia> obtenerLicenciasActivasEnFecha(LocalDate fecha) {
        return licenciaService.findLicenciasActivasEnFecha(fecha);
    }

    /**
     * Filtra las licencias para obtener solo las que tienen estado VÁLIDA
     */
    private List<Licencia> filtrarLicenciasValidas(List<Licencia> licencias) {
        return licencias.stream()
                .filter(licencia -> EstadoLicencia.VALIDA.equals(licencia.getEstado()))
                .collect(Collectors.toList());
    }

    /**
     * Convierte una lista de licencias a una lista de DTOs de docente-licencia
     */
    private List<DocenteLicenciaDTO> convertirLicenciasADTOs(List<Licencia> licencias) {
        return licencias.stream()
                .map(this::convertirLicenciaADocenteLicenciaDTO)
                .collect(Collectors.toList());
    }

    /**
     * Crea el DTO final del parte diario con la fecha y la lista de docentes
     */
    private ParteDiarioDTO crearParteDiarioDTO(LocalDate fecha, List<DocenteLicenciaDTO> docentesDTO) {
        return new ParteDiarioDTO(fecha, docentesDTO);
    }
    
    /**
     * Convierte una entidad Licencia a un DocenteLicenciaDTO.
     * 
     * @param licencia La licencia a convertir
     * @return DocenteLicenciaDTO con los datos del docente y la licencia
     */
    private DocenteLicenciaDTO convertirLicenciaADocenteLicenciaDTO(Licencia licencia) {
        return new DocenteLicenciaDTO(
                licencia.getPersona().getDni(),
                licencia.getPersona().getNombre(),
                licencia.getPersona().getApellido(),
                licencia.getArticuloLicencia().getArticulo(),
                licencia.getArticuloLicencia().getDescripcion(),
                licencia.getPedidoDesde(),
                licencia.getPedidoHasta()
        );
    }
}
