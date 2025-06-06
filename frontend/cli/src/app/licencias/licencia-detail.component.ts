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
    estado: "INVALIDA",
  };

  mensaje: string = "";
  isNew: boolean = true;
  isError: boolean = false;

  personas: Persona[] = [];
  articulosLicencia: ArticuloLicencia[] = [];

  // Propiedades para controlar los dropdowns
  showPersonaDropdown = false;
  showArticuloDropdown = false;
  personasFiltradas: Persona[] = [];
  articulosFiltrados: ArticuloLicencia[] = [];

  // Variables para controlar los valores de input
  personaInputValue: string = '';
  articuloInputValue: string = '';

  isValidDateRange: boolean = true;
  minFechaFin: string = "";
  maxFechaInicio: string | null = null;

  constructor(
    private licenciaService: LicenciaService,
    private personaService: PersonaService,
    private articuloLicenciaService: ArticuloLicenciaService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.getLicencias();
    this.cargarPersonas();
    this.cargarArticulosLicencia();
  }

  cargarPersonas(): void {
    this.personaService.findAll().subscribe({
      next: (response) => {
        this.personas = response.data as Persona[];
        this.personasFiltradas = [...this.personas];
      },
      error: (err) => {
        console.error("Error al cargar personas:", err);
        this.mensaje = "Error al cargar la lista de personas.";
        this.isError = true;
      },
    });
  }

  cargarArticulosLicencia(): void {
    this.articuloLicenciaService.findAll().subscribe({
      next: (response) => {
        this.articulosLicencia = response.data as ArticuloLicencia[];
        this.articulosFiltrados = [...this.articulosLicencia];
      },
      error: (err) => {
        console.error("Error al cargar artículos de licencia:", err);
        this.mensaje = "Error al cargar la lista de artículos de licencia.";
        this.isError = true;
      },
    });
  }

  // Métodos para manejar el dropdown de personas
  onPersonaFocus(): void {
    this.showPersonaDropdown = true;
    this.personasFiltradas = [...this.personas];
  }

  onPersonaBlur(): void {
    setTimeout(() => {
      this.showPersonaDropdown = false;
    }, 150);
  }

  onPersonaInput(event: any): void {
    const term = event.target.value.toLowerCase();
    this.personaInputValue = event.target.value;
    
    if (term.length === 0) {
      this.licencia.persona = {
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
      };
      this.personasFiltradas = [...this.personas];
    } else {
      this.personasFiltradas = this.personas.filter(
        (persona) =>
          persona.nombre?.toLowerCase().includes(term) ||
          persona.apellido?.toLowerCase().includes(term) ||
          (persona.dni && persona.dni.toString().includes(term))
      );
    }
    this.showPersonaDropdown = true;
  }

  selectPersona(persona: Persona): void {
    this.licencia.persona = persona;
    this.personaInputValue = this.formatterPersona(persona);
    this.showPersonaDropdown = false;
  }

  // Métodos para manejar el dropdown de artículos
  onArticuloFocus(): void {
    this.showArticuloDropdown = true;
    this.articulosFiltrados = [...this.articulosLicencia];
  }

  onArticuloBlur(): void {
    setTimeout(() => {
      this.showArticuloDropdown = false;
    }, 150);
  }

  onArticuloInput(event: any): void {
    const term = event.target.value.toLowerCase();
    this.articuloInputValue = event.target.value;
    
    if (term.length === 0) {
      this.licencia.articuloLicencia = {
        id: 0,
        articulo: "",
        descripcion: "",
      };
      this.articulosFiltrados = [...this.articulosLicencia];
    } else {
      this.articulosFiltrados = this.articulosLicencia.filter(
        (articulo) =>
          articulo.articulo?.toLowerCase().includes(term) ||
          articulo.descripcion?.toLowerCase().includes(term)
      );
    }
    this.showArticuloDropdown = true;
  }

  selectArticulo(articulo: ArticuloLicencia): void {
    this.licencia.articuloLicencia = articulo;
    this.articuloInputValue = this.formatterArticulo(articulo);
    this.showArticuloDropdown = false;
  }

  // Formatters
  formatterPersona = (persona: Persona) => {
    if (!persona || persona.id === 0) return "";
    return `${persona.apellido}, ${persona.nombre} (${persona.dni})`;
  };

  formatterArticulo = (articulo: ArticuloLicencia) => {
    if (!articulo || articulo.id === 0) return "";
    return `${articulo.articulo} - ${articulo.descripcion}`;
  };

  getLicencias(): void {
    this.route.paramMap.subscribe((params) => {
      const id = params.get("id");

      this.resetForm();

      if (id === "new") {
        this.isNew = true;
        this.personaInputValue = '';
        this.articuloInputValue = '';
      } else {
        this.isNew = false;
        this.licenciaService.findById(Number(id)).subscribe({
          next: (response) => {
            this.licencia = response.data as Licencia;
            
            // Inicializar los valores de input
            this.personaInputValue = this.licencia.persona ? this.formatterPersona(this.licencia.persona) : '';
            this.articuloInputValue = this.licencia.articuloLicencia ? this.formatterArticulo(this.licencia.articuloLicencia) : '';
            
            this.onFechaInicioChange();
            this.onFechaFinChange();
          },
          error: (err) => {
            console.error("Error al obtener la licencia:", err);
            this.mensaje = "Error al cargar la licencia.";
          },
        });
      }
    });
  }

  saveLicencia(): void {
    if (this.isNew) {
      this.licenciaService.createLicencia(this.licencia).subscribe({
        next: (response) => {
          this.mensaje = response.message;
          this.isError = response.status !== 200;
          this.cdr.detectChanges();
          this.scrollToMessage();
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
          this.cdr.detectChanges();
          this.scrollToMessage();
        },
        error: (err) => {
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
      estado: "INVALIDA",
    };
    this.personaInputValue = '';
    this.articuloInputValue = '';
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

  comparePersonas(p1: Persona, p2: Persona): boolean {
    return p1 && p2 ? p1.id === p2.id : p1 === p2;
  }

  compareArticulos(a1: ArticuloLicencia, a2: ArticuloLicencia): boolean {
    return a1 && a2 ? a1.id === a2.id : a1 === a2;
  }

  onFechaInicioChange(): void {
    if (this.licencia.pedidoDesde) {
      this.minFechaFin = this.licencia.pedidoDesde;
      this.validateDateRange();
    }
  }

  onFechaFinChange(): void {
    if (this.licencia.pedidoHasta) {
      this.maxFechaInicio = this.licencia.pedidoHasta;
      this.validateDateRange();
    }
  }

  private validateDateRange(): void {
    if (this.licencia.pedidoDesde && this.licencia.pedidoHasta) {
      this.isValidDateRange =
        new Date(this.licencia.pedidoHasta) >=
        new Date(this.licencia.pedidoDesde);
      if (!this.isValidDateRange) {
        this.mensaje =
          "La fecha de fin debe ser posterior o igual a la fecha de inicio.";
        this.isError = true;
      } else {
        if (
          this.mensaje ===
          "La fecha de fin debe ser posterior o igual a la fecha de inicio."
        ) {
          this.mensaje = "";
          this.isError = false;
        }
      }
    } else {
      this.isValidDateRange = true;
    }
  }

  formatDateForInput(date: Date | string): string {
    const d = new Date(date);
    let month = "" + (d.getMonth() + 1);
    let day = "" + d.getDate();
    const year = d.getFullYear();

    if (month.length < 2) month = "0" + month;
    if (day.length < 2) day = "0" + day;

    return [year, month, day].join("-");
  }

  volver(): void {
    this.router.navigate(["/licencias"]);
  }

  isLicenciaOtorgada(): boolean {
    return this.licencia.estado === "VALIDA";
  }
}