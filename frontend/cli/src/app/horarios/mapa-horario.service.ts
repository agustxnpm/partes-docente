import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MapaHorarioSemanal, HorarioMapa } from './mapa-horario';
import { Horario } from './horario';
import { Division } from '../divisiones/division';
import { DataPackage } from '../data-package';

@Injectable({
  providedIn: 'root'
})
export class HorarioService {
  private apiUrl = 'rest/horarios';

  constructor(private http: HttpClient) { }

  /**
   * Obtiene todos los horarios disponibles
   */
  obtenerHorariosDisponibles(): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.apiUrl}/disponibles`);
  }

  /**
   * Obtiene todas las divisiones disponibles para generar mapas de horarios
   */
  obtenerDivisionesDisponibles(): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.apiUrl}/divisiones`);
  }

  /**
   * Genera el mapa de horarios semanal para una división específica
   */
  generarMapaHorarioSemanal(divisionId: number, fechaInicio: string, fechaFin: string): Observable<DataPackage> {
    const params = {
      divisionId: divisionId.toString(),
      fechaInicio: fechaInicio,
      fechaFin: fechaFin
    };
    return this.http.get<DataPackage>(`${this.apiUrl}/mapa`, { params });
  }

  /**
   * Genera el mapa de horarios para la semana actual
   */
  generarMapaHorarioSemanaActual(divisionId: number): Observable<DataPackage> {
    const fechaInicio = this.formatearFechaISO(this.getLunesDeLaSemana(new Date()));
    const fechaFin = this.formatearFechaISO(this.getViernesDeLaSemana(new Date()));
    return this.generarMapaHorarioSemanal(divisionId, fechaInicio, fechaFin);
  }

  /**
   * Obtiene el lunes de una fecha dada
   */
  getLunesDeLaSemana(fecha: Date): Date {
    const dayOfWeek = fecha.getDay();
    const monday = new Date(fecha);
    monday.setDate(fecha.getDate() - dayOfWeek + 1);
    return monday;
  }

  /**
   * Obtiene el viernes de una fecha dada
   */
  getViernesDeLaSemana(fecha: Date): Date {
    const monday = this.getLunesDeLaSemana(fecha);
    const friday = new Date(monday);
    friday.setDate(monday.getDate() + 4);
    return friday;
  }

  /**
   * Convierte una fecha a formato ISO string (YYYY-MM-DD)
   */
  formatearFechaISO(fecha: Date): string {
    return fecha.toISOString().split('T')[0];
  }
}
