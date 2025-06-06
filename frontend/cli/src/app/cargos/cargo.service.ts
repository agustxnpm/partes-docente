import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { DataPackage } from "../data-package";
import { Observable } from "rxjs";
import { Cargo } from "./cargo";

@Injectable({
  providedIn: "root",
})
export class CargoService {
  private url = "rest/cargos";
  constructor(private http: HttpClient) {}

  findAll(): Observable<DataPackage> {
    return this.http.get<DataPackage>(this.url);
  }

  createCargo(cargo: Cargo): Observable<DataPackage> {
    return this.http.post<DataPackage>(this.url, cargo);
  }

  findById(id: number): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.url}/${id}`);
  }

  updateCargo(cargo: Cargo): Observable<DataPackage> {
    return this.http.put<DataPackage>(`${this.url}/${cargo.id}`, cargo);
  }

  delete(cargo: Cargo): Observable<DataPackage> {
    return this.http.delete<DataPackage>(`${this.url}/${cargo.id}`);
  }

  byPage(page: number, size: number): Observable<DataPackage> {
    return this.http.get<DataPackage>(
      `${this.url}/page?page=${page - 1}&size=${size}`
    );
  }

  search(term: string): Observable<DataPackage> {
    const params = new HttpParams().set('term', term);
    return this.http.get<DataPackage>(`${this.url}/search`, { params });
  }
}
