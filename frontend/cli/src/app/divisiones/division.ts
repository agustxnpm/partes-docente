import { Turno } from '../divisiones/turno';

export interface Division {
  id: number; // Corresponde a "long" en Java
  anio: number; // Corresponde a "int" en Java
  numDivision: number; // Corresponde a "int" en Java
  orientacion: string; // Corresponde a "String" en Java
  turno: Turno; // Enum Turno
}