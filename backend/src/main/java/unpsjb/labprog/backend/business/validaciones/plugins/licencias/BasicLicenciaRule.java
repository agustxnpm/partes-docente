package unpsjb.labprog.backend.business.validaciones.plugins.licencias;


import unpsjb.labprog.backend.business.interfaces.validaciones.ILicenciaRule;
import unpsjb.labprog.backend.model.Licencia;
import unpsjb.labprog.backend.model.Persona;

/**
 * Regla de validación básica para licencias.
 * Verifica que los datos fundamentales de la licencia estén presentes y sean válidos.
 */
public class BasicLicenciaRule implements ILicenciaRule {
    private static BasicLicenciaRule instance;

    private BasicLicenciaRule() {}

    public static BasicLicenciaRule getInstance() {
        if (instance == null) {
            instance = new BasicLicenciaRule();
        }
        return instance;
    }

    @Override
    public void validate(Licencia licencia) {
        // Validaciones básicas y generales
        if (licencia == null) {
            throw new IllegalArgumentException("La licencia no puede ser nula.");
        }
        
        if (licencia.getPersona() == null) {
            throw new IllegalArgumentException("La persona de la licencia no puede ser nula.");
        }
        
        if (licencia.getArticuloLicencia() == null || licencia.getArticuloLicencia().getArticulo() == null
                || licencia.getArticuloLicencia().getArticulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El artículo de la licencia es inválido o no puede ser nulo.");
        }
        
        if (licencia.getPedidoDesde() == null || licencia.getPedidoHasta() == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin de la licencia son obligatorias.");
        }
        
        if (licencia.getPedidoDesde().isAfter(licencia.getPedidoHasta())) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }

        Persona persona = licencia.getPersona();
        
        if (persona == null) {
            throw new IllegalArgumentException("La persona de la licencia no puede ser nula");
        }
        
        if (persona.getNombre() == null || persona.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la persona no puede estar vacío");
        }
        
        if (persona.getApellido() == null || persona.getApellido().trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido de la persona no puede estar vacío");
        }
        
        if (persona.getDni() <= 0) {
            throw new IllegalArgumentException("El DNI de la persona debe ser válido");
        }
        
        // Verificar que tenga CUIL si es requerido
        if (persona.getCuil() == null || persona.getCuil().trim().isEmpty()) {
            throw new IllegalArgumentException("El CUIL de la persona no puede estar vacío");
        }
    }
    

    @Override
    public String getRuleName() {
        return "Validación Básica de Licencia";
    }
}
