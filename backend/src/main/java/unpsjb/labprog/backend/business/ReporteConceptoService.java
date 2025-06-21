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
        ReporteConceptoDTO reporte = crearReporteBase(anio);
        configurarTotalesReporte(reporte, personas);
        reporte.setPersonas(personas);
        return reporte;
    }

    /**
     * Crea la estructura base del reporte con información básica
     */
    private ReporteConceptoDTO crearReporteBase(int anio) {
        ReporteConceptoDTO reporte = new ReporteConceptoDTO();
        reporte.setAnio(anio);
        reporte.setFechaGeneracion(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        return reporte;
    }

    /**
     * Configura los totales del reporte basándose en las personas con licencias
     */
    private void configurarTotalesReporte(ReporteConceptoDTO reporte, List<PersonaConceptoDTO> personas) {
        reporte.setTotalPersonas(personas.size());
        reporte.setTotalLicencias(calcularTotalLicencias(personas));
        reporte.setTotalDiasLicencia(calcularTotalDiasLicencia(personas));
    }

    /**
     * Calcula el total de licencias de todas las personas
     */
    private int calcularTotalLicencias(List<PersonaConceptoDTO> personas) {
        return (int) personas.stream()
                .mapToLong(p -> p.getLicencias().size())
                .sum();
    }

    /**
     * Calcula el total de días de licencia de todas las personas
     */
    private int calcularTotalDiasLicencia(List<PersonaConceptoDTO> personas) {
        return (int) personas.stream()
                .mapToLong(PersonaConceptoDTO::getTotalDiasLicencia)
                .sum();
    }

    private List<PersonaConceptoDTO> obtenerPersonasConLicencias(int anio) {
        List<Persona> todasLasPersonas = personaService.findAll();
        
        return todasLasPersonas.stream()
                .map(persona -> mapearPersonaConcepto(persona, anio))
                .filter(personaConcepto -> !personaConcepto.getLicencias().isEmpty())
                .collect(Collectors.toList());
    }

    private PersonaConceptoDTO mapearPersonaConcepto(Persona persona, int anio) {
        List<LicenciaReporteDTO> licenciasDTO = obtenerLicenciasDTODelAnio(persona, anio);
        int totalDias = calcularTotalDiasPersona(licenciasDTO);
        return crearPersonaConceptoDTO(persona, licenciasDTO, totalDias);
    }

    /**
     * Obtiene las licencias del año convertidas a DTO para una persona
     */
    private List<LicenciaReporteDTO> obtenerLicenciasDTODelAnio(Persona persona, int anio) {
        List<Licencia> licenciasDelAnio = licenciaService.findByPersonaAndYear(persona, anio);
        return convertirLicenciasADTO(licenciasDelAnio);
    }

    /**
     * Convierte una lista de licencias a DTOs de reporte
     */
    private List<LicenciaReporteDTO> convertirLicenciasADTO(List<Licencia> licencias) {
        return licencias.stream()
                .map(this::mapearLicenciaReporte)
                .collect(Collectors.toList());
    }

    /**
     * Calcula el total de días de licencia para una persona
     */
    private int calcularTotalDiasPersona(List<LicenciaReporteDTO> licenciasDTO) {
        return (int) licenciasDTO.stream()
                .mapToLong(LicenciaReporteDTO::getDiasTomados)
                .sum();
    }

    /**
     * Crea el DTO de PersonaConcepto con todos sus datos
     */
    private PersonaConceptoDTO crearPersonaConceptoDTO(Persona persona, List<LicenciaReporteDTO> licenciasDTO, int totalDias) {
        PersonaConceptoDTO personaConcepto = new PersonaConceptoDTO();
        configurarDatosPersona(personaConcepto, persona);
        configurarDatosLicencias(personaConcepto, licenciasDTO, totalDias);
        return personaConcepto;
    }

    /**
     * Configura los datos básicos de la persona en el DTO
     */
    private void configurarDatosPersona(PersonaConceptoDTO dto, Persona persona) {
        dto.setDni(persona.getDni());
        dto.setNombre(persona.getNombre());
        dto.setApellido(persona.getApellido());
        dto.setCuil(persona.getCuil());
        dto.setTitulo(persona.getTitulo());
    }

    /**
     * Configura los datos de licencias en el DTO
     */
    private void configurarDatosLicencias(PersonaConceptoDTO dto, List<LicenciaReporteDTO> licencias, int totalDias) {
        dto.setTotalDiasLicencia(totalDias);
        dto.setLicencias(licencias);
    }

    private LicenciaReporteDTO mapearLicenciaReporte(Licencia licencia) {
        int diasTomados = calcularDiasTomados(licencia);
        return crearLicenciaReporteDTO(licencia, diasTomados);
    }

    /**
     * Calcula los días tomados en una licencia (inclusive)
     */
    private int calcularDiasTomados(Licencia licencia) {
        long dias = ChronoUnit.DAYS.between(
            licencia.getPedidoDesde(), 
            licencia.getPedidoHasta()
        ) + 1; // +1 para incluir ambos días
        return (int) dias;
    }

    /**
     * Crea el DTO de LicenciaReporte con todos los datos de la licencia
     */
    private LicenciaReporteDTO crearLicenciaReporteDTO(Licencia licencia, int diasTomados) {
        LicenciaReporteDTO licenciaDTO = new LicenciaReporteDTO();
        configurarDatosArticulo(licenciaDTO, licencia);
        configurarDatosFechas(licenciaDTO, licencia, diasTomados);
        licenciaDTO.setEstado(licencia.getEstado().name());
        return licenciaDTO;
    }

    /**
     * Configura los datos del artículo de licencia en el DTO
     */
    private void configurarDatosArticulo(LicenciaReporteDTO dto, Licencia licencia) {
        dto.setArticuloLicencia(licencia.getArticuloLicencia().getArticulo());
        dto.setDescripcion(licencia.getArticuloLicencia().getDescripcion());
    }

    /**
     * Configura los datos de fechas y días en el DTO
     */
    private void configurarDatosFechas(LicenciaReporteDTO dto, Licencia licencia, int diasTomados) {
        dto.setFechaDesde(licencia.getPedidoDesde().toString());
        dto.setFechaHasta(licencia.getPedidoHasta().toString());
        dto.setDiasTomados(diasTomados);
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
        
        construirHeaderCSV(csv, reporte);
        construirColumnasCSV(csv);
        construirDatosCSV(csv, reporte.getPersonas());
        
        return csv.toString();
    }

    /**
     * Construye el header del CSV con información del reporte
     */
    private void construirHeaderCSV(StringBuilder csv, ReporteConceptoDTO reporte) {
        csv.append("REPORTE DE CONCEPTO DE LICENCIAS - AÑO ").append(reporte.getAnio()).append("\n");
        csv.append("Fecha de Generación: ").append(reporte.getFechaGeneracion()).append("\n");
        csv.append("Total de Personas: ").append(reporte.getTotalPersonas()).append("\n");
        csv.append("Total de Licencias: ").append(reporte.getTotalLicencias()).append("\n");
        csv.append("Total de Días de Licencia: ").append(reporte.getTotalDiasLicencia()).append("\n\n");
    }

    /**
     * Construye los nombres de las columnas del CSV
     */
    private void construirColumnasCSV(StringBuilder csv) {
        csv.append("DNI,Nombre,Apellido,CUIL,Título,Total Días,Artículo,Descripción,Fecha Desde,Fecha Hasta,Días Tomados\n");
    }

    /**
     * Construye todas las filas de datos del CSV
     */
    private void construirDatosCSV(StringBuilder csv, List<PersonaConceptoDTO> personas) {
        for (PersonaConceptoDTO persona : personas) {
            if (!persona.getLicencias().isEmpty()) {
                construirFilasPersonaConLicencias(csv, persona);
            } else {
                construirFilaPersonaSinLicencias(csv, persona);
            }
        }
    }

    /**
     * Construye las filas CSV para una persona con licencias
     */
    private void construirFilasPersonaConLicencias(StringBuilder csv, PersonaConceptoDTO persona) {
        for (LicenciaReporteDTO licencia : persona.getLicencias()) {
            construirFilaCompleta(csv, persona, licencia);
        }
    }

    /**
     * Construye una fila completa del CSV con datos de persona y licencia
     */
    private void construirFilaCompleta(StringBuilder csv, PersonaConceptoDTO persona, LicenciaReporteDTO licencia) {
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

    /**
     * Construye una fila CSV para persona sin licencias
     */
    private void construirFilaPersonaSinLicencias(StringBuilder csv, PersonaConceptoDTO persona) {
        csv.append(persona.getDni()).append(",")
           .append(persona.getNombre()).append(",")
           .append(persona.getApellido()).append(",")
           .append(persona.getCuil()).append(",")
           .append(persona.getTitulo()).append(",")
           .append("0,,,,,\n");
    }

    private byte[] exportarAPDF(ReporteConceptoDTO reporte) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            construirContenidoPDF(document, reporte);

            document.close();
            return baos.toByteArray();
            
        } catch (Exception e) {
            throw new RuntimeException("Error al generar PDF: " + e.getMessage(), e);
        }
    }

    /**
     * Construye todo el contenido del PDF
     */
    private void construirContenidoPDF(Document document, ReporteConceptoDTO reporte) throws Exception {
        agregarTituloPDF(document, reporte);
        agregarInformacionReportePDF(document, reporte);
        agregarTablaDatosPDF(document, reporte);
    }

    /**
     * Agrega el título principal del PDF
     */
    private void agregarTituloPDF(Document document, ReporteConceptoDTO reporte) throws Exception {
        Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
        Paragraph titulo = new Paragraph("REPORTE DE CONCEPTO DE LICENCIAS - AÑO " + reporte.getAnio(), titleFont);
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);
        document.add(new Paragraph(" ")); // Espacio
    }

    /**
     * Agrega la información general del reporte
     */
    private void agregarInformacionReportePDF(Document document, ReporteConceptoDTO reporte) throws Exception {
        Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
        document.add(new Paragraph("Fecha de Generación: " + reporte.getFechaGeneracion(), normalFont));
        document.add(new Paragraph("Total de Personas: " + reporte.getTotalPersonas(), normalFont));
        document.add(new Paragraph("Total de Licencias: " + reporte.getTotalLicencias(), normalFont));
        document.add(new Paragraph("Total de Días de Licencia: " + reporte.getTotalDiasLicencia(), normalFont));
        document.add(new Paragraph(" ")); // Espacio
    }

    /**
     * Agrega la tabla con los datos de personas y licencias
     */
    private void agregarTablaDatosPDF(Document document, ReporteConceptoDTO reporte) throws Exception {
        if (!reporte.getPersonas().isEmpty()) {
            PdfPTable table = crearTablaPDF();
            agregarHeadersTablaPDF(table);
            llenarDatosTablaPDF(table, reporte.getPersonas());
            document.add(table);
        } else {
            agregarMensajeSinDatos(document);
        }
    }

    /**
     * Crea la tabla PDF con el formato correcto
     */
    private PdfPTable crearTablaPDF() {
        float[] columnWidths = {1f, 2f, 2f, 1.5f, 2f, 1f, 1.5f, 1.5f, 1.5f, 1f};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);
        return table;
    }

    /**
     * Agrega los headers de la tabla PDF
     */
    private void agregarHeadersTablaPDF(PdfPTable table) {
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
    }

    /**
     * Llena la tabla con los datos de personas y licencias
     */
    private void llenarDatosTablaPDF(PdfPTable table, List<PersonaConceptoDTO> personas) {
        Font cellFont = new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.NORMAL);
        
        for (PersonaConceptoDTO persona : personas) {
            if (!persona.getLicencias().isEmpty()) {
                agregarFilasPersonaConLicencias(table, persona, cellFont);
            } else {
                agregarFilaPersonaSinLicencias(table, persona, cellFont);
            }
        }
    }

    /**
     * Agrega las filas de una persona que tiene licencias
     */
    private void agregarFilasPersonaConLicencias(PdfPTable table, PersonaConceptoDTO persona, Font cellFont) {
        for (LicenciaReporteDTO licencia : persona.getLicencias()) {
            agregarFilaCompleta(table, persona, licencia, cellFont);
        }
    }

    /**
     * Agrega una fila completa con datos de persona y licencia
     */
    private void agregarFilaCompleta(PdfPTable table, PersonaConceptoDTO persona, LicenciaReporteDTO licencia, Font cellFont) {
        // Datos de la persona
        agregarCeldasPersona(table, persona, cellFont);
        
        // Datos de la licencia
        table.addCell(createCell(licencia.getArticuloLicencia(), cellFont));
        table.addCell(createCell(licencia.getFechaDesde().toString(), cellFont));
        table.addCell(createCell(licencia.getFechaHasta().toString(), cellFont));
        table.addCell(createCell(String.valueOf(licencia.getDiasTomados()), cellFont));
    }

    /**
     * Agrega una fila para persona sin licencias
     */
    private void agregarFilaPersonaSinLicencias(PdfPTable table, PersonaConceptoDTO persona, Font cellFont) {
        agregarCeldasPersona(table, persona, cellFont);
        
        // Celdas vacías para licencias
        table.addCell(createCell("", cellFont));
        table.addCell(createCell("", cellFont));
        table.addCell(createCell("", cellFont));
        table.addCell(createCell("", cellFont));
    }

    /**
     * Agrega las celdas con datos básicos de la persona
     */
    private void agregarCeldasPersona(PdfPTable table, PersonaConceptoDTO persona, Font cellFont) {
        table.addCell(createCell(String.valueOf(persona.getDni()), cellFont));
        table.addCell(createCell(persona.getNombre(), cellFont));
        table.addCell(createCell(persona.getApellido(), cellFont));
        table.addCell(createCell(persona.getCuil() != null ? persona.getCuil() : "", cellFont));
        table.addCell(createCell(persona.getTitulo() != null ? persona.getTitulo() : "", cellFont));
        table.addCell(createCell(String.valueOf(persona.getTotalDiasLicencia()), cellFont));
    }

    /**
     * Agrega mensaje cuando no hay datos para mostrar
     */
    private void agregarMensajeSinDatos(Document document) throws Exception {
        Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
        Paragraph noData = new Paragraph("No se encontraron datos para el año especificado.", normalFont);
        noData.setAlignment(Element.ALIGN_CENTER);
        document.add(noData);
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
