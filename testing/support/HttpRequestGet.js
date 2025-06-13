/* clase que aisla la peticion GET al backend para reutilizarla en los test*/

const request = require("sync-request");

class HttpRequestGet {
  static get(endpoint) {
    try {
      const res = request("GET", `http://backend-pd:8080/${endpoint}`, {
        headers: {
          'Content-Type': 'application/json'
        }
      });
      return JSON.parse(res.getBody("utf8"));
    } catch (error) {
      console.error(`Error en petición GET a ${endpoint}:`, error.message);
      throw error;
    }
  }
}

module.exports = HttpRequestGet;