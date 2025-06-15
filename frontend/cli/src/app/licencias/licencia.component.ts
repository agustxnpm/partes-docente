import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";
import { RouterModule } from "@angular/router";
import { FormsModule } from "@angular/forms";
import { PaginationComponent } from "../pagination/pagination.component";
import { ResultsPage } from "../results-page";
import { LicenciaService } from "./licencia.service";
import { ModalService } from "../modal/modal.service";
import { LogLicencia } from "./LogLicencia";
import { Licencia } from "./licencia";
import { ArticuloLicencia } from "../articulo-licencia/articulo-licencia";
import { ArticuloLicenciaService } from "../articulo-licencia/articulo-licencia.service";

@Component({
  selector: "app-licencia",
  imports: [RouterModule, CommonModule, FormsModule, PaginationComponent],
  templateUrl: "./licencia.component.html",
  styleUrls: ["../global-styles/table-styles.css", "./licencia.component.css"],
})
export class LicenciaComponent {
  resultsPage: ResultsPage = <ResultsPage>{};
  currentPage: number = 1;
  mensaje: string = "";

  // Filtros
  filtroTexto: string = '';
  filtroEstado: string = '';
  filtroArticulo: string = '';
  filtroVigencia: string = '';
  
  // Ordenamiento
  ordenActual: string = '';
  direccionOrden: 'asc' | 'desc' = 'asc';
  
  // Datos para enfoque híbrido
  todasLasLicencias: Licencia[] = [];
  licenciasFiltradas: Licencia[] = [];
  loading: boolean = false;
  
  // Control del modo híbrido
  usandoFiltros: boolean = false;
  itemsPorPagina: number = 7;

  // Artículos disponibles para filtros
  articulosDisponibles: ArticuloLicencia[] = [];

  // Para el modal de logs
  showLogsModal: boolean = false;
  logsLicencia: LogLicencia[] = [];
  licenciaSeleccionada: Licencia | null = null;

  constructor(
    private licenciaService: LicenciaService,
    private modalService: ModalService,
    private articuloLicenciaService: ArticuloLicenciaService
  ) {}

  ngOnInit(): void {
    this.cargarLicenciasPaginadas();
    this.cargarArticulos();
  }

  // Cargar licencias con paginación de servidor (orden natural: más recientes primero)
  cargarLicenciasPaginadas(): void {
    this.loading = true;
    this.licenciaService.byPage(this.currentPage, this.itemsPorPagina).subscribe({
      next: (response) => {
        this.resultsPage = <ResultsPage>response.data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar licencias:', error);
        this.loading = false;
      }
    });
  }

  // Cargar todas las licencias para filtrado local
  cargarTodasLasLicencias(): void {
    this.loading = true;
    this.licenciaService.findAll().subscribe({
      next: (response) => {
        this.todasLasLicencias = Array.isArray(response.data) ? response.data : [];
        this.aplicarFiltrosYOrden();
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar todas las licencias:', error);
        this.loading = false;
      }
    });
  }

  // Cargar artículos disponibles para el filtro
  cargarArticulos(): void {
    this.articuloLicenciaService.findAll().subscribe({
      next: (response) => {
        this.articulosDisponibles = Array.isArray(response.data) ? response.data : [];
      },
      error: (error) => {
        console.error('Error al cargar artículos:', error);
      }
    });
  }

  // Obtener descripción del artículo por ID
  getArticuloDescripcion(articuloId: string): string {
    if (!articuloId) return '';
    const articulo = this.articulosDisponibles.find(a => a.id.toString() === articuloId);
    return articulo ? `Art. ${articulo.articulo}` : `ID: ${articuloId}`;
  }

  // Detectar si hay filtros aplicados
  get hayFiltrosAplicados(): boolean {
    return this.filtroTexto.trim() !== '' || 
           this.filtroEstado !== '' || 
           this.filtroArticulo !== '' ||
           this.filtroVigencia !== '' ||
           this.ordenActual !== '' ||
           this.direccionOrden === 'desc'; // Incluir cuando se cambia la dirección por defecto
  }

  // Manejar cambios en filtros
  onFiltroChange(): void {
    const hayFiltros = this.hayFiltrosAplicados;

    if (hayFiltros && !this.usandoFiltros) {
      // Cambiar a modo filtros: cargar todos los datos
      this.usandoFiltros = true;
      this.cargarTodasLasLicencias();
    } else if (!hayFiltros && this.usandoFiltros) {
      // Volver a modo paginado: cargar primera página
      this.usandoFiltros = false;
      this.currentPage = 1;
      this.cargarLicenciasPaginadas();
    } else if (hayFiltros && this.usandoFiltros) {
      // Ya estamos en modo filtros: solo aplicar filtros
      this.aplicarFiltrosYOrden();
    }
  }

  aplicarFiltrosYOrden(): void {
    let licenciasFiltradas = [...this.todasLasLicencias];

    // Aplicar filtro por texto (persona o artículo)
    if (this.filtroTexto) {
      const termino = this.filtroTexto.toLowerCase();
      licenciasFiltradas = licenciasFiltradas.filter(licencia =>
        licencia.persona.nombre.toLowerCase().includes(termino) ||
        licencia.persona.apellido.toLowerCase().includes(termino) ||
        (licencia.articuloLicencia?.articulo?.toString() || '').includes(termino) ||
        (licencia.articuloLicencia?.descripcion || '').toLowerCase().includes(termino)
      );
    }

    // Aplicar filtro por estado
    if (this.filtroEstado) {
      licenciasFiltradas = licenciasFiltradas.filter(licencia => 
        licencia.estado === this.filtroEstado
      );
    }

    // Aplicar filtro por artículo
    if (this.filtroArticulo) {
      const articuloId = parseInt(this.filtroArticulo);
      licenciasFiltradas = licenciasFiltradas.filter(licencia => 
        licencia.articuloLicencia?.id === articuloId
      );
    }

    // Aplicar filtro por vigencia
    if (this.filtroVigencia) {
      const ahora = new Date();
      licenciasFiltradas = licenciasFiltradas.filter(licencia => {
        if (this.filtroVigencia === 'vigente') {
          return new Date(licencia.pedidoHasta) >= ahora;
        } else if (this.filtroVigencia === 'vencido') {
          return new Date(licencia.pedidoHasta) < ahora;
        }
        return true;
      });
    }

    // Aplicar ordenamiento
    if (this.ordenActual) {
      licenciasFiltradas.sort((a, b) => {
        let valorA: any, valorB: any;

        switch (this.ordenActual) {
          case 'persona':
            valorA = `${a.persona.apellido} ${a.persona.nombre}`.toLowerCase();
            valorB = `${b.persona.apellido} ${b.persona.nombre}`.toLowerCase();
            break;
          case 'articulo':
            valorA = a.articuloLicencia?.articulo || 0;
            valorB = b.articuloLicencia?.articulo || 0;
            break;
          case 'fechaInicio':
            valorA = new Date(a.pedidoDesde);
            valorB = new Date(b.pedidoDesde);
            break;
          case 'fechaFin':
            valorA = new Date(a.pedidoHasta);
            valorB = new Date(b.pedidoHasta);
            break;
          case 'estado':
            valorA = a.estado.toLowerCase();
            valorB = b.estado.toLowerCase();
            break;
          default:
            return 0;
        }

        let resultado = 0;
        if (valorA < valorB) resultado = -1;
        if (valorA > valorB) resultado = 1;

        return this.direccionOrden === 'desc' ? -resultado : resultado;
      });
    } else {
      // Ordenamiento por defecto: por ID (más recientes primero o último según dirección)
      licenciasFiltradas.sort((a, b) => {
        return this.direccionOrden === 'desc' ? a.id - b.id : b.id - a.id;
      });
    }

    this.licenciasFiltradas = licenciasFiltradas;
    this.actualizarPaginacion();
  }

  actualizarPaginacion(): void {
    const totalElementos = this.licenciasFiltradas.length;
    const totalPaginas = Math.ceil(totalElementos / this.itemsPorPagina);
    
    // Asegurar que la página actual sea válida
    if (this.currentPage > totalPaginas && totalPaginas > 0) {
      this.currentPage = totalPaginas;
    } else if (this.currentPage < 1) {
      this.currentPage = 1;
    }

    const inicio = (this.currentPage - 1) * this.itemsPorPagina;
    const fin = inicio + this.itemsPorPagina;
    const contenidoPagina = this.licenciasFiltradas.slice(inicio, fin);

    this.resultsPage = {
      content: contenidoPagina,
      totalElements: totalElementos,
      totalPages: totalPaginas,
      size: this.itemsPorPagina,
      number: this.currentPage - 1,
      numberOfElements: contenidoPagina.length,
      first: this.currentPage === 1,
      last: this.currentPage === totalPaginas || totalPaginas === 0
    };
  }

  ordenarPor(campo: string): void {
    if (campo) {
      if (this.ordenActual === campo) {
        this.direccionOrden = this.direccionOrden === 'asc' ? 'desc' : 'asc';
      } else {
        this.ordenActual = campo;
        this.direccionOrden = 'asc';
      }
      this.onFiltroChange();
    }
  }

  toggleOrdenDireccion(): void {
    this.direccionOrden = this.direccionOrden === 'asc' ? 'desc' : 'asc';
    this.onFiltroChange();
  }

  limpiarFiltros(): void {
    this.filtroTexto = '';
    this.filtroEstado = '';
    this.filtroArticulo = '';
    this.filtroVigencia = '';
    this.ordenActual = '';
    this.direccionOrden = 'asc';
    this.currentPage = 1;
    this.onFiltroChange();
  }

  // Mantener método original para compatibilidad si es necesario
  getLicencias(): void {
    this.licenciaService.byPage(this.currentPage, this.itemsPorPagina).subscribe((response) => {
      this.resultsPage = <ResultsPage>response.data;
    });
  }

  onPageChangeRequested(page: number): void {
    this.currentPage = page;
    if (this.usandoFiltros) {
      // En modo filtros: solo actualizar paginación local
      this.actualizarPaginacion();
    } else {
      // En modo servidor: cargar nueva página
      this.cargarLicenciasPaginadas();
    }
  }

  verLogs(licencia: Licencia): void {
    this.licenciaSeleccionada = licencia;
    this.licenciaService.getLogsByLicenciaId(licencia.id).subscribe({
      next: (response) => {
        this.logsLicencia = response.data as LogLicencia[];
        this.showLogsModal = true;
      },
      error: (err) => {
        console.error("Error al cargar logs:", err);
        this.logsLicencia = [];
        this.showLogsModal = true;
      },
    });
  }

  cerrarModalLogs(): void {
    this.showLogsModal = false;
    this.logsLicencia = [];
    this.licenciaSeleccionada = null;
  }
}
