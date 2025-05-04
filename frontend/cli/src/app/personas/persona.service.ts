import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Persona } from "./persona";
import { DataPackage } from '../data-package';

@Injectable({
  providedIn: 'root'
})
export class PersonaService {

  private url = 'rest/personas';
  constructor(private http: HttpClient) { }

  findAll(): Observable<DataPackage> {
    return this.http.get<DataPackage>(this.url);
  }

  createPersona(persona: Persona): Observable<DataPackage> {
    return this.http.post<DataPackage>(this.url, persona);
  }

  findById(id: number): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.url}/${id}`);
  }

  updatePersona(persona: Persona): Observable<DataPackage> {
    return this.http.put<DataPackage>(`${this.url}/${persona.id}`, persona);
  }

  delete(persona: Persona): Observable<DataPackage> {
    return this.http.delete<DataPackage>(`${this.url}/${persona.id}`);
  }

  byPage(page: number, size: number): Observable<DataPackage> {
    return this.http.get<DataPackage>(
      `${this.url}/page?page=${page - 1}&size=${size}`
    );
  }
}
