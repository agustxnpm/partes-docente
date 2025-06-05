import { Component } from "@angular/core";
import { PersonaService } from "./persona.service";
import { Persona } from "./persona";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { ResultsPage } from "../results-page";
import { PaginationComponent } from "../pagination/pagination.component";
import { ModalService } from "../modal/modal.service";

@Component({
  selector: "app-persona",
  imports: [RouterModule, CommonModule, PaginationComponent],
  templateUrl: "./persona.component.html",
  styleUrls: ["../global-styles/table-styles.css"],
})
export class PersonaComponent {
  personas: Persona[] = [];
  resultsPage: ResultsPage = <ResultsPage>{};
  currentPage: number = 1;
  mensaje: string = ""; // Para manejar mensajes de error o éxito

  constructor(
    private personaService: PersonaService,
    private modalService: ModalService
  ) {}

  ngOnInit(): void {
    this.getPersonas();
  }

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
              this.getPersonas();
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
    this.getPersonas();
  }
}
