const request = require("sync-request");

class CargoExistente {
  static getAll() {
    try {
      const res = request("GET", "http://backend-pd:8080/cargos");
      return JSON.parse(res.getBody("utf8")).data;
    } catch (error) {
      console.error("Error al obtener cargos del backend:", error.message);
      throw error;
    }
  }

  static findByNombreYTipo(nombre, tipo) {
    const cargos = this.getAll();
    return cargos.find(
      (c) => c.nombre === nombre && c.tipoDesignacion === tipo
    );
  }
}

module.exports = CargoExistente;