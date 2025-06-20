import { ChangeDetectorRef, Component, OnInit } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import { CargoService } from "./cargo.service";
import { DivisionService } from "../divisiones/division.service";
import { HorarioService } from "../horarios/horario.service";
import { Cargo } from "./cargo";
import { TipoDesignacion } from "./tipoDesignacion";
import { Division } from "../divisiones/division";
import { Horario } from "../horarios/horario";
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
  
  // Propiedades para manejo de horarios
  diaSeleccionado: string = "";
  horaSeleccionada: number | null = null;
  horariosDisponibles: Horario[] = [];
  horasUnicas: number[] = [];
  cargosEnDivision: Cargo[] = []; // Para validación de conflictos

  constructor(
    private cargoService: CargoService,
    private divisionService: DivisionService,
    private horarioService: HorarioService,
    private modalService: ModalService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.getCargo();
    this.getDivisiones();
    this.cargarHorariosDisponibles();
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
            // Asegurar que horario sea siempre un array
            if (!this.cargo.horario) {
              this.cargo.horario = [];
            }
            // Cargar cargos de la división si es un espacio curricular
            if (this.cargo.tipoDesignacion === 'ESPACIO_CURRICULAR' && this.cargo.division) {
              this.cargarCargosEnDivision();
            }
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
    // Si no es ESPACIO_CURRICULAR, limpiar la división y horarios
    if (this.cargo.tipoDesignacion !== TipoDesignacion.ESPACIO_CURRICULAR) {
      this.cargo.division = null;
      this.cargo.horario = []; // Limpiar horarios también
      this.cargosEnDivision = []; // Limpiar cargos en división
    } else {
      // Si es ESPACIO_CURRICULAR, asegurar que horario sea un array
      if (!this.cargo.horario) {
        this.cargo.horario = [];
      }
      // Cargar cargos de la división si ya hay una seleccionada
      if (this.cargo.division) {
        this.cargarCargosEnDivision();
      }
    }
  }

  /**
   * Maneja el cambio de división seleccionada
   */
  onDivisionChange(): void {
    // Cargar cargos de la nueva división para validación de conflictos
    if (this.cargo.tipoDesignacion === TipoDesignacion.ESPACIO_CURRICULAR) {
      this.cargarCargosEnDivision();
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
    
    // Limpiar campos de horarios
    this.diaSeleccionado = "";
    this.horaSeleccionada = null;
  }

  scrollToMessage(): void {
    const messageElement = document.getElementById("message-container");
    if (messageElement) {
      messageElement.scrollIntoView({ behavior: "smooth" });
    }
  }
  
  // Métodos para manejo de horarios
  
  cargarHorariosDisponibles(): void {
    this.horarioService.obtenerHorariosDisponibles().subscribe({
      next: (response) => {
        this.horariosDisponibles = response.data as Horario[];
        // Extraer las horas únicas disponibles
        this.horasUnicas = [...new Set(this.horariosDisponibles.map(h => h.hora))].sort((a, b) => a - b);
      },
      error: (err) => {
        console.error('Error al cargar horarios disponibles:', err);
        this.modalService.alert('Error', 'Error al cargar los horarios disponibles.');
      }
    });
  }
  
  /**
   * Carga los cargos existentes de la misma división para validación de conflictos
   */
  cargarCargosEnDivision(): void {
    if (!this.cargo.division) {
      this.cargosEnDivision = [];
      return;
    }
    
    this.cargoService.findAll().subscribe({
      next: (response) => {
        const todosCargos = response.data as Cargo[];
        // Filtrar solo los cargos de tipo ESPACIO_CURRICULAR de la misma división
        this.cargosEnDivision = todosCargos.filter(c => 
          c.tipoDesignacion === 'ESPACIO_CURRICULAR' &&
          c.division && 
          c.division.id === this.cargo.division?.id &&
          c.id !== this.cargo.id && // Excluir el cargo actual
          c.horario && 
          c.horario.length > 0
        );
      },
      error: (err) => {
        console.error('Error al cargar cargos de la división:', err);
        this.cargosEnDivision = [];
      }
    });
  }
  
  /**
   * Valida si existe conflicto de horarios con otros cargos de la misma división
   */
  validarConflictoHorario(dia: string, hora: number): { hayConflicto: boolean, cargoConflicto?: string } {
    for (const cargoExistente of this.cargosEnDivision) {
      if (cargoExistente.horario) {
        for (const horarioExistente of cargoExistente.horario) {
          if (horarioExistente.dia === dia && horarioExistente.hora === hora) {
            return {
              hayConflicto: true,
              cargoConflicto: cargoExistente.nombre
            };
          }
        }
      }
    }
    return { hayConflicto: false };
  }
  
  agregarHorario(): void {
    if (!this.diaSeleccionado || !this.horaSeleccionada) {
      return;
    }
    
    // Asegurar que horario sea un array
    if (!this.cargo.horario) {
      this.cargo.horario = [];
    }
    
    // Verificar si el horario ya existe
    const horarioExistente = this.cargo.horario.find(
      h => h.dia === this.diaSeleccionado && h.hora === this.horaSeleccionada
    );
    
    if (horarioExistente) {
      this.modalService.alert('Atención', 'Este horario ya está asignado al cargo.');
      return;
    }
    
    // Validar conflictos con otros cargos de la misma división (solo para espacios curriculares)
    if (this.cargo.tipoDesignacion === 'ESPACIO_CURRICULAR' && this.cargo.division) {
      const conflicto = this.validarConflictoHorario(this.diaSeleccionado, this.horaSeleccionada);
      if (conflicto.hayConflicto) {
        const division = this.cargo.division;
        const turno = division.turno ? division.turno.toLowerCase() : 'sin turno';
        this.modalService.alert(
          'Conflicto de Horarios', 
          `El ${this.diaSeleccionado.toLowerCase()} a la ${this.horaSeleccionada}º hora ya está ocupado por "${conflicto.cargoConflicto}" en la división ${division.anio}º${division.numDivision}º ${turno}.`
        );
        return;
      }
    }
    
    // Agregar el nuevo horario (el ID será generado por el backend)
    this.cargo.horario.push({
      id: 0, // Temporal, será generado por el backend
      dia: this.diaSeleccionado,
      hora: this.horaSeleccionada
    });
    
    // Limpiar selección
    this.diaSeleccionado = "";
    this.horaSeleccionada = null;
  }
  
  eliminarHorario(index: number): void {
    if (!this.cargo.horario) {
      this.cargo.horario = [];
      return;
    }
    this.cargo.horario.splice(index, 1);
  }
}
