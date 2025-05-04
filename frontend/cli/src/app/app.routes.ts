import { Routes } from "@angular/router";
import { PersonaComponent } from "./personas/persona.component";
import { PersonasDetailComponent } from "./personas/personas-detail.component";
import { HomeComponent } from "./home/home.component";

export const routes: Routes = [
  { path: "personas", component: PersonaComponent }, // Ruta para listar personas
  { path: "personas/:id", component: PersonasDetailComponent }, // Ruta para crear o editar persona
  { path: "home", component: HomeComponent },
  { path: "", redirectTo: "/home", pathMatch: "full" }, // Redirigir a la ruta de inicio
];