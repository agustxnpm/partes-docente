import { Component } from "@angular/core";
import { RouterModule, RouterOutlet } from "@angular/router";
import { CommonModule } from "@angular/common";
import { NgbDropdownModule } from "@ng-bootstrap/ng-bootstrap"; // Añadir esta importación

@Component({
  selector: "app-root",
  standalone: true,
  imports: [
    RouterModule,
    RouterOutlet,
    CommonModule,
    NgbDropdownModule,
  ],
  template: `
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
      <div class="container-fluid">
        <a class="navbar-brand">Partes Docente</a>
        <button
          class="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarNav"
          aria-controls="navbarNav"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
          <ul class="navbar-nav">
            <li class="nav-item">
              <a class="nav-link" routerLink="/home" routerLinkActive="active">Inicio</a>
            </li>

            <li class="nav-item" ngbDropdown>
              <a
                class="nav-link dropdown-toggle"
                id="personaDropdown"
                role="button"
                ngbDropdownToggle
              >
                Persona
              </a>
              <ul ngbDropdownMenu aria-labelledby="personaDropdown">
                <li>
                  <a
                    ngbDropdownItem
                    routerLink="/personas"
                    [routerLinkActive]="['active']"
                    [routerLinkActiveOptions]="{ exact: true }"
                  >
                    <i class="bi bi-people-fill me-2"></i>Listado de Personas
                  </a>
                </li>
                <li><hr class="dropdown-divider" /></li>
                <li>
                  <a
                    ngbDropdownItem
                    routerLink="/personas/new"
                    [routerLinkActive]="['active']"
                    [routerLinkActiveOptions]="{ exact: true }"
                  >
                    <i class="bi bi-person-plus-fill me-2"></i>Nueva Persona
                  </a>
                </li>
              </ul>
            </li>
          </ul>
        </div>
      </div>
    </nav>

    <div class="container mt-4">
      <router-outlet></router-outlet>
    </div>
  `,
  styles: [``],
})
export class AppComponent {}
