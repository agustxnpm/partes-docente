import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DataPackage } from '../data-package';
import { Observable } from 'rxjs';
import { Division } from './division';

@Injectable({
  providedIn: 'root'
})
export class DivisionService {

  private url = 'rest/divisiones';
  constructor(private http: HttpClient) { }

  findAll(): Observable<DataPackage> {
    return this.http.get<DataPackage>(this.url);
  }

  createDivision(division: Division): Observable<DataPackage> {
    return this.http.post<DataPackage>(this.url, division);
  }

  findById(id: number): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.url}/${id}`);
  }

  updateDivision(division: Division): Observable<DataPackage> {
    return this.http.put<DataPackage>(`${this.url}/${division.id}`, division);
  }

  delete(division: Division): Observable<DataPackage> {
    return this.http.delete<DataPackage>(`${this.url}/${division.id}`);
  }

  byPage(page: number, size: number): Observable<DataPackage> {
    return this.http.get<DataPackage>(
      `${this.url}/page?page=${page - 1}&size=${size}`
    );
  }
}
