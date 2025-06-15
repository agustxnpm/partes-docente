import { Component } from "@angular/core";
import { ResultsPage } from "../results-page";
import { RouterModule } from "@angular/router";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { PaginationComponent } from "../pagination/pagination.component";
import { DesignacionesService } from "./designaciones.service";
import { ModalService } from "../modal/modal.service";
import { Designacion } from "./designacion";

@Component({
  selector: "app-designaciones",
  imports: [RouterModule, CommonModule, FormsModule, PaginationComponent],
  templateUrl: "./designaciones.component.html",
  styleUrls: ["../global-styles/table-styles.css", "./designaciones.component.css"],
})
export class DesignacionesComponent {
  resultsPage: ResultsPage = <ResultsPage>{};
  currentPage: number = 1;
  mensaje: string = "";
  
  // Filtros
  filtroTexto: string = '';
  filtroSituacionRevista: string = '';
  filtroVigencia: string = '';
  
  // Ordenamiento
  ordenActual: string = '';
  direccionOrden: 'asc' | 'desc' = 'asc';
  
  // Datos para enfoque híbrido
  todasLasDesignaciones: Designacion[] = [];
  designacionesFiltradas: Designacion[] = [];
  loading: boolean = false;
  
  // Control del modo híbrido
  usandoFiltros: boolean = false;
  itemsPorPagina: number = 7;

  constructor(
    private designacionService: DesignacionesService,
    private modalService: ModalService
  ) {}

  ngOnInit(): void {
    this.cargarDesignacionesPaginadas();
  }

  // Cargar designaciones con paginación de servidor (orden natural: más recientes primero)
  cargarDesignacionesPaginadas(): void {
    this.loading = true;
    this.designacionService.byPage(this.currentPage, this.itemsPorPagina).subscribe({
      next: (response) => {
        this.resultsPage = <ResultsPage>response.data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar designaciones:', error);
        this.loading = false;
      }
    });
  }

  // Cargar todas las designaciones para filtrado local
  cargarTodasLasDesignaciones(): void {
    this.loading = true;
    this.designacionService.findAll().subscribe({
      next: (response) => {
        this.todasLasDesignaciones = Array.isArray(response.data) ? response.data : [];
        this.aplicarFiltrosYOrden();
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar todas las designaciones:', error);
        this.loading = false;
      }
    });
  }

  // Detectar si hay filtros aplicados
  get hayFiltrosAplicados(): boolean {
    return this.filtroTexto.trim() !== '' || 
           this.filtroSituacionRevista !== '' || 
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
      this.cargarTodasLasDesignaciones();
    } else if (!hayFiltros && this.usandoFiltros) {
      // Volver a modo paginado: cargar primera página
      this.usandoFiltros = false;
      this.currentPage = 1;
      this.cargarDesignacionesPaginadas();
    } else if (hayFiltros && this.usandoFiltros) {
      // Ya estamos en modo filtros: solo aplicar filtros
      this.aplicarFiltrosYOrden();
    }
  }

  aplicarFiltrosYOrden(): void {
    let designacionesFiltradas = [...this.todasLasDesignaciones];

    // Aplicar filtro por texto (persona o cargo)
    if (this.filtroTexto) {
      designacionesFiltradas = designacionesFiltradas.filter(designacion =>
        designacion.persona.nombre.toLowerCase().includes(this.filtroTexto.toLowerCase()) ||
        designacion.persona.apellido.toLowerCase().includes(this.filtroTexto.toLowerCase()) ||
        designacion.cargo.nombre.toLowerCase().includes(this.filtroTexto.toLowerCase())
      );
    }

    // Aplicar filtro por situación de revista
    if (this.filtroSituacionRevista) {
      designacionesFiltradas = designacionesFiltradas.filter(designacion => {
        // Normalizar ambos valores para comparación case-insensitive
        const situacionBuscada = this.filtroSituacionRevista.toUpperCase();
        const situacionDesignacion = designacion.situacionRevista.toUpperCase();
        return situacionDesignacion === situacionBuscada;
      });
    }

    // Aplicar filtro por vigencia
    if (this.filtroVigencia) {
      const ahora = new Date();
      designacionesFiltradas = designacionesFiltradas.filter(designacion => {
        if (this.filtroVigencia === 'vigente') {
          return !designacion.fechaFin || new Date(designacion.fechaFin) > ahora;
        } else if (this.filtroVigencia === 'vencido') {
          return designacion.fechaFin && new Date(designacion.fechaFin) <= ahora;
        }
        return true;
      });
    }

    // Aplicar ordenamiento
    if (this.ordenActual) {
      designacionesFiltradas.sort((a, b) => {
        let valorA: any, valorB: any;

        switch (this.ordenActual) {
          case 'persona':
            valorA = `${a.persona.apellido} ${a.persona.nombre}`.toLowerCase();
            valorB = `${b.persona.apellido} ${b.persona.nombre}`.toLowerCase();
            break;
          case 'cargo':
            valorA = a.cargo.nombre.toLowerCase();
            valorB = b.cargo.nombre.toLowerCase();
            break;
          case 'fechaInicio':
            valorA = new Date(a.fechaInicio);
            valorB = new Date(b.fechaInicio);
            break;
          case 'fechaFin':
            valorA = a.fechaFin ? new Date(a.fechaFin) : new Date('9999-12-31');
            valorB = b.fechaFin ? new Date(b.fechaFin) : new Date('9999-12-31');
            break;
          case 'situacionRevista':
            valorA = a.situacionRevista.toLowerCase();
            valorB = b.situacionRevista.toLowerCase();
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
      designacionesFiltradas.sort((a, b) => {
        return this.direccionOrden === 'desc' ? a.id - b.id : b.id - a.id;
      });
    }

    this.designacionesFiltradas = designacionesFiltradas;
    this.actualizarPaginacion();
  }

  actualizarPaginacion(): void {
    const totalElementos = this.designacionesFiltradas.length;
    const totalPaginas = Math.ceil(totalElementos / this.itemsPorPagina);
    
    // Asegurar que la página actual sea válida
    if (this.currentPage > totalPaginas && totalPaginas > 0) {
      this.currentPage = totalPaginas;
    } else if (this.currentPage < 1) {
      this.currentPage = 1;
    }

    const inicio = (this.currentPage - 1) * this.itemsPorPagina;
    const fin = inicio + this.itemsPorPagina;
    const contenidoPagina = this.designacionesFiltradas.slice(inicio, fin);

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
    this.filtroSituacionRevista = '';
    this.filtroVigencia = '';
    this.ordenActual = '';
    this.direccionOrden = 'asc';
    this.currentPage = 1;
    this.onFiltroChange();
  }

  eliminarDesignacion(designacion: any): void {
    const mensaje = designacion.cargo.division
      ? `¿Está seguro que desea eliminar la designación de ${designacion.persona.nombre} ${designacion.persona.apellido} para el cargo ${designacion.cargo.nombre} de ${designacion.cargo.division.anio}º ${designacion.cargo.division.numDivision}º?`
      : `¿Está seguro que desea eliminar la designación de ${designacion.persona.nombre} ${designacion.persona.apellido} para el cargo institucional ${designacion.cargo.nombre}?`;

    this.modalService
      .confirm(
        "Eliminar Designación",
        mensaje,
        "Esta acción no se puede deshacer"
      )
      .then(
        () => {
          // Si el usuario confirma
          this.designacionService.delete(designacion).subscribe({
            next: (response) => {
              this.mensaje = response.message;
              if (response.status === 200) {
                this.modalService.alert("Éxito", this.mensaje);
                // Recargar datos después de eliminar
                if (this.usandoFiltros) {
                  this.cargarTodasLasDesignaciones();
                } else {
                  this.cargarDesignacionesPaginadas();
                }
              } else {
                this.modalService.alert("Error", this.mensaje);
              }
            },
            error: (err) => {
              console.error("Error al eliminar la designación:", err);
              this.mensaje = "Error al eliminar la designación.";
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
      this.cargarDesignacionesPaginadas();
    }
  }
}
