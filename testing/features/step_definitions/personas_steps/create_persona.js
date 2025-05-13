const { Given } = require("@cucumber/cucumber");

// Paso específico: Dada la persona con <nombre> <apellido> <DNI> <CUIL> <sexo> <título> <domicilio> <teléfono>
Given(
  "la persona con {string} {word} {int} {word} {word} {string} {string} {string}",
  function (nombre, apellido, dni, cuil, sexo, titulo, domicilio, telefono) {
    this.currentPersona = {
      dni: dni,
      nombre: nombre,
      apellido: apellido,
      cuil: cuil,
      sexo: sexo,
      titulo: titulo === "" ? null : titulo,
      domicilio: domicilio,
      telefono: telefono,
    };
  } 
);
