import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { DataPackage } from "../data-package";
import { Observable } from "rxjs";
import { Designacion } from "./designacion";

@Injectable({
  providedIn: "root",
})
export class DesignacionesService {
  private url = "rest/designaciones";
  constructor(private http: HttpClient) {}

  findAll(): Observable<DataPackage> {
    return this.http.get<DataPackage>(this.url);
  }

  createDesignacion(designacion: Designacion): Observable<DataPackage> {
    return this.http.post<DataPackage>(this.url, designacion);
  }

  findById(id: number): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.url}/${id}`);
  }

  updateDesignacion(designacion: Designacion): Observable<DataPackage> {
    return this.http.put<DataPackage>(
      `${this.url}/${designacion.id}`,
      designacion
    );
  }

  delete(designacion: Designacion): Observable<DataPackage> {
    return this.http.delete<DataPackage>(`${this.url}/${designacion.id}`);
  }

  byPage(page: number, size: number): Observable<DataPackage> {
    return this.http.get<DataPackage>(
      `${this.url}/page?page=${page - 1}&size=${size}`
    );
  }
}
