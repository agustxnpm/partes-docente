import { Designacion } from "../designaciones/designacion";

export interface Persona {
  id: number;
  dni: number | null;
  cuil: string;
  nombre: string;
  apellido: string;
  titulo?: string | null;
  sexo: string;
  domicilio: string;
  telefono: string;
  designaciones?: Designacion[];
}
