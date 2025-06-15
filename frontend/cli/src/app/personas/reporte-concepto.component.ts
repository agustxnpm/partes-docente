import { Component, AfterViewInit, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ReporteConceptoDTO, PersonaConceptoDTO } from './reporte-concepto.dto';
import { ReporteConceptoService } from './reporte-concepto.service';
import { PaginationComponent } from '../pagination/pagination.component';
import { Chart, ChartConfiguration, ChartType, registerables } from 'chart.js';

@Component({
  selector: 'app-reporte-concepto',
  standalone: true,
  imports: [CommonModule, FormsModule, PaginationComponent],
  templateUrl: './reporte-concepto.component.html',
  styleUrls: ['./reporte-concepto.component.css']
})
export class ReporteConceptoComponent implements AfterViewInit {
  anioConsulta: number = new Date().getFullYear();
  anioMaximo: number = new Date().getFullYear() + 10;
  reporte: ReporteConceptoDTO | null = null;
  loading: boolean = false;
  mensaje: string = '';
  tipoMensaje: 'success' | 'error' | 'info' = 'info';
  
  // Estado para filtros y ordenamiento
  filtroPersona: string = '';
  filtroArticulo: string = '';
  ordenarPor: 'nombre' | 'apellido' | 'totalDias' | 'dni' = 'apellido';
  ordenDescendente: boolean = false;
  
  // Estado para vista expandida
  personasExpandidas: Set<number> = new Set();

  // Propiedades para paginación
  currentPage: number = 1;
  itemsPerPage: number = 5;
  totalPages: number = 0;

  // Referencias a los elementos canvas para gráficos
  @ViewChild('chartLicenciasPorArticulo', { static: false }) chartLicenciasPorArticulo!: ElementRef<HTMLCanvasElement>;
  @ViewChild('chartDiasPorPersona', { static: false }) chartDiasPorPersona!: ElementRef<HTMLCanvasElement>;

  // Instancias de los gráficos
  chartLicenciasInstance: Chart | null = null;
  chartDiasInstance: Chart | null = null;

  constructor(private reporteService: ReporteConceptoService) {
    // Registrar componentes de Chart.js
    Chart.register(...registerables);
  }

  ngAfterViewInit() {
    // Se ejecuta después de que la vista se inicializa
  }

  consultarReporte() {
    if (!this.anioConsulta || this.anioConsulta < 1900 || this.anioConsulta > this.anioMaximo) {
      this.mostrarMensaje('Por favor ingrese un año válido', 'error');
      return;
    }

    this.loading = true;
    this.mensaje = '';
    
    this.reporteService.generarReporteAnual(this.anioConsulta)
      .subscribe({
        next: (response) => {
          this.reporte = response.data as ReporteConceptoDTO;
          this.loading = false;
          this.mostrarMensaje(`Reporte generado exitosamente. ${this.reporte!.TotalPersonas} personas encontradas.`, 'success');
          this.crearGraficos(); // Crear gráficos al generar el reporte
        },
        error: (error) => {
          this.loading = false;
          this.mostrarMensaje('Error al generar el reporte: ' + error.message, 'error');
          console.error('Error:', error);
        }
      });
  }

  exportarCSV() {
    if (!this.reporte) {
      this.mostrarMensaje('Debe generar un reporte antes de exportar', 'error');
      return;
    }

    this.loading = true;
    this.reporteService.exportarReporteCSV(this.anioConsulta)
      .subscribe({
        next: (response) => {
          if (response.status === 200 && response.data) {
            this.reporteService.descargarArchivo(response.data as unknown as string, `reporte-concepto-${this.anioConsulta}.csv`, 'text/csv');
            this.mostrarMensaje('CSV descargado exitosamente', 'success');
          } else {
            this.mostrarMensaje('Error al generar el CSV', 'error');
          }
        },
        error: (error) => {
          console.error('Error al exportar CSV:', error);
          this.mostrarMensaje('Error al exportar CSV: ' + (error.message || 'Error desconocido'), 'error');
        },
        complete: () => {
          this.loading = false;
        }
      });
  }

  exportarPDF() {
    if (!this.reporte) {
      this.mostrarMensaje('Debe generar un reporte antes de exportar', 'error');
      return;
    }

    this.loading = true;
    this.reporteService.exportarReportePDF(this.anioConsulta)
      .subscribe({
        next: (response) => {
          if (response.status === 200 && response.data) {
            this.reporteService.descargarArchivo(response.data as unknown as string, `reporte-concepto-${this.anioConsulta}.pdf`, 'application/pdf');
            this.mostrarMensaje('PDF descargado exitosamente', 'success');
          } else {
            this.mostrarMensaje('Error al generar el PDF', 'error');
          }
        },
        error: (error) => {
          console.error('Error al exportar PDF:', error);
          this.mostrarMensaje('Error al exportar PDF: ' + (error.message || 'Error desconocido'), 'error');
        },
        complete: () => {
          this.loading = false;
        }
      });
  }

  limpiarConsulta() {
    this.reporte = null;
    this.mensaje = '';
    this.filtroPersona = '';
    this.filtroArticulo = '';
    this.personasExpandidas.clear();
    this.anioConsulta = new Date().getFullYear();
    this.currentPage = 1;
  }

  onPageChangeRequested(page: number): void {
    this.currentPage = page;
  }

  reiniciarPaginacion(): void {
    this.currentPage = 1;
  }

  togglePersonaExpandida(dni: number) {
    if (this.personasExpandidas.has(dni)) {
      this.personasExpandidas.delete(dni);
    } else {
      this.personasExpandidas.add(dni);
    }
  }

  isPersonaExpandida(dni: number): boolean {
    return this.personasExpandidas.has(dni);
  }

  // Métodos para gráficos
  crearGraficos(): void {
    if (!this.reporte) return;
    
    // Esperar un tick para asegurar que los elementos estén en el DOM
    setTimeout(() => {
      this.crearGraficoLicenciasPorArticulo();
      this.crearGraficoDiasPorPersona();
    }, 100);
  }

  crearGraficoLicenciasPorArticulo(): void {
    if (!this.chartLicenciasPorArticulo?.nativeElement || !this.reporte) return;

    // Destruir gráfico existente si existe
    if (this.chartLicenciasInstance) {
      this.chartLicenciasInstance.destroy();
    }

    // Obtener datos para el gráfico
    const datosGrafico = this.obtenerDatosLicenciasPorArticulo();

    const config: ChartConfiguration = {
      type: 'bar' as ChartType,
      data: {
        labels: datosGrafico.labels,
        datasets: [{
          label: 'Cantidad de Licencias',
          data: datosGrafico.data,
          backgroundColor: [
            'rgba(54, 162, 235, 0.8)',
            'rgba(255, 99, 132, 0.8)',
            'rgba(255, 206, 86, 0.8)',
            'rgba(75, 192, 192, 0.8)',
            'rgba(153, 102, 255, 0.8)',
            'rgba(255, 159, 64, 0.8)',
            'rgba(199, 199, 199, 0.8)'
          ],
          borderColor: [
            'rgba(54, 162, 235, 1)',
            'rgba(255, 99, 132, 1)',
            'rgba(255, 206, 86, 1)',
            'rgba(75, 192, 192, 1)',
            'rgba(153, 102, 255, 1)',
            'rgba(255, 159, 64, 1)',
            'rgba(199, 199, 199, 1)'
          ],
          borderWidth: 2
        }]
      },
      options: {
        responsive: true,
        plugins: {
          title: {
            display: true,
            text: 'Licencias por Artículo'
          },
          legend: {
            display: false
          }
        },
        scales: {
          y: {
            beginAtZero: true,
            ticks: {
              stepSize: 1
            }
          }
        }
      }
    };

    this.chartLicenciasInstance = new Chart(this.chartLicenciasPorArticulo.nativeElement, config);
  }

  crearGraficoDiasPorPersona(): void {
    if (!this.chartDiasPorPersona?.nativeElement || !this.reporte) return;

    // Destruir gráfico existente si existe
    if (this.chartDiasInstance) {
      this.chartDiasInstance.destroy();
    }

    // Obtener datos para el gráfico
    const datosGrafico = this.obtenerDatosDiasPorPersona();

    const config: ChartConfiguration = {
      type: 'pie' as ChartType,
      data: {
        labels: datosGrafico.labels,
        datasets: [{
          data: datosGrafico.data,
          backgroundColor: [
            'rgba(255, 99, 132, 0.8)',
            'rgba(54, 162, 235, 0.8)',
            'rgba(255, 206, 86, 0.8)',
            'rgba(75, 192, 192, 0.8)',
            'rgba(153, 102, 255, 0.8)',
            'rgba(255, 159, 64, 0.8)',
            'rgba(199, 199, 199, 0.8)',
            'rgba(83, 102, 255, 0.8)',
            'rgba(255, 159, 243, 0.8)',
            'rgba(54, 235, 162, 0.8)'
          ],
          borderColor: [
            'rgba(255, 99, 132, 1)',
            'rgba(54, 162, 235, 1)',
            'rgba(255, 206, 86, 1)',
            'rgba(75, 192, 192, 1)',
            'rgba(153, 102, 255, 1)',
            'rgba(255, 159, 64, 1)',
            'rgba(199, 199, 199, 1)',
            'rgba(83, 102, 255, 1)',
            'rgba(255, 159, 243, 1)',
            'rgba(54, 235, 162, 1)'
          ],
          borderWidth: 2
        }]
      },
      options: {
        responsive: true,
        plugins: {
          title: {
            display: true,
            text: 'Distribución de Días de Licencia por Persona'
          },
          legend: {
            position: 'bottom'
          }
        }
      }
    };

    this.chartDiasInstance = new Chart(this.chartDiasPorPersona.nativeElement, config);
  }

  obtenerDatosLicenciasPorArticulo(): { labels: string[], data: number[] } {
    if (!this.reporte) return { labels: [], data: [] };

    const conteoArticulos: { [key: string]: number } = {};

    this.reporte.Personas.forEach(persona => {
      persona.Licencias.forEach(licencia => {
        const articulo = `Art. ${licencia.ArticuloLicencia}`;
        conteoArticulos[articulo] = (conteoArticulos[articulo] || 0) + 1;
      });
    });

    // Ordenar por cantidad descendente
    const articulosOrdenados = Object.entries(conteoArticulos)
      .sort(([,a], [,b]) => b - a);

    return {
      labels: articulosOrdenados.map(([articulo]) => articulo),
      data: articulosOrdenados.map(([, cantidad]) => cantidad)
    };
  }

  obtenerDatosDiasPorPersona(): { labels: string[], data: number[] } {
    if (!this.reporte) return { labels: [], data: [] };

    // Obtener personas con días de licencia > 0 y ordenar por días descendente
    const personasConDias = this.reporte.Personas
      .filter(persona => persona.TotalDiasLicencia > 0)
      .sort((a, b) => b.TotalDiasLicencia - a.TotalDiasLicencia)
      .slice(0, 10); // Top 10

    return {
      labels: personasConDias.map(persona => `${persona.Apellido}, ${persona.Nombre}`),
      data: personasConDias.map(persona => persona.TotalDiasLicencia)
    };
  }

  limpiarGraficos(): void {
    // Limpiar gráficos si es necesario
    if (this.chartLicenciasInstance) {
      this.chartLicenciasInstance.destroy();
      this.chartLicenciasInstance = null;
    }
    if (this.chartDiasInstance) {
      this.chartDiasInstance.destroy();
      this.chartDiasInstance = null;
    }
  }

  get personasFiltradas(): PersonaConceptoDTO[] {
    if (!this.reporte) return [];

    let personas = [...this.reporte.Personas];

    // Filtrar por nombre/apellido
    if (this.filtroPersona) {
      const termino = this.filtroPersona.toLowerCase();
      personas = personas.filter(p => 
        p.Nombre.toLowerCase().includes(termino) || 
        p.Apellido.toLowerCase().includes(termino) ||
        p.DNI.toString().includes(termino)
      );
    }

    // Filtrar por artículo
    if (this.filtroArticulo) {
      personas = personas.filter(p => 
        p.Licencias.some(l => 
          l.ArticuloLicencia.toLowerCase().includes(this.filtroArticulo.toLowerCase())
        )
      );
    }

    // Ordenar
    personas.sort((a, b) => {
      let valorA: any, valorB: any;
      
      switch (this.ordenarPor) {
        case 'nombre':
          valorA = a.Nombre;
          valorB = b.Nombre;
          break;
        case 'apellido':
          valorA = a.Apellido;
          valorB = b.Apellido;
          break;
        case 'totalDias':
          valorA = a.TotalDiasLicencia;
          valorB = b.TotalDiasLicencia;
          break;
        case 'dni':
          valorA = a.DNI;
          valorB = b.DNI;
          break;
        default:
          return 0;
      }

      if (typeof valorA === 'string') {
        valorA = valorA.toLowerCase();
        valorB = valorB.toLowerCase();
      }

      let resultado = valorA < valorB ? -1 : valorA > valorB ? 1 : 0;
      return this.ordenDescendente ? -resultado : resultado;
    });

    // Calcular paginación
    this.totalPages = Math.ceil(personas.length / this.itemsPerPage);
    
    // Ajustar página currentPage si es necesario
    if (this.currentPage > this.totalPages && this.totalPages > 0) {
      this.currentPage = this.totalPages;
    }
    
    // Devolver solo los elementos de la página actual
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    return personas.slice(startIndex, endIndex);
  }

  get todasLasPersonasFiltradas(): PersonaConceptoDTO[] {
    if (!this.reporte) return [];

    let personas = [...this.reporte.Personas];

    // Filtrar por nombre/apellido
    if (this.filtroPersona) {
      const termino = this.filtroPersona.toLowerCase();
      personas = personas.filter(p => 
        p.Nombre.toLowerCase().includes(termino) || 
        p.Apellido.toLowerCase().includes(termino) ||
        p.DNI.toString().includes(termino)
      );
    }

    // Filtrar por artículo
    if (this.filtroArticulo) {
      personas = personas.filter(p => 
        p.Licencias.some(l => 
          l.ArticuloLicencia.toLowerCase().includes(this.filtroArticulo.toLowerCase())
        )
      );
    }

    return personas;
  }

  formatearFecha(fecha: string): string {
    if (!fecha) return '';
    
    // Convertir de YYYY-MM-DD a DD/MM/YYYY
    const partes = fecha.split('-');
    if (partes.length === 3) {
      return `${partes[2]}/${partes[1]}/${partes[0]}`;
    }
    
    return fecha; // Devolver original si no se puede convertir
  }

  obtenerEstadisticas() {
    if (!this.reporte) return null;

    const personasFiltradas = this.todasLasPersonasFiltradas;
    const articulosCount: { [key: string]: number } = {};
    
    personasFiltradas.forEach(persona => {
      persona.Licencias.forEach(licencia => {
        articulosCount[licencia.ArticuloLicencia] = (articulosCount[licencia.ArticuloLicencia] || 0) + 1;
      });
    });

    return {
      totalPersonasMostradas: personasFiltradas.length,
      totalLicenciasMostradas: personasFiltradas.reduce((sum, p) => sum + p.Licencias.length, 0),
      totalDiasMostrados: personasFiltradas.reduce((sum, p) => sum + p.TotalDiasLicencia, 0),
      articulosMasUsados: Object.entries(articulosCount)
        .sort(([,a], [,b]) => b - a)
        .slice(0, 3)
    };
  }

  private mostrarMensaje(mensaje: string, tipo: 'success' | 'error' | 'info') {
    this.mensaje = mensaje;
    this.tipoMensaje = tipo;
    setTimeout(() => {
      this.mensaje = '';
    }, 5000);
  }
}
