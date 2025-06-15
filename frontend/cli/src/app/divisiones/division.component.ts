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
  ordenarPor: 'anio' | 'numDivision' | 'orientacion' | 'turno' = 'anio';
  ordenDescendente: boolean = false;

  // Datos para filtrado local
  todasLasDivisiones: Division[] = [];
  divisionesFiltradas: Division[] = [];
  loading: boolean = false;

  // Enum para template
  Turno = Turno;

  constructor(
    private divisionService: DivisionService,
    private modalService: ModalService
  ) {}

  ngOnInit(): void {
    this.cargarTodasLasDivisiones();
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
    divisiones.sort((a, b) => {
      let valorA: any, valorB: any;
      
      switch (this.ordenarPor) {
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
      return this.ordenDescendente ? -resultado : resultado;
    });

    this.divisionesFiltradas = divisiones;
    this.actualizarPaginacion();
  }

  actualizarPaginacion(): void {
    const totalElements = this.divisionesFiltradas.length;
    const size = 7; // Tamaño de página
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

  onFiltroChange(): void {
    this.currentPage = 1;
    this.aplicarFiltrosYOrdenamiento();
  }

  limpiarFiltros(): void {
    this.filtroTexto = '';
    this.filtroTurno = '';
    this.filtroAnio = '';
    this.ordenarPor = 'anio';
    this.ordenDescendente = false;
    this.currentPage = 1;
    this.aplicarFiltrosYOrdenamiento();
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
              this.cargarTodasLasDivisiones(); // Recargar datos después de eliminar
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
    this.actualizarPaginacion();
  }
}
