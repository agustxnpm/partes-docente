const { Given } = require("@cucumber/cucumber");
/* 
Característica: designar una persona a un cargo docente
actividad central de información de la escuela secundaria */

/* Esquema del escenario: Designación de persona en cargos NO cubiertos
 aún en el perído indicado de forma existosa
 */

//Dada la persona con <DNI> <nombre> y <apellido>
Given(
  "la persona con {int} {string} y {string}",
  function (dni, nombre, apellido) {
    this.currentPerson = {
      dni: dni,
      nombre: nombre,
      apellido: apellido,
    };
  }
);

//Y que se asigna al cargo  con tipo de designación <tipo> y <nombreDesignación>
Given(
  "que se asigna al cargo con tipo de designación {string} y {string}",
  function (tipo, nombreDesignacion) {
    this.currentCargo = {
      tipo: tipo,
      nombreDesignacion: nombreDesignacion,
    };
  }
);

//Y si es espacio curricular asignada a la división <año> <número> <turno>
Given(
  "si es espacio curricular asignada a la división {int} {int} {string}",
  function (anio, numDivision, turno) {
    this.currentDivision = {
      anio: anio,
      numDivision: numDivision,
      turno: turno,
    };
  }
);

//   Y se designa por el período <fechadesde> <fechaHasta>
Given(
  "se designa por el período {string} {string}",
  function (fechaDesde, fechaHasta) {
    this.currentPeriod = {
      fechaDesde: fechaDesde,
      fechaHasta: fechaHasta,
    };
  }
);
