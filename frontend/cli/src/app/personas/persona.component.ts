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
  ordenarPor: 'nombre' | 'apellido' | 'dni' | 'cuil' = 'apellido';
  ordenDescendente: boolean = false;

  // Datos para filtrado local
  todasLasPersonas: Persona[] = [];
  personasFiltradas: Persona[] = [];
  loading: boolean = false;

  constructor(
    private personaService: PersonaService,
    private modalService: ModalService
  ) {}

  ngOnInit(): void {
    this.cargarTodasLasPersonas();
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
    personas.sort((a, b) => {
      let valorA: any, valorB: any;
      
      switch (this.ordenarPor) {
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
      return this.ordenDescendente ? -resultado : resultado;
    });

    this.personasFiltradas = personas;
    this.actualizarPaginacion();
  }

  actualizarPaginacion(): void {
    const totalElements = this.personasFiltradas.length;
    const size = 7; // Tamaño de página
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

  onFiltroChange(): void {
    this.currentPage = 1;
    this.aplicarFiltrosYOrdenamiento();
  }

  limpiarFiltros(): void {
    this.filtroTexto = '';
    this.filtroSexo = '';
    this.ordenarPor = 'apellido';
    this.ordenDescendente = false;
    this.currentPage = 1;
    this.aplicarFiltrosYOrdenamiento();
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
              this.cargarTodasLasPersonas(); // Recargar datos después de eliminar
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
    this.actualizarPaginacion();
  }
}
