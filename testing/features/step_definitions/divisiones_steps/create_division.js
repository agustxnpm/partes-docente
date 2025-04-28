const { Given } = require("@cucumber/cucumber");

// Paso específico: Dada el espacio físico división con <año> <número> <orientación> <turno>
Given(
  "el espacio físico división con {int} {int} {string} {string}",
  function (anio, numero, orientacion, turno) {
    this.currentDivision = {
      anio: anio,
      numero: numero,
      orientacion: orientacion,
      turno: turno,
    };
  }
);
