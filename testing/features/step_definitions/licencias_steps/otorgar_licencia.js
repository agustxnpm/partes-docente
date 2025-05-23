const { Given, When, Then } = require("@cucumber/cucumber");
const PersonaExistente = require("../../../support/PersonaExistente");
const CargoExistente = require("../../../support/CargoExistente");
const HttpRequestPost = require("../../../support/HttpRequestPost");
const ResponseValidator = require("../../../support/ResponseValidator");
const HttpRequestGet = require("../../../support/HttpRequestGet");


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

    const articuloResponse = HttpRequestGet.get(`articulos-licencias/codigo/${articulo}`);
    
    if (!articuloResponse || articuloResponse.status != 200 || !articuloResponse.data) {
      console.warn(`No se encontró el artículo ${articulo} en la base de datos.`);
      console.warn(`Respuesta: ${JSON.stringify(articuloResponse)}`);
      throw new Error(`No se pudo obtener el artículo de licencia con código ${articulo}`);
    }
    
    // Configurar la solicitud con el artículo real de la base de datos
    this.currentLicencia = {
      persona: this.currentPersona,
      articuloLicencia: articuloResponse.data,
      pedidoDesde: fechaDesde,
      pedidoHasta: fechaHasta,
      certificadoMedico: articulo.startsWith("5") // Asumimos certificado médico para artículos de enfermedad
    };

    // Si hay designaciones en el contexto, las asociamos a la licencia
    if (this.currentDesignations) {
      this.currentLicencia.designaciones = this.currentDesignations;
    }

    // Realizar la solicitud HTTP para otorgar la licencia
    const endpoint = "licencias";
    console.log("Licencia a otorgar:", this.currentLicencia);
    this.apiResponse = HttpRequestPost.post(endpoint, this.currentLicencia);
    console.log("Respuesta de la API:", this.apiResponse);
  }
);

// Pasos para escenarios con designaciones existentes
Given("que existe la persona", function (dataTable) {
  const personaData = dataTable.hashes()[0];
  const personaEncontrada = PersonaExistente.findByDni(parseInt(personaData.DNI));
  if (!personaEncontrada) {
    throw new Error(`La persona con DNI ${personaData.DNI} definida en el step "que existe la persona" no fue encontrada en la base de datos.`);
  }
  this.currentPersona = personaEncontrada;
});

Given(
  "que existen las siguientes instancias de designación asignada",
  function (dataTable) {
    const designacionData = dataTable.hashes()[0];
    // Buscar el cargo existente en la base de datos
    const cargoEncontrado = CargoExistente.findByNombreYTipo(
      designacionData.NombreTipoDesignacion,
      designacionData.TipoDesignacion
    );

    if (!cargoEncontrado) {
      throw new Error(
        `El cargo "${designacionData.NombreTipoDesignacion}" de tipo "${designacionData.TipoDesignacion}" no fue encontrado en la base de datos.`
      );
    }

    // Este será el cargo al que se designará la nueva persona (el suplente).
    this.currentCargoParaDesignar = cargoEncontrado;
  }
);

Given(
  "que la instancia de designación está asignada a la persona",
  function (dataTable) {
    const designacionPersonaData = dataTable.hashes()[0];

    // Esta es la persona que TIENE la designación original (la que toma licencia)
    const personaConDesignacionOriginal = PersonaExistente.findByDni(parseInt(designacionPersonaData.DNI));
    if(!personaConDesignacionOriginal) {
        throw new Error(`La persona con DNI ${designacionPersonaData.DNI} (designación original) no fue encontrada.`);
    }

    this.currentDesignation = {
      id: null,
      persona: personaConDesignacionOriginal,
      cargo: this.currentCargoParaDesignar, // El mismo cargo que se usará para el suplente
      fechaInicio: designacionPersonaData.Desde,
      fechaFin: designacionPersonaData.Hasta,
      situacionRevista: "Titular" 
    };

    this.currentDesignations = this.currentDesignations || [];
    this.currentDesignations.push(this.currentDesignation);
  }
);

Given(
  "que la instancia de designación está asignada a la persona con licencia {string} comprendida en el período desde {string} hasta {string}",
  function (articuloLicenciaSolicitado, fechaDesdeLicencia, fechaHastaLicencia) {

    // Asegurarse de que this.currentDesignation (establecida por un paso anterior) exista y tenga la persona
    if (!this.currentDesignation || !this.currentDesignation.persona) {
      throw new Error(
        "La información de la designación actual (this.currentDesignation) no está completa o no se ha definido en un paso anterior."
      );
    }

    const designacionExistente = this.currentDesignation;


    this.currentLicencia = {
      articuloLicencia: {
        articulo: articuloLicenciaSolicitado,
        // La descripción del artículo podría obtenerse de una fuente de datos o ser añadida
        // por el paso "When solicita una licencia..." si es necesario para el payload.
      },
      pedidoDesde: fechaDesdeLicencia,
      pedidoHasta: fechaHastaLicencia,
      persona: designacionExistente.persona, 
      designaciones: [designacionExistente], 
      certificadoMedico: articuloLicenciaSolicitado.startsWith("5"), // cconsiderando que el artículo 5 requiere certificado médico
    };

  }
);

When(
  "se solicita el servicio de designación de la persona al cargo en el período comprendido desde {string} hasta {string}",
  function (fechaDesde, fechaHasta) {
    // this.currentPersona es el suplente (Jorge Dismal / Analía Rojas)
    // this.currentCargoParaDesignar es el cargo existente (Preceptor/a / Auxiliar ADM)
    if (!this.currentPersona || !this.currentPersona.id) {
        throw new Error("La persona a designar (suplente) no está definida o no tiene ID.");
    }
    if (!this.currentCargoParaDesignar || !this.currentCargoParaDesignar.id) {
        throw new Error("El cargo para la designación no está definido o no tiene ID.");
    }

    const designacionPayload = {
      persona: this.currentPersona, // Enviar solo el ID de la persona suplente
      cargo: this.currentCargoParaDesignar, // Enviar solo el ID del cargo existente
      fechaInicio: fechaDesde,
      fechaFin: fechaHasta || null,
      situacionRevista: "Suplente", // O la situación de revista que corresponda
    };

    const endpoint = "designaciones";
    
    this.apiResponse = HttpRequestPost.post(endpoint, designacionPayload);
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
