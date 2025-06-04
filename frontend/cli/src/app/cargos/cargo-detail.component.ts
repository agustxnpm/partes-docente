import { ChangeDetectorRef, Component, OnInit } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import { CargoService } from "./cargo.service";
import { DivisionService } from "../divisiones/division.service";
import { Cargo } from "./cargo";
import { TipoDesignacion } from "./tipoDesignacion";
import { Division } from "../divisiones/division";
import { ModalService } from "../modal/modal.service";
import { TipoDesignacionFormatPipe } from "../shared/pipes/tipo-designacion-format.pipe";

@Component({
  selector: "app-cargo-detail",
  imports: [CommonModule, FormsModule, TipoDesignacionFormatPipe],
  templateUrl: "./cargo-detail.component.html",
  styleUrls: ["../global-styles/detail-styles.css"],
})
export class CargoDetailComponent {
  cargo: Cargo = {
    id: 0,
    nombre: "",
    cargaHoraria: null,
    fechaInicio: "",
    fechaFin: null,
    division: null,
    horario: [],
    tipoDesignacion: null,
  };

  divisiones: Division[] = [];
  tiposDesignacionArray = Object.values(TipoDesignacion);
  isNew: boolean = true; // si es un nuevo cargo
  mensaje: string = ""; // Para mostrar el mensaje del backend
  isError: boolean = false; // si el dataPackage no es 200
  showErrorNumero: boolean = false; // para mostrar el error de solo números
  TipoDesignacion = TipoDesignacion;
  minFechaFin: string = "";
  maxFechaInicio: string | null = null;
  fechaActual: string = new Date().toISOString().split("T")[0];

  constructor(
    private cargoService: CargoService,
    private divisionService: DivisionService,
    private modalService: ModalService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.getCargo();
    this.getDivisiones();
  }

  getCargo(): void {
    this.route.paramMap.subscribe((params) => {
      const id = params.get("id");
      this.resetForm(); // Limpiar el formulario antes de cualquier operación

      if (id === "new") {
        this.isNew = true;
      } else {
        this.isNew = false;
        this.cargoService.findById(Number(id)).subscribe({
          next: (response) => {
            this.cargo = response.data as Cargo;
          },
          error: (err) => {
            console.error("Error al obtener el cargo:", err);
            this.modalService.alert("Error", "Error al cargar el cargo.");
          },
        });
      }
    });
  }

  getDivisiones(): void {
    this.divisionService.findAll().subscribe({
      next: (response) => {
        this.divisiones = response.data as Division[];

        // Si estamos editando un cargo con división, asegurarse de que la división coincida
        if (!this.isNew && this.cargo.division) {
          const divisionEncontrada = this.divisiones.find(
            (d) =>
              this.cargo.division?.id !== undefined &&
              d.id === this.cargo.division.id
          );
          if (divisionEncontrada) {
            this.cargo.division = divisionEncontrada;
          }
        }
      },
      error: (err) => {
        console.error("Error al obtener las divisiones:", err);
        this.modalService.alert("Error", "Error al cargar las divisiones.");
      },
    });
  }

  saveCargo(): void {
    // Validar que si es ESPACIO_CURRICULAR tenga una división asignada
    if (
      this.cargo.tipoDesignacion === TipoDesignacion.ESPACIO_CURRICULAR &&
      !this.cargo.division
    ) {
      this.mensaje =
        "Debe seleccionar una división para un espacio curricular.";
      this.isError = true;
      return;
    }

    if (this.isNew) {
      this.cargoService.createCargo(this.cargo).subscribe({
        next: (response) => {
          this.mensaje = response.message;
          this.isError = response.status !== 200;
          this.cdr.detectChanges(); // forzar deteccion de cambios
          this.scrollToMessage(); // desplazar la vista al mensaje
        },
      });
    } else {
      this.cargoService.updateCargo(this.cargo).subscribe({
        next: (response) => {
          this.mensaje = response.message;
          this.isError = response.status !== 200;
          this.cdr.detectChanges(); // forzar deteccion de cambios
          this.scrollToMessage(); // desplazar la vista al mensaje
        },
      });
    }
  }

  onTipoDesignacionChange(): void {
    // Si no es ESPACIO_CURRICULAR, limpiar la división
    if (this.cargo.tipoDesignacion !== TipoDesignacion.ESPACIO_CURRICULAR) {
      this.cargo.division = null;
    }
  }

  volver(): void {
    this.router.navigate(["/cargos"]);
  }

  onlyNumbers(event: KeyboardEvent): boolean {
    const charCode = event.which ? event.which : event.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
      this.showErrorNumero = true;
      setTimeout(() => (this.showErrorNumero = false), 5000);
      return false;
    }
    return true;
  }

  isValidDateRange: boolean = true; // propiedad para controlar la validación de fechas

  onFechaInicioChange(): void {
    if (this.cargo.fechaInicio) {
      this.minFechaFin = this.cargo.fechaInicio;

      if (this.cargo.fechaFin && this.cargo.fechaFin < this.cargo.fechaInicio) {
        this.cargo.fechaFin = null;
        this.mensaje =
          "La fecha de fin debe ser posterior a la fecha de inicio";
        this.isValidDateRange = false;
        this.isError = true;
        this.scrollToMessage(); // desplazar la vista al mensaje
      } else {
        // Limpiar mensaje cuando las fechas son válidas
        this.isValidDateRange = true;
        this.clearErrorMessage();
      }
    }
  }

  onFechaFinChange(): void {
    if (this.cargo.fechaFin) {
      this.maxFechaInicio = this.cargo.fechaFin;

      if (
        this.cargo.fechaInicio &&
        this.cargo.fechaInicio > this.cargo.fechaFin
      ) {
        this.cargo.fechaFin = null;
        this.mensaje =
          "La fecha de fin no puede ser anterior a la fecha de inicio";
        this.isValidDateRange = false;
        this.isError = true;
        this.scrollToMessage(); // desplazar la vista al mensaje
      } else {
        // Limpiar mensaje cuando las fechas son válidas
        this.isValidDateRange = true;
        this.clearErrorMessage();
      }
    } else {
      this.maxFechaInicio = null;
      this.isValidDateRange = true;
      this.clearErrorMessage();
    }
  }

  // método para limpiar mensajes de error de fechas, para que no queden luego de poner la fecha valida
  private clearErrorMessage(): void {
    // Solo limpiar si el mensaje actual es de validación de fechas
    if (this.mensaje.includes("fecha") && this.isError) {
      this.mensaje = "";
      this.isError = false;
    }
  }

  private resetForm(): void {
    this.cargo = {
      id: 0,
      nombre: "",
      cargaHoraria: null,
      fechaInicio: "",
      fechaFin: null,
      division: null,
      horario: [],
      tipoDesignacion: null,
    };
    this.mensaje = "";
    this.isError = false;
    this.isValidDateRange = true;
  }

  scrollToMessage(): void {
    const messageElement = document.getElementById("message-container");
    if (messageElement) {
      messageElement.scrollIntoView({ behavior: "smooth" });
    }
  }
}
