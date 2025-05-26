import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { DataPackage } from "../data-package";

@Injectable({
  providedIn: "root",
})
export class ArticuloLicenciaService {
  private url = "rest/articulos-licencias";
  constructor(private http: HttpClient) {}

  findAll(): Observable<DataPackage> {
    return this.http.get<DataPackage>(this.url);
  }

  findByCodigo(codigo: string): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.url}/codigo/${codigo}`);
  }
}
