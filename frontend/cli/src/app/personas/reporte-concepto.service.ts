import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DataPackage } from '../data-package';

@Injectable({
  providedIn: 'root'
})
export class ReporteConceptoService {
  private readonly apiUrl = 'rest/personas/reporte-concepto';

  constructor(private http: HttpClient) {}

  /**
   * Genera el reporte de concepto para un año específico
   * @param anio Año para el cual generar el reporte
   * @returns Observable con DataPackage que contiene ReporteConceptoDTO
   */
  generarReporteAnual(anio: number): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.apiUrl}/${anio}`);
  }

  /**
   * Exporta el reporte de concepto a formato CSV
   * @param anio Año del reporte a exportar
   * @returns Observable con DataPackage que contiene el archivo CSV en formato Base64
   */
  exportarReporteCSV(anio: number): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.apiUrl}/${anio}/export/csv`);
  }

  /**
   * Exporta el reporte de concepto a formato PDF
   * @param anio Año del reporte a exportar
   * @returns Observable con DataPackage que contiene el archivo PDF en formato Base64
   */
  exportarReportePDF(anio: number): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.apiUrl}/${anio}/export/pdf`);
  }

  /**
   * Obtiene la lista de personas con licencias en un año específico
   * @param anio Año para consultar
   * @returns Observable con DataPackage que contiene la lista de personas
   */
  obtenerPersonasConLicencias(anio: number): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.apiUrl}/personas/${anio}`);
  }

  /**
   * Decodifica contenido Base64 y crea un blob para descarga
   * @param base64Content Contenido en Base64
   * @param filename Nombre del archivo
   * @param mimeType Tipo MIME del archivo
   */
  descargarArchivo(base64Content: string, filename: string, mimeType: string): void {
    try {
      // Decodificar el contenido Base64
      const byteCharacters = atob(base64Content);
      const byteNumbers = new Array(byteCharacters.length);
      for (let i = 0; i < byteCharacters.length; i++) {
        byteNumbers[i] = byteCharacters.charCodeAt(i);
      }
      const byteArray = new Uint8Array(byteNumbers);
      
      // Crear blob
      const blob = new Blob([byteArray], { type: mimeType });
      
      // Crear URL y descargar
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = filename;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      window.URL.revokeObjectURL(url);
    } catch (error) {
      console.error('Error al decodificar el archivo:', error);
      throw new Error('Error al procesar el archivo descargado');
    }
  }
}
