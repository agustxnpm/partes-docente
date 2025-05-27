import { Pipe, PipeTransform } from "@angular/core";
import { TipoDesignacion } from "../../cargos/tipoDesignacion"; // Ajusta la ruta si es necesario

@Pipe({
  name: "tipoDesignacionFormat",
  standalone: true,
})
export class TipoDesignacionFormatPipe implements PipeTransform {
  transform(value: TipoDesignacion | string | undefined | null): string {
    if (!value) {
      return "";
    }
    switch (value) {
      case TipoDesignacion.ESPACIO_CURRICULAR:
      case "ESPACIO_CURRICULAR": // Para cubrir si el valor llega como string
        return "Espacio Curricular";
      case TipoDesignacion.CARGO:
      case "CARGO": // Para cubrir si el valor llega como string
        return "Cargo Institucional";
      default:
        return value.toString(); // Retornar el valor original si no coincide
    }
  }
}
