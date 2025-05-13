const request = require("sync-request");

class PersonaExistente {
  static getAll() {
    try {
      const res = request("GET", "http://backend-pd:8080/personas");
      return JSON.parse(res.getBody("utf8")).data;
    } catch (error) {
      console.error("Error al obtener cargos del backend:", error.message);
      throw error;
    }
  }

  static findByDni(dni) {
    const personas = this.getAll();
    const persona = personas.find(
      (p) => p.dni === dni
    );
    //if (!persona) throw new Error(`Persona con DNI "${dni}" no encontrada.`);
    return persona;
  }
}

module.exports = PersonaExistente;
