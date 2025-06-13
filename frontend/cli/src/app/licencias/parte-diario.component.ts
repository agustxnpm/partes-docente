import { Component, OnInit } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { LicenciaService } from "./licencia.service";
import { ParteDiarioDTO, DocenteLicenciaDTO, ParteDiarioResponse } from "./parte-diario.dto";

@Component({
  selector: "app-parte-diario",
  imports: [CommonModule, FormsModule],
  templateUrl: "./parte-diario.component.html",
  styleUrls: ["../global-styles/table-styles.css"],
})
export class ParteDiarioComponent implements OnInit {
  fechaConsulta: string = "";
  parteDiario: ParteDiarioDTO | null = null;
  docentes: DocenteLicenciaDTO[] = [];
  loading: boolean = false;
  mensaje: string = "";
  tipoMensaje: "success" | "error" | "info" = "info";

  constructor(private licenciaService: LicenciaService) {
    // Establecer fecha actual como valor por defecto
    const today = new Date();
    this.fechaConsulta = today.toISOString().split('T')[0];
  }

  ngOnInit(): void {
    // Opcionalmente cargar datos automáticamente para la fecha actual
    // this.consultarParteDiario();
  }

  consultarParteDiario(): void {
    if (!this.fechaConsulta) {
      this.mostrarMensaje("Por favor, seleccione una fecha.", "error");
      return;
    }

    this.loading = true;
    this.mensaje = "";
    this.docentes = [];

    this.licenciaService.obtenerParteDiario(this.fechaConsulta).subscribe({
      next: (response) => {
        this.loading = false;
        if (response.data) {
          const parteDiarioResponse = response.data as ParteDiarioResponse;
          this.parteDiario = parteDiarioResponse.ParteDiario;
          this.docentes = this.parteDiario.Docentes || [];
          
          if (this.docentes.length === 0) {
            this.mostrarMensaje(`No hay licencias activas para la fecha ${this.fechaConsulta}.`, "info");
          } else {
            this.mostrarMensaje(`Se encontraron ${this.docentes.length} licencia(s) activa(s) para la fecha ${this.fechaConsulta}.`, "success");
          }
        } else {
          this.mostrarMensaje("No se pudieron obtener los datos del parte diario.", "error");
        }
      },
      error: (error) => {
        this.loading = false;
        console.error("Error al obtener parte diario:", error);
        this.mostrarMensaje("Error al consultar el parte diario. Por favor, intente nuevamente.", "error");
      }
    });
  }

  private mostrarMensaje(texto: string, tipo: "success" | "error" | "info"): void {
    this.mensaje = texto;
    this.tipoMensaje = tipo;
  }

  limpiarConsulta(): void {
    this.parteDiario = null;
    this.docentes = [];
    this.mensaje = "";
    this.fechaConsulta = new Date().toISOString().split('T')[0];
  }

  formatearFecha(fecha: string): string {
    if (!fecha) return '';
    const date = new Date(fecha);
    return date.toLocaleDateString('es-ES', {
      day: '2-digit',
      month: '2-digit', 
      year: 'numeric'
    });
  }
}
