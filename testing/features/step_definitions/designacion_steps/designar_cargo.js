const { Given } = require("@cucumber/cucumber");
const CargoExistente = require("../../../support/CargoExistente");
const PersonaExistente = require("../../../support/PersonaExistente");
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
    const persona = PersonaExistente.findByDni(dni);
    
    if (!persona) {
      throw new Error(`No se encontró la persona con DNI ${dni}`);
    }
    
    if (persona.nombre !== nombre || persona.apellido !== apellido) {
      throw new Error(
        `Los datos de la persona con DNI ${dni} no coinciden.\n` +
        `Esperado: ${nombre} ${apellido}\n` +
        `Encontrado: ${persona.nombre} ${persona.apellido}`
      );
    }
    
    this.currentPerson = persona;
  }
);

//Y que se asigna al cargo  con tipo de designación <tipo> y <nombreDesignación>
Given(
  "que se asigna al cargo con tipo de designación {string} y {string}",
  function (tipo, nombreDesignacion) {
    // Buscar el cargo existente en lugar de crear uno nuevo
    const cargo = CargoExistente.findByNombreYTipo(nombreDesignacion, tipo);
    
    if (!cargo) {
      throw new Error(`No se encontró el cargo ${nombreDesignacion} de tipo ${tipo}`);
    }
    
    this.currentCargo = cargo;
  }
);

// Y si es espacio curricular asignada a la división <año> <número> <turno>
Given(
  "si es espacio curricular asignada a la división {int} {int} {string}",
  function (anio, numDivision, turno) {
    if (this.currentCargo?.tipoDesignacion === "ESPACIO_CURRICULAR") {
      const division = {
        anio: anio,
        numDivision: numDivision,
        turno: turno,
      };

      this.currentDivision = division;

      // Asignar la división al cargo
      this.currentCargo.division = division;
    } else {
      this.currentDivision = null;
    }
  }
);




//   Y se designa por el período <fechadesde> <fechaHasta>
Given(
  "se designa por el período {string} {string}",
  function (fechaDesde, fechaHasta) {
    // Configurar currentDesignation en el contexto
    this.currentDesignation = {
      persona: this.currentPerson,
      cargo: this.currentCargo,
      situacionRevista: "Titular",
      fechaInicio: fechaDesde,
      fechaFin: fechaHasta || null,
    };
  }
);
