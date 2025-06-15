package unpsjb.labprog.backend.business.interfaces;

import unpsjb.labprog.backend.dto.ReporteConceptoDTO;

public interface IReporteConceptoService {
    
    /**
     * Genera un reporte de concepto de licencias para un año específico
     * @param anio El año para el cual generar el reporte
     * @return ReporteConceptoDTO con los datos del reporte
     */
    ReporteConceptoDTO generarReporteConcepto(int anio);
    
    /**
     * Exporta el reporte de concepto a formato CSV
     * @param anio El año del reporte a exportar
     * @return String con el contenido CSV
     */
    String exportarReporteCSV(int anio);
    
    /**
     * Exporta el reporte de concepto a formato PDF
     * @param anio El año del reporte a exportar
     * @return byte[] con el contenido PDF
     */
    byte[] exportarReportePDF(int anio);
}
