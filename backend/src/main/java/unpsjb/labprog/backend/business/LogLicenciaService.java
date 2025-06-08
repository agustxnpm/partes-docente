package unpsjb.labprog.backend.business;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import unpsjb.labprog.backend.business.interfaces.ILogLicenciaService;
import unpsjb.labprog.backend.model.LogLicencia;
import unpsjb.labprog.backend.model.Licencia;
import unpsjb.labprog.backend.model.EstadoLicencia;

@Service
public class LogLicenciaService implements ILogLicenciaService {

    @Autowired
    private LogLicenciaRepository logLicenciaRepository;

    @Transactional
    public LogLicencia crearLog(Licencia licencia, EstadoLicencia estado, String mensaje) {
        LogLicencia log = new LogLicencia();
        log.setLicencia(licencia);
        log.setMensaje(mensaje);
        log.setFechaLog(LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")));
        
        return logLicenciaRepository.save(log);
    }

    public LogLicencia obtenerUltimoLog(Licencia licencia) {
        return logLicenciaRepository.findByLicenciaIdOrderByFechaLogDesc(licencia.getId()).stream()
                .findFirst()
                .orElse(null);
    }

    public List<LogLicencia> obtenerLicenciasEnOrdenPorFechaDesc(Long id) {
        return logLicenciaRepository.findByLicenciaIdOrderByFechaLogDesc(id);
    }
} 
