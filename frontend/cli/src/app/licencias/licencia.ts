import { Persona } from "../personas/persona";
import { Designacion } from "../designaciones/designacion";
import { ArticuloLicencia } from "../articulo-licencia/articulo-licencia";

export interface Licencia {
  id: number;
  pedidoDesde: string;
  pedidoHasta: string;
  domicilio?: string;
  certificadoMedico: boolean;
  articuloLicencia: ArticuloLicencia;
  persona: Persona;
  designaciones?: Designacion[];
  estado: "VALIDA" | "INVALIDA";
}
