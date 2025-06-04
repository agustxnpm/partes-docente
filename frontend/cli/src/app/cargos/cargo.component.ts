import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";
import { RouterModule } from "@angular/router";
import { PaginationComponent } from "../pagination/pagination.component";
import { ResultsPage } from "../results-page";
import { CargoService } from "./cargo.service";
import { ModalService } from "../modal/modal.service";
import { Cargo } from "./cargo";
import { TipoDesignacionFormatPipe } from '../shared/pipes/tipo-designacion-format.pipe';


@Component({
  selector: "app-cargo",
  imports: [CommonModule, RouterModule, PaginationComponent, TipoDesignacionFormatPipe],
  templateUrl: "./cargo.component.html",
  styleUrls: ["../global-styles/table-styles.css"],
})
export class CargoComponent {
  resultsPage: ResultsPage = <ResultsPage>{};
  currentPage: number = 1;
  mensaje: string = "";

  constructor(
    private cargoService: CargoService,
    private modalService: ModalService
  ) {}

  ngOnInit(): void {
    this.getCargos();
  }

  getCargos(): void {
    this.cargoService.byPage(this.currentPage, 7).subscribe((response) => {
      this.resultsPage = <ResultsPage>response.data;
    });
  }

  eliminarCargo(cargo: Cargo): void {
    this.modalService
      .confirm(
        "Eliminar Cargo",
        `¿Está seguro que desea eliminar el cargo ${cargo.nombre}?`,
        "Esta acción no se puede deshacer"
      )
      .then(
        () => {
          this.cargoService.delete(cargo).subscribe({
            next: (response) => {
              this.mensaje = response.message;
              if (response.status === 200) {
                this.modalService.alert("Éxito", this.mensaje);
              } else {
                this.modalService.alert("Error", this.mensaje);
              }
              this.getCargos();
            },
            error: (err) => {
              console.error("Error al eliminar el cargo:", err);
              this.mensaje = "Error al eliminar el cargo.";
              this.modalService.alert("Error", this.mensaje);
            },
          });
        },
        () => {
          console.log("Operación cancelada");
        }
      );
  }

  onPageChangeRequested(page: number): void {
    this.currentPage = page;
    this.getCargos();
  }
}
