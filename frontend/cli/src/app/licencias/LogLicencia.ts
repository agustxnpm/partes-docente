export interface LogLicencia {
  id: number;
  fechaLog: string; // ISO 8601 string para LocalDateTime desde el backend
  mensaje: string;
  licencia: {
    id: number;
  };
}
