const { Given, When, Then } = require("@cucumber/cucumber");
const PersonaExistente = require("../../support/PersonaExistente");
const HttpRequestPost = require("../../support/HttpRequestPost");
const HttpRequestGet = require("../../support/HttpRequestGet");
const assert = require("assert");

// Emitir el parte diario de licencias de una escuela para un determinado día

// Step para verificar la existencia de las licencias en el sistema
Given("la existencia de las siguientes licencias", function (dataTable) {
  this.licenciasExistentes = dataTable.hashes();
  
  // Verificar que existan las personas y licencias en el sistema
  this.licenciasExistentes.forEach(licencia => {
    // Verificar si la persona existe
    let persona = PersonaExistente.findByDni(parseInt(licencia.DNI));
    if (!persona) {
      throw new Error(`Persona con DNI ${licencia.DNI} no encontrada. Debe existir previamente.`);
    }
    
    // Verificar que la licencia existe usando el endpoint buscarLicencias
    const licenciasResponse = HttpRequestGet.get(
      `licencias/buscar?personaDni=${licencia.DNI}&articulo=${licencia.Artículo}&desde=${licencia.Desde}&hasta=${licencia.Hasta}`
    );
    
    if (!licenciasResponse || licenciasResponse.status !== 200 || !licenciasResponse.data || licenciasResponse.data.length === 0) {
      throw new Error(`No se encontró la licencia para DNI ${licencia.DNI}, artículo ${licencia.Artículo}, período ${licencia.Desde} - ${licencia.Hasta}`);
    }
  });
});

// Step para otorgar nuevas licencias
Given("que se otorgan las siguientes nuevas licencias", function (dataTable) {
  const nuevasLicencias = dataTable.hashes();
  
  nuevasLicencias.forEach(licencia => {
    // Verificar que la persona existe
    let personaEncontrada = PersonaExistente.findByDni(parseInt(licencia.DNI));
  
    
    // Obtener el artículo de licencia
    const articuloResponse = HttpRequestGet.get(`articulos-licencias/codigo/${licencia.Artículo}`);
    if (!articuloResponse || articuloResponse.status !== 200) {
      throw new Error(`No se encontró el artículo ${licencia.Artículo}`);
    }
    
    // Crear la licencia
    const licenciaData = {
      persona: personaEncontrada,
      articuloLicencia: articuloResponse.data,
      pedidoDesde: licencia.Desde,
      pedidoHasta: licencia.Hasta,
      certificadoMedico: licencia.Artículo.startsWith("5")
    };
    
    const licenciaResponse = HttpRequestPost.post("licencias", licenciaData);
    if (licenciaResponse.status !== 200) {
      throw new Error(`Error al crear licencia para DNI ${licencia.DNI}: ${licenciaResponse.message}`);
    }
  });
});

// Step para solicitar el parte diario
When("se solicita el parte diario para la fecha {string}", function (fecha) {
  this.fechaConsulta = fecha;
  this.parteDiarioResponse = HttpRequestGet.get(`licencias/parte-diario/${fecha}`);
});

// Step para validar la respuesta del sistema
Then("el sistema responde", function (docString) {
  const expectedResponse = JSON.parse(docString);
  
  // Verificar que la respuesta tenga el status correcto
  assert.equal(this.parteDiarioResponse.status, 200, 
    `Se esperaba status 200 pero se recibió ${this.parteDiarioResponse.status}`);
  
  // Verificar que la respuesta tenga la estructura esperada
  assert(this.parteDiarioResponse.data, "La respuesta no contiene datos");
  assert(this.parteDiarioResponse.data.ParteDiario, "La respuesta no contiene ParteDiario");
  
  const actualParteDiario = this.parteDiarioResponse.data.ParteDiario;
  const expectedParteDiario = expectedResponse.ParteDiario;
  
  // Verificar la fecha
  assert.equal(actualParteDiario.Fecha, expectedParteDiario.Fecha, 
    `Se esperaba fecha ${expectedParteDiario.Fecha} pero se recibió ${actualParteDiario.Fecha}`);
  
  // Verificar la cantidad de docentes
  assert.equal(actualParteDiario.Docentes.length, expectedParteDiario.Docentes.length,
    `Se esperaban ${expectedParteDiario.Docentes.length} docentes pero se recibieron ${actualParteDiario.Docentes.length}`);
  
  // Verificar cada docente
  expectedParteDiario.Docentes.forEach((expectedDocente, index) => {
    const actualDocente = actualParteDiario.Docentes.find(d => d.DNI === expectedDocente.DNI);
    
    assert(actualDocente, `No se encontró el docente con DNI ${expectedDocente.DNI}`);
    
    assert.equal(actualDocente.Nombre, expectedDocente.Nombre,
      `Nombre incorrecto para DNI ${expectedDocente.DNI}`);
    assert.equal(actualDocente.Apellido, expectedDocente.Apellido,
      `Apellido incorrecto para DNI ${expectedDocente.DNI}`);
    assert.equal(actualDocente.Artículo, expectedDocente.Artículo,
      `Artículo incorrecto para DNI ${expectedDocente.DNI}`);
    assert.equal(actualDocente.Descripción, expectedDocente.Descripción,
      `Descripción incorrecta para DNI ${expectedDocente.DNI}`);
    assert.equal(actualDocente.Desde, expectedDocente.Desde,
      `Fecha desde incorrecta para DNI ${expectedDocente.DNI}`);
    assert.equal(actualDocente.Hasta, expectedDocente.Hasta,
      `Fecha hasta incorrecta para DNI ${expectedDocente.DNI}`);
  });
});