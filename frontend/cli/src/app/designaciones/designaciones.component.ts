import { Component } from "@angular/core";
import { ResultsPage } from "../results-page";
import { RouterModule } from "@angular/router";
import { CommonModule } from "@angular/common";
import { PaginationComponent } from "../pagination/pagination.component";
import { DesignacionesService } from "./designaciones.service";
import { ModalService } from "../modal/modal.service";

@Component({
  selector: "app-designaciones",
  imports: [RouterModule, CommonModule, PaginationComponent],
  templateUrl: "./designaciones.component.html",
  styleUrls: ["../global-styles/table-styles.css"],
})
export class DesignacionesComponent {
  resultsPage: ResultsPage = <ResultsPage>{};
  currentPage: number = 1;
  mensaje: string = "";

  constructor(
    private designacionService: DesignacionesService,
    private modalService: ModalService
  ) {}

  ngOnInit(): void {
    this.getDesignaciones();
  }

  getDesignaciones(): void {
    this.designacionService
      .byPage(this.currentPage, 7)
      .subscribe((response) => {
        this.resultsPage = <ResultsPage>response.data;
      });
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
              } else {
                this.modalService.alert("Error", this.mensaje);
              }
              this.getDesignaciones();
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
    this.getDesignaciones();
  }
}
