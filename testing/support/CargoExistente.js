const request = require("sync-request");

class CargoClient {
  static getAll() {
    try {
      const res = request("GET", "http://backend-pd:8080/cargos");
      return JSON.parse(res.getBody("utf8")).data;
    } catch (error) {
      console.error("Error al obtener cargos del backend:", error.message);
      throw error;
    }
  }

  static findByNombreYTipo(nombre, tipo, anio, numDivision, turno) {
    const cargos = this.getAll();
    const cargo = cargos.find(
      (c) => c.nombre === nombre && c.tipoDesignacion === tipo &&
        c.anio === anio &&
        c.numDivision === numDivision &&
        c.turno === turno
    );
    if (!cargo) throw new Error(`Cargo "${nombre}" con tipo "${tipo}" no encontrado.`);
    return cargo;
  }
}

module.exports = CargoClient;
