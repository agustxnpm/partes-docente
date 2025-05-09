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
  sections = [
    {
      title: 'Gestión de Personas',
      icon: 'bi-people-fill',
      listLink: '/personas',
      newLink: '/personas/new'
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
}
