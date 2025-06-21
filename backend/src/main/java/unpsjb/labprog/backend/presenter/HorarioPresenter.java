package unpsjb.labprog.backend.presenter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import unpsjb.labprog.backend.Response;
import unpsjb.labprog.backend.business.interfaces.servicios.IHorarioService;
import unpsjb.labprog.backend.dto.MapaHorarioSemanalDTO;
import unpsjb.labprog.backend.model.Division;
import unpsjb.labprog.backend.model.Horario;

@RestController
@RequestMapping("horarios")
public class HorarioPresenter {

    @Autowired
    private IHorarioService horarioService;

    
    // Endpoints para mapa de horarios
    
    @RequestMapping(value = "/mapa", method = RequestMethod.GET)
    public ResponseEntity<Object> obtenerMapaHorarioSemanal(
            @RequestParam("divisionId") Long divisionId,
            @RequestParam("fechaInicio") String fechaInicioStr,
            @RequestParam("fechaFin") String fechaFinStr) {
        try {
            MapaHorarioSemanalDTO mapa = horarioService.generarMapaHorarioSemanal(
                divisionId, fechaInicioStr, fechaFinStr);
            return Response.ok(mapa);
        } catch (Exception e) {
            return Response.badRequest(null, "Error al generar mapa de horarios: " + e.getMessage());
        }
    }
    
    @RequestMapping(value = "/divisiones", method = RequestMethod.GET)
    public ResponseEntity<Object> obtenerDivisionesDisponibles() {
        try {
            List<Division> divisiones = horarioService.obtenerDivisionesParaMapa();
            return Response.ok(divisiones);
        } catch (Exception e) {
            return Response.badRequest(null, "Error al obtener divisiones: " + e.getMessage());
        }
    }
    
    @RequestMapping(value = "/disponibles", method = RequestMethod.GET)
    public ResponseEntity<Object> obtenerHorariosDisponibles() {
        try {
            List<Horario> horarios = horarioService.findAll();
            return Response.ok(horarios);
        } catch (Exception e) {
            return Response.badRequest(null, "Error al obtener horarios: " + e.getMessage());
        }
    }
}
