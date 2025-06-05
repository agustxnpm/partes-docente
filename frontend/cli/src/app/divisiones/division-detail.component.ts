import { CommonModule } from "@angular/common";
import { ChangeDetectorRef, Component } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { Division } from "./division";
import { DivisionService } from "./division.service";
import { ActivatedRoute, Router } from "@angular/router";
import { Turno } from "./turno";

@Component({
  selector: "app-division-detail",
  imports: [CommonModule, FormsModule],
  templateUrl: "./division-detail.component.html",
  styleUrls: ["../global-styles/detail-styles.css"],
  standalone: true,
})
export class DivisionDetailComponent {
  division: Division = {
    id: 0,
    anio: null,
    numDivision: null,
    orientacion: "",
    turno: null, // Valor por defecto
  };

  turnos = Object.values(Turno);

  mensaje: string = "";
  isNew: boolean = true;
  isError: boolean = false;
  showErrorNumeroAnio: boolean = false;
  showErrorNumeroDivision: boolean = false;

  constructor(
    private divisionService: DivisionService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.getDivision();
  }

  getDivision(): void {
    this.route.paramMap.subscribe((params) => {
      const id = params.get("id");

      this.resetForm();

      if (id === "new") {
        this.isNew = true;
      } else {
        this.isNew = false;
        this.divisionService.findById(Number(id)).subscribe({
          next: (response) => {
            this.division = response.data as Division;
          },
          error: (err) => {
            console.error("Error al obtener la división:", err);
            this.mensaje = "Error al cargar la división.";
          },
        });
      }
    });
  }

  saveDivision(): void {
    if (this.isNew) {
      this.divisionService.createDivision(this.division).subscribe({
        next: (response) => {
          this.mensaje = response.message;
          this.isError = response.status !== 200;
          this.cdr.detectChanges(); // forzar deteccion de cambios
          this.scrollToMessage(); // desplazar la vista al mensaje
        },
      });
    } else {
      this.divisionService.updateDivision(this.division).subscribe({
        next: (response) => {
          this.mensaje = response.message;
          this.isError = response.status !== 200;
          this.cdr.detectChanges(); // forzar deteccion de cambios
          this.scrollToMessage(); // desplazar la vista al mensaje
        },
      });
    }
  }

  onlyNumbers(event: KeyboardEvent): boolean {
    const charCode = event.which ? event.which : event.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
      // Identificar qué campo disparó el evento
      if ((event.target as HTMLInputElement).name === "anio") {
        this.showErrorNumeroAnio = true;
        setTimeout(() => (this.showErrorNumeroAnio = false), 5000);
      } else if ((event.target as HTMLInputElement).name === "numDivision") {
        this.showErrorNumeroDivision = true;
        setTimeout(() => (this.showErrorNumeroDivision = false), 5000);
      }
      return false;
    }
    return true;
  }

  volver(): void {
    this.router.navigate(["/divisiones"]);
  }

  private resetForm(): void {
    this.division = {
      id: 0,
      anio: null,
      numDivision: null,
      orientacion: "",
      turno: null,
    };
    this.mensaje = "";
    this.isError = false;
    this.showErrorNumeroAnio = false;
    this.showErrorNumeroDivision = false;
  }

  scrollToMessage(): void {
    const messageElement = document.getElementById("message-container");
    if (messageElement) {
      messageElement.scrollIntoView({ behavior: "smooth" });
    }
  }
}
