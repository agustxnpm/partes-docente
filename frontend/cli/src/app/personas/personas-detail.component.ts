import { Component } from "@angular/core";
import { PersonaService } from "./persona.service";
import { Persona } from "./persona";
import { ActivatedRoute, Router } from "@angular/router";
import { FormsModule } from "@angular/forms";
import { CommonModule } from "@angular/common";

@Component({
  selector: "app-personas-create",
  imports: [CommonModule, FormsModule],
  templateUrl: './personas-detail.component.html',
  styleUrls: ['./personas-detail.component.css']

})
export class PersonasDetailComponent {
  persona: Persona = {
    id: 0,
    dni: 0,
    cuil: "",
    nombre: "",
    apellido: "",
    sexo: "M",
    titulo: null,
    domicilio: "",
    telefono: "",
    designaciones: [],
  };

  mensaje: string = ""; // Para mostrar el mensaje del backend
  isNew: boolean = true;

  cuilPrefix: string = ""; // Prefijo del CUIL
  cuilSuffix: string = ""; // Sufijo del CUIL
  cuilInvalid: boolean = false;

  constructor(
    private personaService: PersonaService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.getPersona();
  }

  getPersona(): void {
    this.route.paramMap.subscribe((params) => {
      const id = params.get('id');
      if (id === 'new') {
        // Persona nueva
        this.isNew = true;
        this.persona = {
          id: 0,
          dni: null,
          cuil: "",
          nombre: "",
          apellido: "",
          sexo: "M",
          titulo: null,
          domicilio: "",
          telefono: "",
          designaciones: [],
        };
        this.cuilPrefix = "";
        this.cuilSuffix = "";
      } else {
        // Editar persona existente
        this.isNew = false;
        this.personaService.findById(Number(id)).subscribe({
          next: (response) => {
            this.persona = response.data as Persona;
            const cuilParts = this.persona.cuil.split("-");
            this.cuilPrefix = cuilParts[0];
            this.cuilSuffix = cuilParts[2];
          },
          error: (err) => {
            console.error("Error al obtener la persona:", err);
            this.mensaje = "Error al cargar la persona.";
          },
        });
      }
    });
  }

  volver(): void {
    this.router.navigate(['/personas']);
  }

  isError: boolean = false;

  savePersona(): void {
    // Validar que el CUIL esté completo
    if (!this.cuilPrefix || !this.cuilSuffix || !this.persona.dni) {
      this.cuilInvalid = true;
      return;
    }

    // Construir el CUIL completo
    this.persona.cuil = `${this.cuilPrefix}-${this.persona.dni}-${this.cuilSuffix}`;
    this.cuilInvalid = false;

    if (this.isNew) {
      this.personaService.createPersona(this.persona).subscribe({
        next: (response) => {
          this.mensaje = response.message;
          this.isError = response.status !== 200;
        },
      });
    } else {
      this.personaService.updatePersona(this.persona).subscribe({
        next: (response) => {
          this.mensaje = response.message;
          this.isError = response.status !== 200;
        },
      });
    }
  }


  showErrorDni: boolean = false;
  showErrorCuil: boolean = false;

  onlyNumbers(event: KeyboardEvent): boolean {
    const charCode = (event.which) ? event.which : event.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
      // Identificar qué campo disparó el evento
      if ((event.target as HTMLInputElement).name === 'dni') {
        this.showErrorDni = true;
        setTimeout(() => this.showErrorDni = false, 5000); 
      } else if ((event.target as HTMLInputElement).name === 'cuil') {
        this.showErrorCuil = true;
        setTimeout(() => this.showErrorCuil = false, 5000); // 
      }
      return false;
    }
    return true;
  }
}
