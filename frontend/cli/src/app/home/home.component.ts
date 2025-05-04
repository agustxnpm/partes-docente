import { Component } from '@angular/core';

@Component({
  selector: 'app-home',
  imports: [],
  template: `
    <div class="container mt-5">
  <div class="text-center">
    <h1 class="display-5 mb-4">
      <i class="bi bi-people-fill me-2"></i>
      Bienvenido al Sistema de Gestión de Personal
    </h1>
    <p class="lead">Desde aquí podés administrar la información de las personas registradas.</p>

    <div class="mt-4">
      <a routerLink="/personas" class="btn btn-primary btn-lg me-3">
        <i class="bi bi-card-list me-1"></i>
        Ver Personas
      </a>
      <a routerLink="/personas/new" class="btn btn-success btn-lg">
        <i class="bi bi-person-plus-fill me-1"></i>
        Nueva Persona
      </a>
    </div>
  </div>
</div>

  `,
  styles: ``
})
export class HomeComponent {

}
