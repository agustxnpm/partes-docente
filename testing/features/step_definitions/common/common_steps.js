const { When, Then } = require("@cucumber/cucumber");
const HttpRequestPost = require("../../../support/HttpRequestPost");
const ResponseValidator = require("../../../support/ResponseValidator");

// Paso común: Cuando se presiona el botón de guardar
When("se presiona el botón de guardar", function () {
  let endpoint = "";
  let data = {};

  if (this.currentDivision) {
    endpoint = "divisiones";
    data = this.currentDivision;
  } else if (this.currentPersona) {
    endpoint = "personas";
    data = this.currentPersona;
  } else {
    throw new Error("No se encontró un contexto válido para la operación.");
  }

  this.apiResponse = HttpRequestPost.post(endpoint, data);
});

// Paso común: Entonces se espera el siguiente <status> con la <respuesta>
Then(
  "se espera el siguiente {int} con la {string}",
  function (expectedStatus, expectedResponse) {
    ResponseValidator.validateResponse(
      this.apiResponse,
      expectedStatus,
      expectedResponse
    );
  }
);
