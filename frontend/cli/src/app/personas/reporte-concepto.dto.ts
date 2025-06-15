export interface LicenciaReporteDTO {
  ArticuloLicencia: string;
  Descripcion: string;
  FechaDesde: string;
  FechaHasta: string;
  DiasTomados: number;
  Estado: string;
}

export interface PersonaConceptoDTO {
  DNI: number;
  Nombre: string;
  Apellido: string;
  CUIL: string;
  Titulo: string;
  TotalDiasLicencia: number;
  Licencias: LicenciaReporteDTO[];
}

export interface ReporteConceptoDTO {
  Anio: number;
  FechaGeneracion: string;
  TotalPersonas: number;
  TotalLicencias: number;
  TotalDiasLicencia: number;
  Personas: PersonaConceptoDTO[];
}
