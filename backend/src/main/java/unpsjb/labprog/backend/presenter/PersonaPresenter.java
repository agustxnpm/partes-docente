package unpsjb.labprog.backend.presenter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import unpsjb.labprog.backend.Response;
import unpsjb.labprog.backend.business.interfaces.mensajes.IPersonaMensajeBuilder;
import unpsjb.labprog.backend.business.interfaces.servicios.IPersonaService;
import unpsjb.labprog.backend.business.interfaces.servicios.IReporteConceptoService;
import unpsjb.labprog.backend.dto.ReporteConceptoDTO;
import unpsjb.labprog.backend.model.Persona;

/**
 * Presenter para la gestión de personas.
 * Aplica el principio DIP (Dependency Inversion Principle) 
 */
@RestController
@RequestMapping("personas")
public class PersonaPresenter {

    @Autowired
    private IPersonaService personaService;

    @Autowired
    private IPersonaMensajeBuilder personaMensajeBuilder;

    @Autowired
    private IReporteConceptoService reporteConceptoService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> createPersona(@RequestBody Persona persona) {

        try {
            personaService.save(persona);
            return Response.ok(persona, personaMensajeBuilder.generarMensajeExitoCreacion(persona));
        } catch (IllegalArgumentException e) {
            return Response.badRequest(persona, e.getMessage());
        }

    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> listarPersonas() {
        try {
            return Response.ok(personaService.findAll());
        } catch (IllegalArgumentException e) {
            return Response.badRequest(null, e.getMessage());
        }
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public ResponseEntity<Object> findByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Response.ok(personaService.findByPage(page, size));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updatePersona(@PathVariable("id") Long id, @RequestBody Persona persona) {
        try {
            persona.setId(id);
            Persona updatedPersona = personaService.update(persona);
            return Response.ok(updatedPersona, personaMensajeBuilder.generarMensajeExitoActualizacion(updatedPersona));
        } catch (IllegalArgumentException e) {
            return Response.badRequest(persona, e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deletePersona(@PathVariable("id") Long id) {
        try {
            Persona persona = personaService.findById(id);
            personaService.delete(persona);
            return Response.ok(persona, personaMensajeBuilder.generarMensajeExitoBorrado(persona));
        } catch (IllegalArgumentException e) {
            return Response.badRequest(null, e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getPersonaById(@PathVariable("id") Long id) {
        try {
            Persona persona = personaService.findById(id);
            return Response.ok(persona);
        } catch (IllegalArgumentException e) {
            return Response.badRequest(null, e.getMessage());
        }
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<Object> search(@RequestParam("term") String term) {
        try {
            List<Persona> personas = personaService.search(term);
            return Response.ok(personas, "Búsqueda completada exitosamente");
        } catch (Exception e) {
            return Response.internalServerError(
                    "Error en la búsqueda: " + e.getMessage(), null);
        }
    }

    // === ENDPOINTS PARA REPORTE DE CONCEPTO ===

    /**
     * Genera el reporte de concepto anual para todas las personas
     */
    @RequestMapping(value = "/reporte-concepto/{anio}", method = RequestMethod.GET)
    public ResponseEntity<Object> generarReporteConceptoAnual(@PathVariable("anio") int anio) {
        try {
            ReporteConceptoDTO reporte = reporteConceptoService.generarReporteConcepto(anio);
            return Response.ok(reporte, "Reporte de concepto generado exitosamente");
        } catch (Exception e) {
            return Response.internalServerError(
                    "Error al generar el reporte de concepto: " + e.getMessage(), null);
        }
    }


    /**
     * Exporta el reporte de concepto a formato CSV
     */
    @RequestMapping(value = "/reporte-concepto/{anio}/export/csv", method = RequestMethod.GET)
    public ResponseEntity<Object> exportarReporteCSV(@PathVariable("anio") int anio) {
        try {
            String csvContent = reporteConceptoService.exportarReporteCSV(anio);
            return Response.ok(csvContent.getBytes(), "Archivo CSV generado exitosamente: reporte-concepto-" + anio + ".csv");
        } catch (Exception e) {
            return Response.internalServerError("Error al generar el archivo", null);
        }
    }

    /**
     * Exporta el reporte de concepto a formato PDF
     */
    @RequestMapping(value = "/reporte-concepto/{anio}/export/pdf", method = RequestMethod.GET)
    public ResponseEntity<Object> exportarReportePDF(@PathVariable("anio") int anio) {
        try {
            byte[] pdfContent = reporteConceptoService.exportarReportePDF(anio);
            
            return Response.ok(pdfContent, "Archivo PDF generado exitosamente: reporte-concepto-" + anio + ".pdf");
        } catch (Exception e) {
            return Response.internalServerError("Error al generar el archivo", null);
        }
    }

}
