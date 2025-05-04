import { Cargo } from '../cargos/cargo';
import { Persona } from '../personas/persona';

export interface Designacion {
    id: number;
    situacionRevista: string;
    fechaInicio: string; 
    fechaFin?: string | null;
    cargo: Cargo;
    persona: Persona;
  }