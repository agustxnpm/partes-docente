const { When, Then } = require("@cucumber/cucumber");
const HttpRequestPost = require("../../../support/HttpRequestPost");
const ResponseValidator = require("../../../support/ResponseValidator");
const EndpointStrategy = require("../../../support/EndpointStrategy");


// Paso común: Cuando se presiona el botón de guardar
When("se presiona el botón de guardar", function () {
  const { endpoint, data } = EndpointStrategy.getEndpointInfo(this);
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
