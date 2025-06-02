import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";
import { RouterModule } from "@angular/router";
import { PaginationComponent } from "../pagination/pagination.component";
import { ResultsPage } from "../results-page";
import { LicenciaService } from "./licencia.service";
import { LogLicencia } from "./LogLicencia";
import { Licencia } from "./licencia";

@Component({
  selector: "app-licencia",
  imports: [RouterModule, CommonModule, PaginationComponent],
  templateUrl: "./licencia.component.html",
  styleUrls: ["../global-styles/table-styles.css"],
})
export class LicenciaComponent {
  resultsPage: ResultsPage = <ResultsPage>{};
  currentPage: number = 1;
  mensaje: string = "";

  // Para el modal de logs
  showLogsModal: boolean = false;
  logsLicencia: LogLicencia[] = [];
  licenciaSeleccionada: Licencia | null = null;

  constructor(private licenciaService: LicenciaService) {}

  ngOnInit(): void {
    this.getLicencias();
  }

  getLicencias(): void {
    this.licenciaService.byPage(this.currentPage, 7).subscribe((response) => {
      this.resultsPage = <ResultsPage>response.data;
    });
  }

  onPageChangeRequested(page: number): void {
    this.currentPage = page;
    this.getLicencias();
  }

  verLogs(licencia: Licencia): void {
    this.licenciaSeleccionada = licencia;
    this.licenciaService.getLogsByLicenciaId(licencia.id).subscribe({
      next: (response) => {
        this.logsLicencia = response.data as LogLicencia[];
        this.showLogsModal = true;
      },
      error: (err) => {
        console.error("Error al cargar logs:", err);
        this.logsLicencia = [];
        this.showLogsModal = true;
      },
    });
  }

  cerrarModalLogs(): void {
    this.showLogsModal = false;
    this.logsLicencia = [];
    this.licenciaSeleccionada = null;
  }
}
