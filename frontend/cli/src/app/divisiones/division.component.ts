import { Component } from "@angular/core";
import { ResultsPage } from "../results-page";
import { DivisionService } from "./division.service";
import { PaginationComponent } from "../pagination/pagination.component";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { FormsModule } from "@angular/forms";
import { ModalService } from "../modal/modal.service";
import { Division } from "./division";
import { Turno } from "./turno";

@Component({
  selector: "app-division",
  imports: [RouterModule, CommonModule, PaginationComponent, FormsModule],
  templateUrl: "./division.component.html",
  styleUrls: ["../global-styles/table-styles.css", "./division.component.css"],
})
export class DivisionComponent {
  resultsPage: ResultsPage = <ResultsPage>{};
  currentPage: number = 1;
  mensaje: string = "";

  // Propiedades para filtros y ordenamiento
  filtroTexto: string = '';
  filtroTurno: string = '';
  filtroAnio: string = '';
  ordenActual: string = '';
  direccionOrden: 'asc' | 'desc' = 'asc';

  // Datos para enfoque híbrido
  todasLasDivisiones: Division[] = [];
  divisionesFiltradas: Division[] = [];
  loading: boolean = false;
  
  // Control del modo híbrido
  usandoFiltros: boolean = false;
  itemsPorPagina: number = 7;

  // Enum para template
  Turno = Turno;

  constructor(
    private divisionService: DivisionService,
    private modalService: ModalService
  ) {}

  ngOnInit(): void {
    this.cargarDivisionesPaginadas();
  }

  // Cargar divisiones con paginación de servidor (orden natural: más recientes primero)
  cargarDivisionesPaginadas(): void {
    this.loading = true;
    this.divisionService.byPage(this.currentPage, this.itemsPorPagina).subscribe({
      next: (response) => {
        this.resultsPage = <ResultsPage>response.data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar divisiones:', error);
        this.loading = false;
      }
    });
  }

  cargarTodasLasDivisiones(): void {
    this.loading = true;
    this.divisionService.findAll().subscribe({
      next: (response) => {
        this.todasLasDivisiones = (response.data as Division[]) || [];
        this.aplicarFiltrosYOrdenamiento();
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar divisiones:', error);
        this.mensaje = 'Error al cargar las divisiones';
        this.loading = false;
      }
    });
  }

  // Detectar si hay filtros aplicados
  get hayFiltrosAplicados(): boolean {
    return this.filtroTexto.trim() !== '' || 
           this.filtroTurno !== '' || 
           this.filtroAnio !== '' ||
           this.ordenActual !== '' ||
           this.direccionOrden === 'desc'; // Incluir cuando se cambia la dirección por defecto
  }

  // Manejar cambios en filtros
  onFiltroChange(): void {
    const hayFiltros = this.hayFiltrosAplicados;

    if (hayFiltros && !this.usandoFiltros) {
      // Cambiar a modo filtros: cargar todos los datos
      this.usandoFiltros = true;
      this.cargarTodasLasDivisiones();
    } else if (!hayFiltros && this.usandoFiltros) {
      // Volver a modo paginado: cargar primera página
      this.usandoFiltros = false;
      this.currentPage = 1;
      this.cargarDivisionesPaginadas();
    } else if (hayFiltros && this.usandoFiltros) {
      // Ya estamos en modo filtros: solo aplicar filtros
      this.aplicarFiltrosYOrdenamiento();
    }
  }

  aplicarFiltrosYOrdenamiento(): void {
    let divisiones = [...this.todasLasDivisiones];

    // Aplicar filtro de texto (orientación)
    if (this.filtroTexto) {
      const termino = this.filtroTexto.toLowerCase();
      divisiones = divisiones.filter(d => 
        d.orientacion.toLowerCase().includes(termino)
      );
    }

    // Aplicar filtro de turno
    if (this.filtroTurno) {
      divisiones = divisiones.filter(d => d.turno === this.filtroTurno);
    }

    // Aplicar filtro de año
    if (this.filtroAnio) {
      const anio = parseInt(this.filtroAnio);
      divisiones = divisiones.filter(d => d.anio === anio);
    }

    // Aplicar ordenamiento
    if (this.ordenActual) {
      divisiones.sort((a, b) => {
        let valorA: any, valorB: any;
        
        switch (this.ordenActual) {
          case 'anio':
            valorA = a.anio || 0;
            valorB = b.anio || 0;
            break;
          case 'numDivision':
            valorA = a.numDivision || 0;
            valorB = b.numDivision || 0;
            break;
          case 'orientacion':
            valorA = a.orientacion;
            valorB = b.orientacion;
            break;
          case 'turno':
            valorA = a.turno || '';
            valorB = b.turno || '';
            break;
          default:
            return 0;
        }

        if (typeof valorA === 'string') {
          valorA = valorA.toLowerCase();
          valorB = valorB.toLowerCase();
        }

        let resultado = valorA < valorB ? -1 : valorA > valorB ? 1 : 0;
        return this.direccionOrden === 'desc' ? -resultado : resultado;
      });
    } else {
      // Ordenamiento por defecto: por ID (más recientes primero o último según dirección)
      divisiones.sort((a, b) => {
        return this.direccionOrden === 'desc' ? a.id - b.id : b.id - a.id;
      });
    }

    this.divisionesFiltradas = divisiones;
    this.actualizarPaginacion();
  }

  actualizarPaginacion(): void {
    const totalElements = this.divisionesFiltradas.length;
    const size = this.itemsPorPagina;
    const totalPages = Math.ceil(totalElements / size);
    
    // Ajustar página actual si es necesario
    if (this.currentPage > totalPages && totalPages > 0) {
      this.currentPage = 1;
    }
    
    const startIndex = (this.currentPage - 1) * size;
    const endIndex = startIndex + size;
    const content = this.divisionesFiltradas.slice(startIndex, endIndex);
    
    this.resultsPage = {
      content: content,
      totalElements: totalElements,
      totalPages: totalPages,
      size: size,
      number: this.currentPage - 1,
      first: this.currentPage === 1,
      last: this.currentPage === totalPages || totalPages === 0,
      numberOfElements: content.length
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
    this.filtroTurno = '';
    this.filtroAnio = '';
    this.ordenActual = '';
    this.direccionOrden = 'asc';
    this.currentPage = 1;
    this.onFiltroChange();
  }

  // Mantener método original para compatibilidad si es necesario
  getDivisiones(): void {
    this.divisionService.byPage(this.currentPage, 7).subscribe((response) => {
      this.resultsPage = <ResultsPage>response.data;
    });
  }

  eliminarDivision(division: any): void {
    this.modalService
      .confirm(
        "Eliminar División",
        `¿Está seguro que desea eliminar la división ${division.anio}º ${division.numDivision}º turno ${division.turno}?`,
        "Esta acción no se puede deshacer"
      )
      .then(
        () => {
          // Si el usuario confirma
          this.divisionService.delete(division).subscribe({
            next: (response) => {
              this.mensaje = response.message;
              if (response.status === 200) {
                this.modalService.alert("Éxito", this.mensaje);
              } else {
                this.modalService.alert("Error", this.mensaje);
              }
              // Recargar datos después de eliminar
              if (this.usandoFiltros) {
                this.cargarTodasLasDivisiones();
              } else {
                this.cargarDivisionesPaginadas();
              }
            },
            error: (err) => {
              console.error("Error al eliminar la división:", err);
              this.mensaje = "Error al eliminar la división.";
              this.modalService.alert("Error", this.mensaje);
            },
          });
        },
        () => {
          // Si el usuario cancela
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
      this.cargarDivisionesPaginadas();
    }
  }
}
