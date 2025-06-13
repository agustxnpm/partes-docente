import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { DataPackage } from "../data-package";
import { Observable } from "rxjs";
import { Licencia } from "./licencia";

@Injectable({
  providedIn: "root",
})
export class LicenciaService {
  private url = "rest/licencias";
  constructor(private http: HttpClient) {}

  createLicencia(licencia: Licencia): Observable<DataPackage> {
    return this.http.post<DataPackage>(this.url, licencia);
  }

  updateLicencia(licencia: Licencia): Observable<DataPackage> {
    return this.http.put<DataPackage>(`${this.url}/${licencia.id}`, licencia);
  }

  findAll(): Observable<DataPackage> {
    return this.http.get<DataPackage>(this.url);
  }

  /**
   * Busca licencias por criterios específicos.
   * Corresponde a: GET /licencias/buscar
   * @param personaDni DNI de la persona
   * @param articulo Código del artículo de la licencia
   * @param desde Fecha de inicio del período de búsqueda (formato YYYY-MM-DD)
   * @param hasta Fecha de fin del período de búsqueda (formato YYYY-MM-DD)
   */
  buscarLicencias(
    personaDni: string | number,
    articulo: string,
    desde: string,
    hasta: string
  ): Observable<DataPackage> {
    const url = `${this.url}/buscar`;

    let params = new HttpParams();
    params = params.append("personaDni", personaDni.toString());
    params = params.append("articulo", articulo);
    params = params.append("desde", desde);
    params = params.append("hasta", hasta);
    return this.http.get<DataPackage>(url, { params });
  }

  byPage(page: number, size: number): Observable<DataPackage> {
    return this.http.get<DataPackage>(
      `${this.url}/page?page=${page - 1}&size=${size}`
    );
  }

  findById(id: number): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.url}/${id}`);
  }

  getLogsByLicenciaId(licenciaId: number): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.url}/${licenciaId}/logs`);
  }

  /**
   * Obtiene el parte diario de licencias para una fecha específica.
   * Corresponde a: GET /licencias/parte-diario/{fecha}
   * @param fecha Fecha en formato YYYY-MM-DD
   */
  obtenerParteDiario(fecha: string): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.url}/parte-diario/${fecha}`);
  }
}
