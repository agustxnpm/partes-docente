package unpsjb.labprog.backend.model;

public enum Turno {
    Mañana,
    Tarde,
    Vespertino,
    Noche;

    public boolean isEmpty() {
        return this == null || this.name().isEmpty();
    }
}
