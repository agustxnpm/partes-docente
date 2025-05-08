import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-home',
  imports: [RouterModule],
  template: `
    <div class="container mt-5">
      <div class="bg-light rounded shadow p-4 border border-2 border-primary-subtle">
        <div class="text-center mb-5">
          <div class="d-flex align-items-center justify-content-center mb-3">
            <i class="bi bi-house-door-fill text-primary fs-1"></i>
          </div>
          <h2 class="mb-3">Panel Principal</h2>
          <p class="text-muted">
            Bienvenido al sistema. Desde aquí podés navegar fácilmente hacia las secciones principales del gestor.
          </p>
        </div>

        <div class="row g-4 justify-content-center">
          <!-- Sección Personas -->
          <div class="col-md-6">
            <div class="card h-100 border-0 shadow-sm">
              <div class="card-body text-center">
                <i class="bi bi-people-fill text-primary fs-1 mb-3"></i>
                <h4 class="card-title mb-4">Gestión de Personas</h4>
                <div class="d-grid gap-2">
                  <a routerLink="/personas" class="btn btn-outline-primary">
                    <i class="bi bi-list-ul me-2"></i>
                    Listado de Personas
                  </a>
                  <a routerLink="/personas/new" class="btn btn-outline-success">
                    <i class="bi bi-person-plus-fill me-2"></i>
                    Nueva Persona
                  </a>
                </div>
              </div>
            </div>
          </div>

          <!-- Sección Divisiones -->
          <div class="col-md-6">
            <div class="card h-100 border-0 shadow-sm">
              <div class="card-body text-center">
                <i class="bi bi-collection-fill text-primary fs-1 mb-3"></i>
                <h4 class="card-title mb-4">Gestión de Divisiones</h4>
                <div class="d-grid gap-2">
                  <a routerLink="/divisiones" class="btn btn-outline-primary">
                    <i class="bi bi-list-ul me-2"></i>
                    Listado de Divisiones
                  </a>
                  <a routerLink="/divisiones/new" class="btn btn-outline-success">
                    <i class="bi bi-plus-circle me-2"></i>
                    Nueva División
                  </a>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .card {
      transition: transform 0.2s ease-in-out;
      background: rgba(255, 255, 255, 0.9);
    }

    .card:hover {
      transform: translateY(-5px);
    }

    .btn {
      padding: 0.75rem 1.5rem;
      font-weight: 500;
      text-transform: uppercase;
      letter-spacing: 0.5px;
    }

    .btn-outline-primary {
      border-width: 2px;
    }

    .btn-outline-success {
      border-width: 2px;
    }

    .bi {
      transition: transform 0.3s ease;
    }

    .card:hover .bi {
      transform: scale(1.1);
    }
  `]
})
export class HomeComponent {}