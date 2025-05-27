import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { PaginationComponent } from '../pagination/pagination.component';
import { ResultsPage } from '../results-page';
import { LicenciaService } from './licencia.service';

@Component({
  selector: 'app-licencia',
  imports: [RouterModule, CommonModule, PaginationComponent],
  templateUrl: './licencia.component.html',
  styleUrls: ["../global-styles/table-styles.css"],
})
export class LicenciaComponent {

   resultsPage: ResultsPage = <ResultsPage>{};
    currentPage: number = 1;
    mensaje: string = "";

    constructor(
        private licenciaService: LicenciaService,
      ) {}

ngOnInit(): void {
    this.getLicencias();
  }


 getLicencias(): void {
    this.licenciaService.byPage(this.currentPage, 7).subscribe((response) => {
      this.resultsPage = <ResultsPage>response.data;
    });
  }



onPageChangeRequested(page: number): void {
    this.currentPage = page;
    this.getLicencias();
  }

















}
