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
        // Obtener todas las licencias activas en la fecha específica
        List<Licencia> licenciasActivas = licenciaService.findLicenciasActivasEnFecha(fecha);
        
        // Filtrar solo las licencias válidas
        List<Licencia> licenciasValidas = licenciasActivas.stream()
                .filter(licencia -> EstadoLicencia.VALIDA.equals(licencia.getEstado()))
                .collect(Collectors.toList());
        
        // Convertir las licencias a DTOs de docente-licencia
        List<DocenteLicenciaDTO> docentesDTO = licenciasValidas.stream()
                .map(this::convertirLicenciaADocenteLicenciaDTO)
                .collect(Collectors.toList());
        
        // Crear y retornar el ParteDiarioDTO
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
