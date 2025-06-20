package unpsjb.labprog.backend.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import unpsjb.labprog.backend.business.interfaces.IArticuloLicenciaService;
import unpsjb.labprog.backend.model.ArticuloLicencia;

@Service
public class ArticuloLicenciaService implements IArticuloLicenciaService {

    @Autowired
    private ArticuloLicenciaRepository articuloLicenciaRepository;

    // Aplicando DIP: Dependemos de la abstracción específica IArticuloLicenciaMensajeBuilder

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

}
