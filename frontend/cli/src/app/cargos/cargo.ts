import { Division } from '../divisiones/division';
import { Horario } from '../horarios/horario';
import { TipoDesignacion } from './tipoDesignacion';

export interface Cargo {
    id: number; 
    nombre: string; 
    cargaHoraria: number | null; 
    fechaInicio: string; 
    fechaFin?: string | null; // Opcional, puede ser null
    division?: Division | null; // Relación ManyToOne con Division, puede ser null
    horario: Horario[]; // Relación OneToMany con Horario
    tipoDesignacion: TipoDesignacion | null; // Enum TipoDesignacion
  }