import { CommonModule } from "@angular/common";
import { ChangeDetectorRef, Component } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { Licencia } from "./licencia";
import { LicenciaService } from "./licencia.service";
import { ActivatedRoute, Router } from "@angular/router";
import { Persona } from "../personas/persona";
import { ArticuloLicencia } from "../articulo-licencia/articulo-licencia";
import { ArticuloLicenciaService } from "../articulo-licencia/articulo-licencia.service";
import { PersonaService } from "../personas/persona.service";

@Component({
  selector: "app-licencia-detail",
  imports: [CommonModule, FormsModule],
  templateUrl: "./licencia-detail.component.html",
  styleUrls: ["../global-styles/detail-styles.css"],
})
export class LicenciaDetailComponent {
  licencia: Licencia = {
    id: 0,
    pedidoDesde: "",
    pedidoHasta: "",
    domicilio: "",
    certificadoMedico: false,
    articuloLicencia: {
      id: 0,
      articulo: "",
      descripcion: "",
    },
    persona: {
      id: 0,
      dni: null,
      cuil: "",
      nombre: "",
      apellido: "",
      titulo: null,
      sexo: "",
      domicilio: "",
      telefono: "",
      designaciones: [],
    },
    designaciones: [],
  };

  mensaje: string = "";
  isNew: boolean = true;
  isError: boolean = false;

  personas: Persona[] = []; // Añadido
  articulosLicencia: ArticuloLicencia[] = []; // Añadido

  isValidDateRange: boolean = true; // Añadido
  minFechaFin: string = ""; // Añadido
  maxFechaInicio: string | null = null; // Añadido

  constructor(
    private licenciaService: LicenciaService,
    private personaService: PersonaService, // Añadido
    private articuloLicenciaService: ArticuloLicenciaService, // Añadido
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.getLicencias();
    this.cargarPersonas(); // Añadido
    this.cargarArticulosLicencia(); // Añadido
  }

  getLicencias(): void {
    this.route.paramMap.subscribe((params) => {
      const id = params.get("id");

      this.resetForm();

      if (id === "new") {
        this.isNew = true;
      } else {
        this.isNew = false;
        this.licenciaService.findById(Number(id)).subscribe({
          next: (response) => {
            this.licencia = response.data as Licencia;
            this.onFechaInicioChange();
            this.onFechaFinChange();
          },
          error: (err) => {
            console.error("Error al obtener la división:", err);
            this.mensaje = "Error al cargar la división.";
          },
        });
      }
    });
  }

  cargarPersonas(): void {
    // Añadido
    this.personaService.findAll().subscribe({
      next: (response) => {
        this.personas = response.data as Persona[];
      },
      error: (err) => {
        console.error("Error al cargar personas:", err);
        this.mensaje = "Error al cargar la lista de personas.";
        this.isError = true;
      },
    });
  }

  cargarArticulosLicencia(): void {
    // Añadido
    this.articuloLicenciaService.findAll().subscribe({
      next: (response) => {
        this.articulosLicencia = response.data as ArticuloLicencia[];
      },
      error: (err) => {
        console.error("Error al cargar artículos de licencia:", err);
        this.mensaje = "Error al cargar la lista de artículos de licencia.";
        this.isError = true;
      },
    });
  }

  saveLicencia(): void {
    if (this.isNew) {
      this.licenciaService.createLicencia(this.licencia).subscribe({
        next: (response) => {
          this.mensaje = response.message;
          this.isError = response.status !== 200;
          this.cdr.detectChanges(); // forzar deteccion de cambios
          this.scrollToMessage(); // desplazar la vista al mensaje
        },
        error: (err) => {
          this.mensaje = err.error?.message || "Error al crear la licencia.";
          this.isError = true;
          this.cdr.detectChanges();
          this.scrollToMessage();
        },
      });
    } else {
      this.licenciaService.updateLicencia(this.licencia).subscribe({
        next: (response) => {
          this.mensaje = response.message;
          this.isError = response.status !== 200;
          this.cdr.detectChanges(); // forzar deteccion de cambios
          this.scrollToMessage(); // desplazar la vista al mensaje
        },
        error: (err) => {
          // Añadido manejo de error
          this.mensaje =
            err.error?.message || "Error al actualizar la licencia.";
          this.isError = true;
          this.cdr.detectChanges();
          this.scrollToMessage();
        },
      });
    }
  }

 private resetForm(): void {
    const defaultPersona = {
      id: 0, dni: null, cuil: "", nombre: "", apellido: "",
      titulo: null, sexo: "", domicilio: "", telefono: "", designaciones: []
    };
    const defaultArticulo = { id: 0, articulo: "", descripcion: "" };

    this.licencia = {
      id: 0,
      pedidoDesde: "", 
      pedidoHasta: "", 
      domicilio: "",
      certificadoMedico: false,
      articuloLicencia: defaultArticulo,
      persona: defaultPersona,
      designaciones: [],
    };
    this.mensaje = "";
    this.isError = false;
    this.isValidDateRange = true; // Añadido
    this.minFechaFin = ""; // Añadido
    this.maxFechaInicio = null; // Añadido
  }

  scrollToMessage(): void {
    const messageElement = document.getElementById("message-container");
    if (messageElement) {
      messageElement.scrollIntoView({ behavior: "smooth" });
    }
  }

  comparePersonas(p1: Persona, p2: Persona): boolean { // Añadido
    return p1 && p2 ? p1.id === p2.id : p1 === p2;
  }

  compareArticulos(a1: ArticuloLicencia, a2: ArticuloLicencia): boolean { // Añadido
    return a1 && a2 ? a1.id === a2.id : a1 === a2;
  }

  onFechaInicioChange(): void { // Añadido
    if (this.licencia.pedidoDesde) {
      this.minFechaFin = this.licencia.pedidoDesde;
      this.validateDateRange();
    }
  }

  onFechaFinChange(): void { // Añadido
    if (this.licencia.pedidoHasta) {
      this.maxFechaInicio = this.licencia.pedidoHasta;
      this.validateDateRange();
    }
  }

    private validateDateRange(): void { // Añadido
    if (this.licencia.pedidoDesde && this.licencia.pedidoHasta) {
      this.isValidDateRange =
        new Date(this.licencia.pedidoHasta) >=
        new Date(this.licencia.pedidoDesde);
      if (!this.isValidDateRange) {
        this.mensaje = "La fecha de fin debe ser posterior o igual a la fecha de inicio.";
        this.isError = true;
      } else {
        if (this.mensaje === "La fecha de fin debe ser posterior o igual a la fecha de inicio.") {
          this.mensaje = "";
          this.isError = false;
        }  
      }
    } else {
      this.isValidDateRange = true; // Si una de las fechas no está, no se considera inválido el rango aún
    }
  }

    formatDateForInput(date: Date | string): string { // Añadido
    const d = new Date(date);
    let month = "" + (d.getMonth());  
    let day = "" + d.getDate();
    const year = d.getFullYear();

    if (month.length < 2) month = "0" + month;
    if (day.length < 2) day = "0" + day;

    return [year, month, day].join("-");
  }

  volver(): void { // Añadido
    this.router.navigate(["/licencias"]);
  }
}
