import { Component } from "@angular/core";
import { PersonaService } from "./persona.service";
import { Persona } from "./persona";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { ResultsPage } from "../results-page";
import { PaginationComponent } from "../pagination/pagination.component";

@Component({
  selector: "app-persona",
  imports: [RouterModule, CommonModule, PaginationComponent],
  templateUrl: './persona.component.html',
  styleUrls: ['./persona.component.css']
})
export class PersonaComponent {

  personas: Persona[] = [];
  resultsPage: ResultsPage = <ResultsPage>{};
  currentPage: number = 1;
  mensaje: string = ''; // Para manejar mensajes de error o éxito

  constructor(private personaService: PersonaService) {}

  ngOnInit(): void {
    this.getPersonas();
  }

  getPersonas(): void {
    this.personaService.byPage(this.currentPage, 7).subscribe((response) => {
      this.resultsPage = <ResultsPage>response.data;
    });
  }

  eliminarPersona(persona: Persona): void {
    if (confirm(`¿Está seguro que desea eliminar a ${persona.nombre} ${persona.apellido}?`)) {
      this.personaService.delete(persona).subscribe({
        next: (response) => {
          this.mensaje = response.message;
          alert(this.mensaje);
          this.getPersonas(); // Actualizar la lista
        },
        error: (err) => {
          console.error('Error al eliminar la persona:', err);
          this.mensaje = 'Error al eliminar la persona.';
        },
      });
    }
  }

  onPageChangeRequested(page: number): void {
    this.currentPage = page;
    this.getPersonas();
  }

}
