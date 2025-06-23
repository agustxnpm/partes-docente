import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { HorarioService } from './horario.service';
import { MapaHorarioSemanal, HorarioMapa } from './mapa-horario';
import { Division } from '../divisiones/division';
import { Turno } from '../divisiones/turno';
import { ModalService } from '../modal/modal.service';

@Component({
  selector: 'app-mapa-horario',
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './mapa-horario.component.html',
  styleUrls: ['./mapa-horario.component.css', '../global-styles/table-styles.css']
})
export class MapaHorarioComponent implements OnInit {
  
  // Enum para usar en el template
  readonly Turno = Turno;
  
  // Datos del mapa
  mapaHorario: MapaHorarioSemanal | null = null;
  divisiones: Division[] = [];
  loading: boolean = false;
  
  // Filtros
  divisionSeleccionada: Division | null = null;
  fechaSeleccionada: Date = new Date();
  
  // Propiedades para typeahead de divisiones
  showDivisionDropdown = false;
  divisionesFiltradas: Division[] = [];
  divisionInputValue: string = '';
  
  // Para navegación semanal
  fechaInicio: Date = new Date();
  fechaFin: Date = new Date();
  
  // Para el grid
  dias: string[] = ['LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES'];
  horas: number[] = [1, 2, 3, 4, 5, 6, 7, 8];
  
  constructor(
    private horarioService: HorarioService,
    private modalService: ModalService
  ) {}

  ngOnInit(): void {
    this.cargarDivisiones();
    this.inicializarFechas();
  }
  
  inicializarFechas(): void {
    const hoy = new Date();
    this.fechaSeleccionada = hoy;
    this.fechaInicio = this.horarioService.getLunesDeLaSemana(hoy);
    this.fechaFin = this.horarioService.getViernesDeLaSemana(hoy);
  }

  cargarDivisiones(): void {
    this.loading = true;
    this.horarioService.obtenerDivisionesDisponibles().subscribe({
      next: (response) => {
        this.divisiones = response.data as Division[];
        this.divisionesFiltradas = [...this.divisiones]; // inicializar filtradas
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar divisiones:', error);
        this.modalService.alert('Error', 'No se pudieron cargar las divisiones.');
        this.loading = false;
      }
    });
  }

  onDivisionChange(): void {
    if (this.divisionSeleccionada) {
      this.generarMapaHorario();
    } else {
      this.mapaHorario = null;
    }
  }

  generarMapaHorario(): void {
    if (!this.divisionSeleccionada) {
      this.modalService.alert('Atención', 'Debe seleccionar una división.');
      return;
    }

    this.loading = true;
    const fechaInicioStr = this.horarioService.formatearFechaISO(this.fechaInicio);
    const fechaFinStr = this.horarioService.formatearFechaISO(this.fechaFin);

    this.horarioService.generarMapaHorarioSemanal(
      this.divisionSeleccionada.id, 
      fechaInicioStr, 
      fechaFinStr
    ).subscribe({
      next: (response) => {
        this.mapaHorario = response.data as MapaHorarioSemanal;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al generar mapa de horarios:', error);
        this.modalService.alert('Error', 'No se pudo generar el mapa de horarios.');
        this.loading = false;
      }
    });
  }

  // Navegación semanal
  semanaAnterior(): void {
    this.fechaInicio.setDate(this.fechaInicio.getDate() - 7);
    this.fechaFin.setDate(this.fechaFin.getDate() - 7);
    this.fechaSeleccionada.setDate(this.fechaSeleccionada.getDate() - 7);
    
    if (this.divisionSeleccionada) {
      this.generarMapaHorario();
    }
  }

  semanaSiguiente(): void {
    this.fechaInicio.setDate(this.fechaInicio.getDate() + 7);
    this.fechaFin.setDate(this.fechaFin.getDate() + 7);
    this.fechaSeleccionada.setDate(this.fechaSeleccionada.getDate() + 7);
    
    if (this.divisionSeleccionada) {
      this.generarMapaHorario();
    }
  }

  irASemanaActual(): void {
    this.inicializarFechas();
    if (this.divisionSeleccionada) {
      this.generarMapaHorario();
    }
  }

  // Obtener horario específico para una celda
  getHorarioParaCelda(dia: string, hora: number): HorarioMapa | null {
    if (!this.mapaHorario) return null;
    
    return this.mapaHorario.horarios.find(h => h.dia === dia && h.hora === hora) || null;
  }

  // Métodos para el estilo de las celdas
  getCellClass(horario: HorarioMapa | null): string {
    if (!horario) return 'hora-vacia';
    
    if (horario.horaLibre) {
      // Distinguir entre diferentes tipos de "hora libre"
      if (horario.docente === 'Sin docente asignado') return 'hora-sin-docente';
      if (horario.docente === 'Hora libre (docente con licencia)') return 'hora-licencia';
      return 'hora-libre';
    }
    
    if (horario.esSuplencia) return 'hora-suplencia';
    return 'hora-ocupada';
  }

  getCellContent(horario: HorarioMapa | null): string {
    if (!horario) return '';
    
    // Si es hora libre, mostrar el espacio curricular si existe, sino vacío para horas sin asignar
    if (horario.horaLibre) {
      if (horario.espacioCurricular && horario.espacioCurricular !== 'Hora libre') {
        return horario.espacioCurricular;
      }
      return ''; // Horas sin asignar quedan vacías
    }
    
    return horario.espacioCurricular;
  }

  getDocenteContent(horario: HorarioMapa | null): string {
    if (!horario) return '';
    
    // Mostrar información del docente siempre que exista
    if (horario.docente && horario.docente.trim() !== '') {
      // Si es suplencia, agregar indicador visual
      if (horario.esSuplencia) {
        return `${horario.docente} (Suplente)`;
      }
      return horario.docente;
    }
    
    return '';
  }

  // Formateo de fechas para mostrar
  formatearFecha(fecha: Date): string {
    return fecha.toLocaleDateString('es-AR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric'
    });
  }

  formatearFechaCompleta(fecha: Date): string {
    return fecha.toLocaleDateString('es-AR', {
      weekday: 'long',
      day: 'numeric',
      month: 'long',
      year: 'numeric'
    });
  }

  // Métodos para el typeahead de divisiones
  onDivisionFocus(): void {
    this.showDivisionDropdown = true;
    this.divisionesFiltradas = [...this.divisiones];
  }

  onDivisionBlur(): void {
    // Delay para permitir click en opciones
    setTimeout(() => {
      this.showDivisionDropdown = false;
    }, 150);
  }

  onDivisionInput(event: any): void {
    const term = event.target.value.toLowerCase();
    this.divisionInputValue = event.target.value;
    
    // Si se borra el contenido, limpiar la selección
    if (term.length === 0) {
      this.divisionSeleccionada = null;
      this.divisionesFiltradas = [...this.divisiones];
      this.mapaHorario = null; // Limpiar el mapa cuando no hay división seleccionada
    } else {
      this.divisionesFiltradas = this.divisiones.filter(
        (division) =>
          division.anio?.toString().includes(term) ||
          division.numDivision?.toString().includes(term) ||
          division.turno?.toLowerCase().includes(term) ||
          division.orientacion?.toLowerCase().includes(term) ||
          `${division.anio}°${division.numDivision}°`.includes(term) ||
          `${division.anio} ${division.numDivision}`.includes(term)
      );
    }
    this.showDivisionDropdown = true;
  }

  selectDivision(division: Division): void {
    this.divisionSeleccionada = division;
    this.divisionInputValue = this.formatterDivision(division);
    this.showDivisionDropdown = false;
    this.onDivisionChange(); // Mantener la funcionalidad existente
  }

  formatterDivision = (division: Division) => {
    if (!division) return "";
    let texto = `${division.anio}° ${division.numDivision}° - ${division.turno}`;
    if (division.orientacion) {
      texto += ` (${division.orientacion})`;
    }
    return texto;
  };
}
