import { Component } from "@angular/core";
import { ResultsPage } from "../results-page";
import { DivisionService } from "./division.service";
import { PaginationComponent } from "../pagination/pagination.component";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";

@Component({
  selector: "app-division",
  imports: [RouterModule, CommonModule, PaginationComponent],
  templateUrl: "./division.component.html",
  styleUrls: ["./division.component.css"],
})
export class DivisionComponent {
  resultsPage: ResultsPage = <ResultsPage>{};
  currentPage: number = 1;
  mensaje: string = "";

  constructor(private divisionService: DivisionService) {}

  ngOnInit(): void {
    this.getDivisiones();
  }

  getDivisiones(): void {
    this.divisionService.byPage(this.currentPage, 7).subscribe((response) => {
      this.resultsPage = <ResultsPage>response.data;
    });
  }

  eliminarDivision(division: any): void {
    if (
      confirm(
        `¿Está seguro que desea eliminar la división ${division.anio}º ${division.numDivision}º Turno: ${division.turno}?`
      )
    ) {
      this.divisionService.delete(division).subscribe({
        next: (response) => {
          this.mensaje = response.message;
          alert(this.mensaje);
          this.getDivisiones(); // Actualizar la lista
        },
        error: (err) => {
          console.error("Error al eliminar la división:", err);
          this.mensaje = "Error al eliminar la división.";
        },
      });
    }
  }

  onPageChangeRequested(page: number): void {
    this.currentPage = page;
    this.getDivisiones();
  }
}
