const { Given, When, Then } = require("@cucumber/cucumber");
const PersonaExistente = require("../../../support/PersonaExistente");
const HttpRequestPost = require("../../../support/HttpRequestPost");
const ResponseValidator = require("../../../support/ResponseValidator");

// Paso para definir el docente que solicita licencia
Given(
  "el docente con DNI {int}, nombre {string} y apellido {string}",
  function (dni, nombre, apellido) {
    // Buscar si la persona ya existe o crearla en el contexto
    const persona = PersonaExistente.findByDni(dni);
    if (!persona) {
      throw new Error(`Persona con DNI ${dni} no encontrada.`);
    }
    this.currentPersona = persona;
  }
);

// Paso para la solicitud de licencia
When(
  "solicita una licencia artículo {string} con descripción {string} para el período {string} {string}",
  function (articulo, descripcion, fechaDesde, fechaHasta) {
    // Configurar la solicitud de licencia en el contexto
    this.currentLicencia = {
      persona: this.currentPersona,
      articuloLicencia: {
        articulo: articulo,
        descripcion: descripcion,
      },
      pedidoDesde: fechaDesde,
      pedidoHasta: fechaHasta,
      certificadoMedico: articulo.startsWith("5"), // Asumimos certificado médico para artículos de enfermedad
    };

    // Si hay designaciones en el contexto, las asociamos a la licencia
    if (this.currentDesignations) {
      this.currentLicencia.designaciones = this.currentDesignations;
    }

    // Realizar la solicitud HTTP para otorgar la licencia
    const endpoint = "licencias";
    this.apiResponse = HttpRequestPost.post(endpoint, this.currentLicencia);
  }
);

// Pasos para escenarios con designaciones existentes
Given("que existe la persona", function (dataTable) {
  const personaData = dataTable.hashes()[0];
  this.currentPersona = {
    dni: parseInt(personaData.DNI),
    nombre: personaData.Nombre,
    apellido: personaData.Apellido,
  };
});

Given(
  "que existen las siguientes instancias de designación asignada",
  function (dataTable) {
    const designacionData = dataTable.hashes()[0];
    this.currentDesignationType = {
      tipoDesignacion: designacionData.TipoDesignacion,
      nombre: designacionData.NombreTipoDesignacion,
      cargaHoraria: parseInt(designacionData.CargaHoraria),
    };
  }
);

Given(
  "que la instancia de designación está asignada a la persona",
  function (dataTable) {
    const designacionPersonaData = dataTable.hashes()[0];

    this.currentDesignation = {
      persona: {
        dni: parseInt(designacionPersonaData.DNI),
        nombre: designacionPersonaData.Nombre,
        apellido: designacionPersonaData.Apellido,
      },
      cargo: this.currentDesignationType,
      fechaInicio: designacionPersonaData.Desde,
      fechaFin: designacionPersonaData.Hasta,
    };

    // Agregar a la lista de designaciones
    this.currentDesignations = this.currentDesignations || [];
    this.currentDesignations.push(this.currentDesignation);
  }
);

Given(
  "que la instancia de designación está asignada a la persona con licencia {string} comprendida en el período desde {string} hasta {string}",
  function (articulo, fechaDesde, fechaHasta, dataTable) {
    const designacionPersonaData = dataTable.hashes()[0];

    // Crear la designación sin licencia
    const designacion = {
      persona: {
        dni: parseInt(designacionPersonaData.DNI),
        nombre: designacionPersonaData.Nombre,
        apellido: designacionPersonaData.Apellido,
      },
      cargo: this.currentDesignationType,
      fechaInicio: designacionPersonaData.Desde,
      fechaFin: designacionPersonaData.Hasta,
    };

    // Agregar a la lista de designaciones
    this.currentDesignations = this.currentDesignations || [];
    this.currentDesignations.push(designacion);

    // Crear la licencia y asociarla con la designación
    this.currentLicencia = {
      articuloLicencia: {
        articulo: articulo,
      },
      pedidoDesde: fechaDesde,
      pedidoHasta: fechaHasta,
      persona: designacion.persona,
      designaciones: [designacion],
    };
  }
);

When(
  "se solicita el servicio de designación de la persona al cargo en el período comprendido desde {string} hasta {string}",
  function (fechaDesde, fechaHasta) {
    // Crear la designación con los datos actuales
    const designacion = {
      persona: this.currentPersona,
      cargo: this.currentDesignationType,
      fechaInicio: fechaDesde,
      fechaFin: fechaHasta,
    };

    // Realizar la solicitud HTTP para la designación
    const endpoint = "designaciones";
    this.apiResponse = HttpRequestPost.post(endpoint, designacion);
  }
);

Then("se recupera el mensaje", function (docString) {
  // Validar la respuesta con el mensaje esperado en formato JSON
  const expectedResponse = JSON.parse(docString);
  ResponseValidator.validateResponse(
    this.apiResponse,
    expectedResponse.status,
    expectedResponse.message
  );
});
