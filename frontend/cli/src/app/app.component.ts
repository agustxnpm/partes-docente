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
  templateUrl: './app.component.html',
  styles: [``],
})
export class AppComponent {}
