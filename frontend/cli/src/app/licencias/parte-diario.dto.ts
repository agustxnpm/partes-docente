export interface DocenteLicenciaDTO {
  DNI: number;
  Nombre: string;
  Apellido: string;
  Articulo: string;
  Descripcion: string;
  Desde: string;
  Hasta: string;
}

export interface ParteDiarioDTO {
  Fecha: string;
  Docentes: DocenteLicenciaDTO[];
}

export interface ParteDiarioResponse {
  ParteDiario: ParteDiarioDTO;
}
