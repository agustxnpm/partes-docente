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
import { NgbTypeaheadModule } from "@ng-bootstrap/ng-bootstrap";
import {
  catchError,
  debounceTime,
  distinctUntilChanged,
  map,
  Observable,
  of,
  switchMap,
  tap,
} from "rxjs";

@Component({
  selector: "app-designaciones-detail",
  imports: [CommonModule, FormsModule, RouterModule, NgbTypeaheadModule],
  templateUrl: "./designaciones-detail.component.html",
  styleUrls: ["../global-styles/detail-styles.css"],
  standalone: true,
})
export class DesignacionesDetailComponent {
  designacion: any = {
    id: 0,
    persona: null,
    cargo: null,
    situacionRevista: null,
    fechaInicio: null,
    fechaFin: null,
  };

  personas: Persona[] = [];
  cargos: Cargo[] = [];
  situacionesRevista: string[] = ["TITULAR", "SUPLENTE"];

  mensaje: string = "";
  isNew: boolean = true;
  isError: boolean = false;

  // variables para los typeahead
  searchingPersona = false;
  searchFailedPersona = false;
  searchingCargo = false;
  searchFailedCargo = false;

  // propiedades para controlar el dropdown
  showPersonaDropdown = false;
  showCargoDropdown = false;
  personasFiltradas: Persona[] = [];
  cargosFiltrados: Cargo[] = [];

   personaInputValue: string = '';
  cargoInputValue: string = '';

  constructor(
    private designacionesService: DesignacionesService,
    private personaService: PersonaService,
    private cargoService: CargoService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.getDesignacion();
    this.cargarPersonasYCargos();
  }

  cargarPersonasYCargos(): void {
    this.personaService.findAll().subscribe({
      next: (response) => {
        this.personas = response.data as Persona[];
        this.personasFiltradas = [...this.personas]; // inicializar filtradas
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
        this.cargosFiltrados = [...this.cargos]; // inicializar filtradas
        this.isError = response.status !== 200;
      },
      error: (err) => {
        console.error("Error al cargar cargos:", err);
        this.mensaje = "Error al cargar la lista de cargos.";
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
    // Delay para permitir click en opciones
    setTimeout(() => {
      this.showPersonaDropdown = false;
    }, 150);
  }

    selectPersona(persona: Persona): void {
    this.designacion.persona = persona;
    this.personaInputValue = this.formatterPersona(persona);
    this.showPersonaDropdown = false;
  }

  selectCargo(cargo: Cargo): void {
    this.designacion.cargo = cargo;
    this.cargoInputValue = this.formatterCargo(cargo);
    this.showCargoDropdown = false;
  }

  onPersonaInput(event: any): void {
    const term = event.target.value.toLowerCase();
    this.personaInputValue = event.target.value;
    
    // Si se borra el contenido, limpiar la selección
    if (term.length === 0) {
      this.designacion.persona = null;
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



  // Métodos para manejar el dropdown de cargos
  onCargoFocus(): void {
    this.showCargoDropdown = true;
    this.cargosFiltrados = [...this.cargos];
  }

  onCargoBlur(): void {
    // Delay para permitir click en opciones
    setTimeout(() => {
      this.showCargoDropdown = false;
    }, 150);
  }

  onCargoInput(event: any): void {
    const term = event.target.value.toLowerCase();
    this.cargoInputValue = event.target.value;
    
    // Si se borra el contenido, limpiar la selección
    if (term.length === 0) {
      this.designacion.cargo = null;
      this.cargosFiltrados = [...this.cargos];
    } else {
      this.cargosFiltrados = this.cargos.filter(
        (cargo) =>
          cargo.nombre?.toLowerCase().includes(term) ||
          (cargo.division &&
            (cargo.division?.anio?.toString().includes(term) ||
              cargo.division?.numDivision?.toString().includes(term) ||
              cargo.division?.turno?.toLowerCase().includes(term)))
      );
    }
    this.showCargoDropdown = true;
  }

  // TypeAhead para Personas
  searchPersona = (text$: Observable<string>): Observable<Persona[]> =>
    text$.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      tap(() => (this.searchingPersona = true)),
      switchMap((term) => {
        return this.personaService.search(term.trim()).pipe(
          map((response: any) => {
            return response?.data || [];
          }),
          tap(() => (this.searchFailedPersona = false)),
          catchError((error) => {
            console.error("Error en búsqueda de personas:", error);
            this.searchFailedPersona = true;
            return of([]);
          })
        );
      }),
      tap(() => (this.searchingPersona = false))
    );

  // TypeAhead para Cargos
  searchCargo = (text$: Observable<string>): Observable<Cargo[]> =>
    text$.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      tap(() => (this.searchingCargo = true)),
      switchMap((term) => {
        return this.cargoService.search(term.trim()).pipe(
          map((response: any) => {
            return response?.data || [];
          }),
          tap(() => (this.searchFailedCargo = false)),
          catchError((error) => {
            console.error("Error en búsqueda de cargos:", error);
            this.searchFailedCargo = true;
            return of([]);
          })
        );
      }),
      tap(() => (this.searchingCargo = false))
    );

  // Formatters para el display
  formatterPersona = (persona: Persona) => {
    if (!persona) return "";
    return `${persona.apellido}, ${persona.nombre} (${persona.dni})`;
  };

  formatterCargo = (cargo: Cargo) => {
    if (!cargo) return "";
    if (cargo.division) {
      return `${cargo.nombre} - ${cargo.division.anio}° ${cargo.division.numDivision}º (${cargo.division.turno})`;
    }
    return `${cargo.nombre} (Cargo Institucional)`;
  };

  getDesignacion(): void {
    this.route.paramMap.subscribe((params) => {
      const id = params.get("id");

      this.resetForm();

      if (id === "new") {
        this.isNew = true;
        this.personaInputValue = '';
        this.cargoInputValue = '';
      } else {
        this.isNew = false;
        this.designacionesService.findById(Number(id)).subscribe({
          next: (response) => {
            this.designacion = response.data;
            
            // Inicializar los valores de input
            this.personaInputValue = this.designacion.persona ? this.formatterPersona(this.designacion.persona) : '';
            this.cargoInputValue = this.designacion.cargo ? this.formatterCargo(this.designacion.cargo) : '';
            
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

            this.cdr.detectChanges();
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

  comparePersonas(persona1: any, persona2: any): boolean {
    return persona1 && persona2
      ? persona1.id === persona2.id
      : persona1 === persona2;
  }

  compareCargos(cargo1: any, cargo2: any): boolean {
    return cargo1 && cargo2 ? cargo1.id === cargo2.id : cargo1 === cargo2;
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

private resetForm(): void {
    this.designacion = {
      id: 0,
      persona: null,
      cargo: null,
      situacionRevista: null,
      fechaInicio: null,
      fechaFin: null,
    };
    this.personaInputValue = '';
    this.cargoInputValue = '';
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
