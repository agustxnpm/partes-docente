import { Routes } from "@angular/router";
import { PersonaComponent } from "./personas/persona.component";
import { PersonasDetailComponent } from "./personas/personas-detail.component";
import { HomeComponent } from "./home/home.component";
import { DivisionDetailComponent } from "./divisiones/division-detail.component";
import { DivisionComponent } from "./divisiones/division.component";
import { CargoComponent } from "./cargos/cargo.component";
import { CargoDetailComponent } from "./cargos/cargo-detail.component";

export const routes: Routes = [
  { path: "personas", component: PersonaComponent }, // Ruta para listar personas
  { path: "personas/:id", component: PersonasDetailComponent }, // Ruta para crear o editar persona
  { path: "home", component: HomeComponent },
  { path: "divisiones", component: DivisionComponent },
  { path: "divisiones/:id", component: DivisionDetailComponent },
  { path: "cargos", component: CargoComponent },
  { path: "cargos/:id", component: CargoDetailComponent },
  { path: "", redirectTo: "/home", pathMatch: "full" }, // Redirigir a la ruta de inicio
];
