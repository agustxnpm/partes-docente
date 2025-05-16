import { CommonModule } from "@angular/common";
import { ChangeDetectorRef, Component } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { ActivatedRoute, Router, RouterModule } from "@angular/router";
import { DesignacionesService } from "./designaciones.service";
import { PersonaService } from "../personas/persona.service";
import { CargoService } from "../cargos/cargo.service";
import { Persona } from "../personas/persona";
import { Cargo } from "../cargos/cargo";
import { ModalService } from "../modal/modal.service";

@Component({
  selector: "app-designaciones-detail",
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: "./designaciones-detail.component.html",
  styleUrls: ["../global-styles/detail-styles.css"],
  standalone: true,
})
export class DesignacionesDetailComponent {
  designacion: any = {
    id: 0,
    persona: null,
    cargo: null,
    situacionRevista: "",
    fechaInicio: null,
    fechaFin: null,
  };

  personas: Persona[] = [];
  cargos: Cargo[] = [];
  situacionesRevista: string[] = [
    "TITULAR",
    "INTERINO",
    "SUPLENTE",
    "TEMPORAL",
  ];

  mensaje: string = "";
  isNew: boolean = true;
  isError: boolean = false;

  constructor(
    private designacionesService: DesignacionesService,
    private personaService: PersonaService,
    private cargoService: CargoService,
    private route: ActivatedRoute,
    private router: Router,
    private modalService: ModalService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.getDesignacion();
    this.cargarPersonasYCargos();
  }

  getDesignacion(): void {
    this.route.paramMap.subscribe((params) => {
      const id = params.get("id");

      this.resetForm();

      if (id === "new") {
        this.isNew = true;
      } else {
        this.isNew = false;
        this.designacionesService.findById(Number(id)).subscribe({
          next: (response) => {
            this.designacion = response.data;
            // Formatear fechas para el input type="date"
            if (this.designacion.fechaInicio) {
              this.designacion.fechaInicio = this.formatDateForInput(
                new Date(this.designacion.fechaInicio)
              );
            }
            if (this.designacion.fechaFin) {
              this.designacion.fechaFin = this.formatDateForInput(
                new Date(this.designacion.fechaFin)
              );
            }
          },
          error: (err) => {
            console.error("Error al obtener la designación:", err);
            this.mensaje = "Error al cargar la designación.";
            this.isError = true;
          },
        });
      }
    });
  }

  cargarPersonasYCargos(): void {
    this.personaService.findAll().subscribe({
      next: (response) => {
        this.personas = response.data as Persona[];
        this.isError = response.status !== 200;
      },
      error: (err) => {
        console.error("Error al cargar personas:", err);
        this.mensaje = "Error al cargar la lista de personas.";
        this.isError = true;
      },
    });

    this.cargoService.findAll().subscribe({
      next: (response) => {
        this.cargos = response.data as Cargo[];
        this.isError = response.status !== 200;
      },
      error: (err) => {
        console.error("Error al cargar cargos:", err);
        this.mensaje = "Error al cargar la lista de cargos.";
        this.isError = true;
      },
    });
  }

  saveDesignacion(): void {
    // crear una copia de la designacion para evitar alterar el objeto original y afectar la interfaz
    const designacionCopia = { ...this.designacion };

    if (designacionCopia.fechaInicio) {
      designacionCopia.fechaInicio = new Date(designacionCopia.fechaInicio);
    }

    if (designacionCopia.fechaFin) {
      designacionCopia.fechaFin = new Date(designacionCopia.fechaFin);
    }

    if (this.isNew) {
      this.designacionesService.createDesignacion(designacionCopia).subscribe({
        next: (response) => {
          this.mensaje = response.message;
          this.isError = response.status !== 200;
          this.cdr.detectChanges(); // forzar deteccion de cambios
          this.scrollToMessage(); // desplazar la vista al mensaje
        },
        error: (err) => {
          this.mensaje = err.error.message || "Error al crear la designación.";
          this.isError = true;
          console.error(err);
        },
      });
    } else {
      this.designacionesService.updateDesignacion(designacionCopia).subscribe({
        next: (response) => {
          this.mensaje = response.message;
          this.isError = response.status !== 200;
          this.cdr.detectChanges(); // forzar deteccion de cambios
          this.scrollToMessage(); // desplazar la vista al mensaje
        },
        error: (err) => {
          this.mensaje =
            err.error.message || "Error al actualizar la designación.";
          this.isError = true;
          console.error(err);
        },
      });
    }
  }

  formatDateForInput(date: Date): string {
    const d = new Date(date);
    let month = "" + (d.getMonth() + 1);
    let day = "" + d.getDate();
    const year = d.getFullYear();

    if (month.length < 2) month = "0" + month;
    if (day.length < 2) day = "0" + day;

    return [year, month, day].join("-");
  }

  isCargoInstitucional(): boolean {
    return this.designacion.cargo && !this.designacion.cargo.division;
  }

  volver(): void {
    this.router.navigate(["/designaciones"]);
  }

  isValidDateRange: boolean = true;
  minFechaFin: string = "";
  maxFechaInicio: string | null = null;

  onFechaFinChange(): void {
    if (this.designacion.fechaFin) {
      this.maxFechaInicio = this.designacion.fechaFin;

      if (
        this.designacion.fechaInicio &&
        new Date(this.designacion.fechaInicio) >
          new Date(this.designacion.fechaFin)
      ) {
        this.designacion.fechaFin = null;
        this.mensaje =
          "La fecha de fin no puede ser anterior a la fecha de inicio";
        this.isError = true;
        this.isValidDateRange = false;
      } else {
        this.isValidDateRange = true;
      }
    } else {
      this.maxFechaInicio = null;
      this.isValidDateRange = true;
    }
  }

  onFechaInicioChange(): void {
    if (this.designacion.fechaInicio) {
      this.minFechaFin = this.designacion.fechaInicio;

      if (
        this.designacion.fechaFin &&
        new Date(this.designacion.fechaInicio) >
          new Date(this.designacion.fechaFin)
      ) {
        this.designacion.fechaFin = null;
        this.mensaje =
          "La fecha de fin debe ser posterior a la fecha de inicio";
        this.isError = true;
        this.isValidDateRange = false;
      } else {
        this.isValidDateRange = true;
      }
    }
  }

  comparePersonas(persona1: any, persona2: any): boolean {
    return persona1 && persona2
      ? persona1.id === persona2.id
      : persona1 === persona2;
  }

  compareCargos(cargo1: any, cargo2: any): boolean {
    return cargo1 && cargo2 ? cargo1.id === cargo2.id : cargo1 === cargo2;
  }

  private resetForm(): void {
    this.designacion = {
      id: 0,
      persona: null,
      cargo: null,
      situacionRevista: "",
      fechaInicio: null,
      fechaFin: null,
    };
    this.mensaje = "";
    this.isError = false;
    this.isValidDateRange = true;
    this.minFechaFin = "";
    this.maxFechaInicio = null;
  }

  scrollToMessage(): void {
    const messageElement = document.getElementById("message-container");
    if (messageElement) {
      messageElement.scrollIntoView({ behavior: "smooth" });
    }
  }
}
