package unpsjb.labprog.backend.business;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import unpsjb.labprog.backend.business.interfaces.servicios.IReporteConceptoService;
import unpsjb.labprog.backend.dto.LicenciaReporteDTO;
import unpsjb.labprog.backend.dto.PersonaConceptoDTO;
import unpsjb.labprog.backend.dto.ReporteConceptoDTO;
import unpsjb.labprog.backend.model.Licencia;
import unpsjb.labprog.backend.model.Persona;

@Service
public class ReporteConceptoService implements IReporteConceptoService {

    @Autowired
    private PersonaService personaService;

    @Autowired
    private LicenciaService licenciaService;

    @Override
    public ReporteConceptoDTO generarReporteConcepto(int anio) {
        List<PersonaConceptoDTO> personas = obtenerPersonasConLicencias(anio);
        
        long totalLicencias = personas.stream()
                .mapToLong(p -> p.getLicencias().size())
                .sum();
        
        long totalDiasLicencia = personas.stream()
                .mapToLong(PersonaConceptoDTO::getTotalDiasLicencia)
                .sum();
        
        ReporteConceptoDTO reporte = new ReporteConceptoDTO();
        reporte.setAnio(anio);
        reporte.setFechaGeneracion(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        reporte.setTotalPersonas(personas.size());
        reporte.setTotalLicencias((int) totalLicencias);
        reporte.setTotalDiasLicencia((int) totalDiasLicencia);
        reporte.setPersonas(personas);
        
        return reporte;
    }

    private List<PersonaConceptoDTO> obtenerPersonasConLicencias(int anio) {
        List<Persona> todasLasPersonas = personaService.findAll();
        
        return todasLasPersonas.stream()
                .map(persona -> mapearPersonaConcepto(persona, anio))
                .filter(personaConcepto -> !personaConcepto.getLicencias().isEmpty())
                .collect(Collectors.toList());
    }

    private PersonaConceptoDTO mapearPersonaConcepto(Persona persona, int anio) {
        List<Licencia> licenciasDelAnio = licenciaService.findByPersonaAndYear(persona, anio);
        
        List<LicenciaReporteDTO> licenciasDTO = licenciasDelAnio.stream()
                .map(this::mapearLicenciaReporte)
                .collect(Collectors.toList());
        
        long totalDias = licenciasDTO.stream()
                .mapToLong(LicenciaReporteDTO::getDiasTomados)
                .sum();
        
        PersonaConceptoDTO personaConcepto = new PersonaConceptoDTO();
        personaConcepto.setDni(persona.getDni());
        personaConcepto.setNombre(persona.getNombre());
        personaConcepto.setApellido(persona.getApellido());
        personaConcepto.setCuil(persona.getCuil());
        personaConcepto.setTitulo(persona.getTitulo());
        personaConcepto.setTotalDiasLicencia((int) totalDias);
        personaConcepto.setLicencias(licenciasDTO);
        
        return personaConcepto;
    }

    private LicenciaReporteDTO mapearLicenciaReporte(Licencia licencia) {
        long diasTomados = ChronoUnit.DAYS.between(
            licencia.getPedidoDesde(), 
            licencia.getPedidoHasta()
        ) + 1; // +1 para incluir ambos días
        
        LicenciaReporteDTO licenciaDTO = new LicenciaReporteDTO();
        licenciaDTO.setArticuloLicencia(licencia.getArticuloLicencia().getArticulo());
        licenciaDTO.setDescripcion(licencia.getArticuloLicencia().getDescripcion());
        licenciaDTO.setFechaDesde(licencia.getPedidoDesde().toString());
        licenciaDTO.setFechaHasta(licencia.getPedidoHasta().toString());
        licenciaDTO.setDiasTomados((int) diasTomados);
        licenciaDTO.setEstado(licencia.getEstado().name());
        
        return licenciaDTO;
    }

    @Override
    public String exportarReporteCSV(int anio) {
        ReporteConceptoDTO reporte = generarReporteConcepto(anio);
        return exportarACSV(reporte);
    }

    @Override
    public byte[] exportarReportePDF(int anio) {
        ReporteConceptoDTO reporte = generarReporteConcepto(anio);
        return exportarAPDF(reporte);
    }

    private String exportarACSV(ReporteConceptoDTO reporte) {
        StringBuilder csv = new StringBuilder();
        
        // Header del reporte
        csv.append("REPORTE DE CONCEPTO DE LICENCIAS - AÑO ").append(reporte.getAnio()).append("\n");
        csv.append("Fecha de Generación: ").append(reporte.getFechaGeneracion()).append("\n");
        csv.append("Total de Personas: ").append(reporte.getTotalPersonas()).append("\n");
        csv.append("Total de Licencias: ").append(reporte.getTotalLicencias()).append("\n");
        csv.append("Total de Días de Licencia: ").append(reporte.getTotalDiasLicencia()).append("\n\n");
        
        // Header de datos
        csv.append("DNI,Nombre,Apellido,CUIL,Título,Total Días,Artículo,Descripción,Fecha Desde,Fecha Hasta,Días Tomados\n");
        
        // Datos de personas y licencias
        for (PersonaConceptoDTO persona : reporte.getPersonas()) {
            for (LicenciaReporteDTO licencia : persona.getLicencias()) {
                csv.append(persona.getDni()).append(",")
                   .append(persona.getNombre()).append(",")
                   .append(persona.getApellido()).append(",")
                   .append(persona.getCuil()).append(",")
                   .append(persona.getTitulo()).append(",")
                   .append(persona.getTotalDiasLicencia()).append(",")
                   .append(licencia.getArticuloLicencia()).append(",")
                   .append("\"").append(licencia.getDescripcion()).append("\"").append(",")
                   .append(licencia.getFechaDesde()).append(",")
                   .append(licencia.getFechaHasta()).append(",")
                   .append(licencia.getDiasTomados()).append("\n");
            }
            if (persona.getLicencias().isEmpty()) {
                csv.append(persona.getDni()).append(",")
                   .append(persona.getNombre()).append(",")
                   .append(persona.getApellido()).append(",")
                   .append(persona.getCuil()).append(",")
                   .append(persona.getTitulo()).append(",")
                   .append("0,,,,,\n");
            }
        }
        
        return csv.toString();
    }

    private byte[] exportarAPDF(ReporteConceptoDTO reporte) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            // Título del reporte
            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
            Paragraph titulo = new Paragraph("REPORTE DE CONCEPTO DE LICENCIAS - AÑO " + reporte.getAnio(), titleFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);
            document.add(new Paragraph(" ")); // Espacio

            // Información del reporte
            Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
            document.add(new Paragraph("Fecha de Generación: " + reporte.getFechaGeneracion(), normalFont));
            document.add(new Paragraph("Total de Personas: " + reporte.getTotalPersonas(), normalFont));
            document.add(new Paragraph("Total de Licencias: " + reporte.getTotalLicencias(), normalFont));
            document.add(new Paragraph("Total de Días de Licencia: " + reporte.getTotalDiasLicencia(), normalFont));
            document.add(new Paragraph(" ")); // Espacio

            // Tabla de datos
            if (!reporte.getPersonas().isEmpty()) {
                float[] columnWidths = {1f, 2f, 2f, 1.5f, 2f, 1f, 1.5f, 1.5f, 1.5f, 1f};
                PdfPTable table = new PdfPTable(columnWidths);
                table.setWidthPercentage(100);

                // Headers de la tabla
                Font headerFont = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD);
                table.addCell(createHeaderCell("DNI", headerFont));
                table.addCell(createHeaderCell("Nombre", headerFont));
                table.addCell(createHeaderCell("Apellido", headerFont));
                table.addCell(createHeaderCell("CUIL", headerFont));
                table.addCell(createHeaderCell("Título", headerFont));
                table.addCell(createHeaderCell("Total Días", headerFont));
                table.addCell(createHeaderCell("Artículo", headerFont));
                table.addCell(createHeaderCell("Desde", headerFont));
                table.addCell(createHeaderCell("Hasta", headerFont));
                table.addCell(createHeaderCell("Días", headerFont));

                // Datos de las personas y licencias
                Font cellFont = new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.NORMAL);
                for (PersonaConceptoDTO persona : reporte.getPersonas()) {
                    if (!persona.getLicencias().isEmpty()) {
                        for (LicenciaReporteDTO licencia : persona.getLicencias()) {
                            // Datos de la persona (repetidos en cada fila de licencia)
                            table.addCell(createCell(String.valueOf(persona.getDni()), cellFont));
                            table.addCell(createCell(persona.getNombre(), cellFont));
                            table.addCell(createCell(persona.getApellido(), cellFont));
                            table.addCell(createCell(persona.getCuil() != null ? persona.getCuil() : "", cellFont));
                            table.addCell(createCell(persona.getTitulo() != null ? persona.getTitulo() : "", cellFont));
                            table.addCell(createCell(String.valueOf(persona.getTotalDiasLicencia()), cellFont));
                            
                            // Datos de la licencia
                            table.addCell(createCell(licencia.getArticuloLicencia(), cellFont));
                            table.addCell(createCell(licencia.getFechaDesde().toString(), cellFont));
                            table.addCell(createCell(licencia.getFechaHasta().toString(), cellFont));
                            table.addCell(createCell(String.valueOf(licencia.getDiasTomados()), cellFont));
                        }
                    } else {
                        // Persona sin licencias
                        table.addCell(createCell(String.valueOf(persona.getDni()), cellFont));
                        table.addCell(createCell(persona.getNombre(), cellFont));
                        table.addCell(createCell(persona.getApellido(), cellFont));
                        table.addCell(createCell(persona.getCuil() != null ? persona.getCuil() : "", cellFont));
                        table.addCell(createCell(persona.getTitulo() != null ? persona.getTitulo() : "", cellFont));
                        table.addCell(createCell("0", cellFont));
                        table.addCell(createCell("", cellFont));
                        table.addCell(createCell("", cellFont));
                        table.addCell(createCell("", cellFont));
                        table.addCell(createCell("", cellFont));
                    }
                }
                
                document.add(table);
            } else {
                Paragraph noData = new Paragraph("No se encontraron datos para el año especificado.", normalFont);
                noData.setAlignment(Element.ALIGN_CENTER);
                document.add(noData);
            }

            document.close();
            return baos.toByteArray();
            
        } catch (Exception e) {
            throw new RuntimeException("Error al generar PDF: " + e.getMessage(), e);
        }
    }

    private PdfPCell createHeaderCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        return cell;
    }

    private PdfPCell createCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(3);
        return cell;
    }
}
