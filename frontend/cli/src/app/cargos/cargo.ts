import { Division } from '../divisiones/division';
import { Horario } from '../horarios/horario';
import { TipoDesignacion } from './tipoDesignacion';

export interface Cargo {
    id: number; // Corresponde a "long" en Java
    nombre: string; // Corresponde a "String" en Java
    cargaHoraria: number | null; // Corresponde a "int" en Java
    fechaInicio: string; 
    fechaFin?: string | null; // Opcional, puede ser null
    division?: Division | null; // Relación ManyToOne con Division, puede ser null
    horario: Horario[]; // Relación OneToMany con Horario
    tipoDesignacion: TipoDesignacion | null; // Enum TipoDesignacion
  }