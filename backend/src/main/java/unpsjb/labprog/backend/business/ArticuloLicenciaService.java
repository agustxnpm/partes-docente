package unpsjb.labprog.backend.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import unpsjb.labprog.backend.business.utilidades.MensajeBuilder;
import unpsjb.labprog.backend.model.ArticuloLicencia;

@Service
public class ArticuloLicenciaService {

    @Autowired
    private ArticuloLicenciaRepository articuloLicenciaRepository;

    @Autowired
    private MensajeBuilder mensajeBuilder;

    public ArticuloLicencia findById(Long id) {
        return articuloLicenciaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ArticuloLicencia no encontrado"));
    }

    public List<ArticuloLicencia> findAll() {
        return articuloLicenciaRepository.findAll();
    }

    public ArticuloLicencia findByArticulo(String codigo) {
        return articuloLicenciaRepository.findByArticulo(codigo)
                .orElseThrow(() -> new IllegalArgumentException("ArticuloLicencia no encontrado"));
    }

    public String getMensajeExito(ArticuloLicencia articuloLicencia) {
        return mensajeBuilder.generarMensajeExitoArticuloLicenciaCreado(articuloLicencia);
    }

    public String getMensajeExitoBorrado(ArticuloLicencia articuloLicencia) {
        return mensajeBuilder.generarMensajeExitoArticuloLicenciaBorrado(articuloLicencia);
    }

    public String getMensajeExitoActualizacion(ArticuloLicencia articuloLicencia) {
        return mensajeBuilder.generarMensajeExitoArticuloLicenciaActualizado(articuloLicencia);
    }

}
