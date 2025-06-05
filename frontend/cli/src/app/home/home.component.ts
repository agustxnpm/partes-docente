import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.css']
})
export class HomeComponent {
  // Información del sistema
  systemInfo = {
    school: 'Escuela Secundaria N° 775',
    location: 'Puerto Madryn, Chubut',
    ministry: 'Ministerio de Educación - Provincia del Chubut'
  };

  // Secciones principales (mantengo la estructura original por compatibilidad)
  sections = [
    {
      title: 'Gestión de Personas',
      icon: 'bi-people-fill',
      listLink: '/personas',
      newLink: '/personas/new'
    },
    {
      title: 'Gestión de Designaciones',
      icon: 'bi-person-badge-fill',
      listLink: '/designaciones',
      newLink: '/designaciones/new'
    },
    {
      title: 'Gestión de Licencias',
      icon: 'bi-journal-medical',
      listLink: '/licencias',
      newLink: '/licencias/new'
    },
    {
      title: 'Gestión de Divisiones',
      icon: 'bi-collection-fill',
      listLink: '/divisiones',
      newLink: '/divisiones/new'
    },
    {
      title: 'Gestión de Cargos',
      icon: 'bi-briefcase-fill',
      listLink: '/cargos',
      newLink: '/cargos/new'
    }
  ];

  // Accesos rápidos
  quickAccess = [
    {
      title: 'Nuevo Docente',
      icon: 'bi-person-plus-fill',
      link: '/personas/new',
      color: 'primary'
    },
    {
      title: 'Nueva Designación',
      icon: 'bi-person-badge',
      link: '/designaciones/new',
      color: 'success'
    },
    {
      title: 'Nueva Licencia',
      icon: 'bi-journal-plus',
      link: '/licencias/new',
      color: 'info'
    },
    {
      title: 'Nuevo Cargo',
      icon: 'bi-briefcase-fill',
      link: '/cargos/new',
      color: 'secondary'
    },
    {
      title: 'Nueva División',
      icon: 'bi-collection-fill',
      link: '/divisiones/new',
      color: 'secondary'
    }
  ];

  // Reportes disponibles (para futuras implementaciones)
  reports = [
    {
      title: 'Parte Diario de Novedades',
      description: 'Licencias y novedades del día',
      icon: 'bi-calendar-day',
      available: false
    },
    {
      title: 'Concepto Anual del Personal',
      description: 'Evaluación anual por docente',
      icon: 'bi-person-check',
      available: false
    },
    {
      title: 'Mapa de Horarios por División',
      description: 'Distribución semanal de clases',
      icon: 'bi-calendar-week',
      available: false
    },
    {
      title: 'Espacios Curriculares Descubiertos',
      description: 'Materias sin designación o con licencia',
      icon: 'bi-exclamation-triangle',
      available: false
    }
  ];
}