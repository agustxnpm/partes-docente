import { Component } from "@angular/core";
import { ResultsPage } from "../results-page";
import { DivisionService } from "./division.service";
import { PaginationComponent } from "../pagination/pagination.component";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { ModalService } from "../modal/modal.service";

@Component({
  selector: "app-division",
  imports: [RouterModule, CommonModule, PaginationComponent],
  templateUrl: "./division.component.html",
  styleUrls: ["../global-styles/table-styles.css"],
})
export class DivisionComponent {
  resultsPage: ResultsPage = <ResultsPage>{};
  currentPage: number = 1;
  mensaje: string = "";

  constructor(
    private divisionService: DivisionService,
    private modalService: ModalService
  ) {}

  ngOnInit(): void {
    this.getDivisiones();
  }

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
              this.modalService.alert("Éxito", this.mensaje);
              this.getDivisiones();
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
    this.getDivisiones();
  }
}
