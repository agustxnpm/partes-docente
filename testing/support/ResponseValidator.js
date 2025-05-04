/* clase que aisla la validacion entre lo que recibe del backend y lo que hay el .feature */
const assert = require("assert");

class ResponseValidator {
  static validateResponse(actual, expectedStatus, expectedMessage) {
    assert.equal(
      actual.status,
      expectedStatus,
      `El código de estado esperado era ${expectedStatus}, pero se recibió ${actual.status}`
    );

    assert.equal(
      actual.message,
      expectedMessage,
      `La respuesta esperada era "${expectedMessage}", pero se recibió "${actual.message}"`
    );
  }
}

module.exports = ResponseValidator;
