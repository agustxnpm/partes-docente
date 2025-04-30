/* clase que aisla la peticion POST al backend para reutilizarla en los test*/

const request = require("sync-request");

class HttpClient {
  static post(endpoint, data) {
    try {
      //console.log(JSON.stringify(data, null, 2)); // Formato legible
      const res = request("POST", `http://backend-pd:8080/${endpoint}`, {
        json: data,
      });
      return JSON.parse(res.getBody("utf8"));
    } catch (error) {
      console.error(`Error en petición POST a ${endpoint}:`, error.message);
      throw error;
    }
  }
}

module.exports = HttpClient;
