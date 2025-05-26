const { Given } = require("@cucumber/cucumber");

// Paso específico: Dada el espacio físico división con <año> <número> <orientación> <turno>
Given(
  "el espacio físico división con {int} {int} {string} {string}",
  function (anio, numDivision, orientacion, turno) {
    this.currentDivision = {
      anio: anio,
      numDivision: numDivision,
      orientacion: orientacion,
      turno: turno,
    };
  }
);

// pasos when y then en common_steps.js
