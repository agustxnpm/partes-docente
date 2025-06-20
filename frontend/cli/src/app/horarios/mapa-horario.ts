export interface HorarioMapa {
  dia: string;
  hora: number;
  espacioCurricular: string;
  docente: string;
  suplente?: string;
  esSuplencia: boolean;
  horaLibre: boolean;
  divisionId: number;
  turno: string;
}

export interface MapaHorarioSemanal {
  fechaInicio: string; // ISO date string
  fechaFin: string;    // ISO date string
  turno: string;
  anio: number;
  numDivision: number;
  horarios: HorarioMapa[];
}
