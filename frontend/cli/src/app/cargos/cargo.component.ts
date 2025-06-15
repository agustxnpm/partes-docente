import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";
import { RouterModule } from "@angular/router";
import { FormsModule } from "@angular/forms";
import { PaginationComponent } from "../pagination/pagination.component";
import { ResultsPage } from "../results-page";
import { CargoService } from "./cargo.service";
import { ModalService } from "../modal/modal.service";
import { Cargo } from "./cargo";
import { TipoDesignacion } from "./tipoDesignacion";
import { TipoDesignacionFormatPipe } from '../shared/pipes/tipo-designacion-format.pipe';


@Component({
  selector: "app-cargo",
  imports: [CommonModule, RouterModule, FormsModule, PaginationComponent, TipoDesignacionFormatPipe],
  templateUrl: "./cargo.component.html",
  styleUrls: ["../global-styles/table-styles.css", "./cargo.component.css"],
})
export class CargoComponent {
  resultsPage: ResultsPage = <ResultsPage>{};
  currentPage: number = 1;
  mensaje: string = "";
  
  // Datos para enfoque híbrido
  allCargos: Cargo[] = [];
  filteredCargos: Cargo[] = [];
  loading: boolean = false;
  
  // Control del modo híbrido
  usandoFiltros: boolean = false;
  
  // Filtros
  filtroTexto: string = '';
  filtroTipoDesignacion: string = '';
  filtroVigencia: string = '';
  
  // Ordenamiento
  ordenActual: string = '';
  direccionOrden: 'asc' | 'desc' = 'asc';
  
  // Paginación local
  itemsPorPagina: number = 7;
  
  // Enum para templates
  TipoDesignacion = TipoDesignacion;

  constructor(
    private cargoService: CargoService,
    private modalService: ModalService
  ) {}

  ngOnInit(): void {
    this.cargarCargosPaginados();
  }

  // Cargar cargos con paginación de servidor (orden natural: más recientes primero)
  cargarCargosPaginados(): void {
    this.loading = true;
    this.cargoService.byPage(this.currentPage, this.itemsPorPagina).subscribe({
      next: (response) => {
        this.resultsPage = <ResultsPage>response.data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar cargos:', error);
        this.loading = false;
      }
    });
  }

  // Cargar todos los cargos para filtrado local
  cargarTodosLosCargos(): void {
    this.loading = true;
    this.cargoService.findAll().subscribe({
      next: (response) => {
        this.allCargos = Array.isArray(response.data) ? response.data : [];
        this.aplicarFiltrosYOrden();
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar todos los cargos:', error);
        this.loading = false;
      }
    });
  }

  getCargos(): void {
    this.cargoService.findAll().subscribe((response) => {
      this.allCargos = Array.isArray(response.data) ? response.data : [];
      this.aplicarFiltrosYOrden();
    });
  }

  // Detectar si hay filtros aplicados
  get hayFiltrosAplicados(): boolean {
    return this.filtroTexto.trim() !== '' || 
           this.filtroTipoDesignacion !== '' || 
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
      this.cargarTodosLosCargos();
    } else if (!hayFiltros && this.usandoFiltros) {
      // Volver a modo paginado: cargar primera página
      this.usandoFiltros = false;
      this.currentPage = 1;
      this.cargarCargosPaginados();
    } else if (hayFiltros && this.usandoFiltros) {
      // Ya estamos en modo filtros: solo aplicar filtros
      this.aplicarFiltrosYOrden();
    }
  }

  aplicarFiltrosYOrden(): void {
    let cargosFiltrados = [...this.allCargos];

    // Aplicar filtro por texto (nombre)
    if (this.filtroTexto) {
      cargosFiltrados = cargosFiltrados.filter(cargo =>
        cargo.nombre.toLowerCase().includes(this.filtroTexto.toLowerCase())
      );
    }

    // Aplicar filtro por tipo de designación
    if (this.filtroTipoDesignacion) {
      cargosFiltrados = cargosFiltrados.filter(cargo =>
        cargo.tipoDesignacion === this.filtroTipoDesignacion
      );
    }

    // Aplicar filtro por vigencia
    if (this.filtroVigencia) {
      const ahora = new Date();
      cargosFiltrados = cargosFiltrados.filter(cargo => {
        if (this.filtroVigencia === 'vigente') {
          return !cargo.fechaFin || new Date(cargo.fechaFin) > ahora;
        } else if (this.filtroVigencia === 'vencido') {
          return cargo.fechaFin && new Date(cargo.fechaFin) <= ahora;
        }
        return true;
      });
    }

    // Aplicar ordenamiento
    if (this.ordenActual) {
      cargosFiltrados.sort((a, b) => {
        let valorA: any, valorB: any;

        switch (this.ordenActual) {
          case 'nombre':
            valorA = a.nombre?.toLowerCase() || '';
            valorB = b.nombre?.toLowerCase() || '';
            break;
          case 'cargaHoraria':
            valorA = a.cargaHoraria || 0;
            valorB = b.cargaHoraria || 0;
            break;
          case 'fechaInicio':
            valorA = new Date(a.fechaInicio);
            valorB = new Date(b.fechaInicio);
            break;
          case 'fechaFin':
            valorA = a.fechaFin ? new Date(a.fechaFin) : new Date('9999-12-31');
            valorB = b.fechaFin ? new Date(b.fechaFin) : new Date('9999-12-31');
            break;
          case 'tipoDesignacion':
            valorA = a.tipoDesignacion || '';
            valorB = b.tipoDesignacion || '';
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
      cargosFiltrados.sort((a, b) => {
        return this.direccionOrden === 'desc' ? a.id - b.id : b.id - a.id;
      });
    }

    this.filteredCargos = cargosFiltrados;
    this.actualizarPaginacion();
  }

  actualizarPaginacion(): void {
    const totalElementos = this.filteredCargos.length;
    const totalPaginas = Math.ceil(totalElementos / this.itemsPorPagina);
    
    // Asegurar que la página actual sea válida
    if (this.currentPage > totalPaginas && totalPaginas > 0) {
      this.currentPage = totalPaginas;
    } else if (this.currentPage < 1) {
      this.currentPage = 1;
    }

    const inicio = (this.currentPage - 1) * this.itemsPorPagina;
    const fin = inicio + this.itemsPorPagina;
    const contenidoPagina = this.filteredCargos.slice(inicio, fin);

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

  toggleOrdenDireccion(): void {
    this.direccionOrden = this.direccionOrden === 'asc' ? 'desc' : 'asc';
    this.onFiltroChange();
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

  limpiarFiltros(): void {
    this.filtroTexto = '';
    this.filtroTipoDesignacion = '';
    this.filtroVigencia = '';
    this.ordenActual = '';
    this.direccionOrden = 'asc';
    this.currentPage = 1;
    this.onFiltroChange();
  }

  eliminarCargo(cargo: Cargo): void {
    this.modalService
      .confirm(
        "Eliminar Cargo",
        `¿Está seguro que desea eliminar el cargo ${cargo.nombre}?`,
        "Esta acción no se puede deshacer"
      )
      .then(
        () => {
          this.cargoService.delete(cargo).subscribe({
            next: (response) => {
              this.mensaje = response.message;
              if (response.status === 200) {
                this.modalService.alert("Éxito", this.mensaje);
                // Recargar datos después de eliminar
                if (this.usandoFiltros) {
                  this.cargarTodosLosCargos();
                } else {
                  this.cargarCargosPaginados();
                }
              } else {
                this.modalService.alert("Error", this.mensaje);
              }
            },
            error: (err) => {
              console.error("Error al eliminar el cargo:", err);
              this.mensaje = "Error al eliminar el cargo.";
              this.modalService.alert("Error", this.mensaje);
            },
          });
        },
        () => {
          console.log("Operación cancelada");
        }
      );
  }

  onPageChangeRequested(page: number): void {
    this.currentPage = page;
    if (this.usandoFiltros) {
      // En modo filtros: solo actualizar paginación local
      this.actualizarPaginacion();
    } else {
      // En modo servidor: cargar nueva página
      this.cargarCargosPaginados();
    }
  }
}
