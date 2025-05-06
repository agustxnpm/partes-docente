package unpsjb.labprog.backend.business;

import org.springframework.stereotype.Component;

import unpsjb.labprog.backend.model.Division;

@Component
public class DivisionValidator {
    
    public void validarDivision(Division division) {
        
        if (division.getAnio() <= 0)
            throw new IllegalArgumentException("El año debe ser un número positivo");

        if (division.getNumDivision() <= 0)
            throw new IllegalArgumentException("El número de división debe ser un número positivo");

        if (division.getTurno() == null || division.getTurno().isEmpty())
            throw new IllegalArgumentException("El turno no puede estar vacío");
    }
}
