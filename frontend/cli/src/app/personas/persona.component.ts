import { Component } from "@angular/core";
import { PersonaService } from "./persona.service";
import { Persona } from "./persona";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { FormsModule } from "@angular/forms";
import { ResultsPage } from "../results-page";
import { PaginationComponent } from "../pagination/pagination.component";
import { ModalService } from "../modal/modal.service";

@Component({
  selector: "app-persona",
  imports: [RouterModule, CommonModule, PaginationComponent, FormsModule],
  templateUrl: "./persona.component.html",
  styleUrls: ["../global-styles/table-styles.css", "./persona.component.css"],
})
export class PersonaComponent {
  personas: Persona[] = [];
  resultsPage: ResultsPage = <ResultsPage>{};
  currentPage: number = 1;
  mensaje: string = ""; // Para manejar mensajes de error o éxito

  // Propiedades para filtros y ordenamiento
  filtroTexto: string = '';
  filtroSexo: string = '';
  ordenActual: string = '';
  direccionOrden: 'asc' | 'desc' = 'asc';

  // Datos para enfoque híbrido
  todasLasPersonas: Persona[] = [];
  personasFiltradas: Persona[] = [];
  loading: boolean = false;
  
  // Control del modo híbrido
  usandoFiltros: boolean = false;
  itemsPorPagina: number = 7;

  constructor(
    private personaService: PersonaService,
    private modalService: ModalService
  ) {}

  ngOnInit(): void {
    this.cargarPersonasPaginadas();
  }

  // Cargar personas con paginación de servidor (orden natural: más recientes primero)
  cargarPersonasPaginadas(): void {
    this.loading = true;
    this.personaService.byPage(this.currentPage, this.itemsPorPagina).subscribe({
      next: (response) => {
        this.resultsPage = <ResultsPage>response.data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar personas:', error);
        this.loading = false;
      }
    });
  }

  cargarTodasLasPersonas(): void {
    this.loading = true;
    this.personaService.findAll().subscribe({
      next: (response) => {
        this.todasLasPersonas = (response.data as Persona[]) || [];
        this.aplicarFiltrosYOrdenamiento();
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar personas:', error);
        this.mensaje = 'Error al cargar las personas';
        this.loading = false;
      }
    });
  }

  // Detectar si hay filtros aplicados
  get hayFiltrosAplicados(): boolean {
    return this.filtroTexto.trim() !== '' || 
           this.filtroSexo !== '' ||
           this.ordenActual !== '' ||
           this.direccionOrden === 'desc'; // Incluir cuando se cambia la dirección por defecto
  }

  // Manejar cambios en filtros
  onFiltroChange(): void {
    const hayFiltros = this.hayFiltrosAplicados;

    if (hayFiltros && !this.usandoFiltros) {
      // Cambiar a modo filtros: cargar todos los datos
      this.usandoFiltros = true;
      this.cargarTodasLasPersonas();
    } else if (!hayFiltros && this.usandoFiltros) {
      // Volver a modo paginado: cargar primera página
      this.usandoFiltros = false;
      this.currentPage = 1;
      this.cargarPersonasPaginadas();
    } else if (hayFiltros && this.usandoFiltros) {
      // Ya estamos en modo filtros: solo aplicar filtros
      this.aplicarFiltrosYOrdenamiento();
    }
  }

  aplicarFiltrosYOrdenamiento(): void {
    let personas = [...this.todasLasPersonas];

    // Aplicar filtro de texto (nombre, apellido, DNI)
    if (this.filtroTexto) {
      const termino = this.filtroTexto.toLowerCase();
      personas = personas.filter(p => 
        p.nombre.toLowerCase().includes(termino) || 
        p.apellido.toLowerCase().includes(termino) ||
        (p.dni?.toString() || '').includes(termino) ||
        p.cuil.toLowerCase().includes(termino)
      );
    }

    // Aplicar filtro de sexo
    if (this.filtroSexo) {
      personas = personas.filter(p => p.sexo === this.filtroSexo);
    }

    // Aplicar ordenamiento
    if (this.ordenActual) {
      personas.sort((a, b) => {
        let valorA: any, valorB: any;
        
        switch (this.ordenActual) {
          case 'nombre':
            valorA = a.nombre;
            valorB = b.nombre;
            break;
          case 'apellido':
            valorA = a.apellido;
            valorB = b.apellido;
            break;
          case 'dni':
            valorA = a.dni;
            valorB = b.dni;
            break;
          case 'cuil':
            valorA = a.cuil;
            valorB = b.cuil;
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
      personas.sort((a, b) => {
        return this.direccionOrden === 'desc' ? a.id - b.id : b.id - a.id;
      });
    }

    this.personasFiltradas = personas;
    this.actualizarPaginacion();
  }

  actualizarPaginacion(): void {
    const totalElements = this.personasFiltradas.length;
    const size = this.itemsPorPagina;
    const totalPages = Math.ceil(totalElements / size);
    
    // Ajustar página actual si es necesario
    if (this.currentPage > totalPages && totalPages > 0) {
      this.currentPage = 1;
    }
    
    const startIndex = (this.currentPage - 1) * size;
    const endIndex = startIndex + size;
    const content = this.personasFiltradas.slice(startIndex, endIndex);
    
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
    this.filtroSexo = '';
    this.ordenActual = '';
    this.direccionOrden = 'asc';
    this.currentPage = 1;
    this.onFiltroChange();
  }

  // Mantener método original para compatibilidad si es necesario
  getPersonas(): void {
    this.personaService.byPage(this.currentPage, 7).subscribe((response) => {
      this.resultsPage = <ResultsPage>response.data;
    });
  }

  eliminarPersona(persona: Persona): void {
    this.modalService
      .confirm(
        "Eliminar Persona",
        `¿Está seguro que desea eliminar a ${persona.nombre} ${persona.apellido}?`,
        "Esta acción no se puede deshacer"
      )
      .then(
        () => {
          // Si el usuario confirma
          this.personaService.delete(persona).subscribe({
            next: (response) => {
              this.mensaje = response.message;
              if (response.status === 200) {
              this.modalService.alert("Éxito", this.mensaje);
              } else {
              this.modalService.alert("Error", this.mensaje);
              }
              // Recargar datos después de eliminar
              if (this.usandoFiltros) {
                this.cargarTodasLasPersonas();
              } else {
                this.cargarPersonasPaginadas();
              }
            },
            error: (err) => {
              console.error("Error al eliminar la persona:", err);
              this.mensaje = "Error al eliminar la persona.";
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
      this.cargarPersonasPaginadas();
    }
  }
}
