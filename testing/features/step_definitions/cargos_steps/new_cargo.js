const { Given, And } = require("@cucumber/cucumber");

// Dado el cargo institucional cuyo "<nombre>" que da título al mismo
Given(
  "el cargo institucional cuyo {string} que da título al mismo",
  function (nombre) {
    this.currentCargo = {
      nombre: nombre,
    };
  }
);

// Y que es del tipo de designación "<tipoDesignación>"
Given('que es del tipo de designación {string}', function(tipoDesignacion) {
    this.currentCargo = {
        ...this.currentCargo,
        tipoDesignacion: tipoDesignacion
    };
});

// Y que tiene una carga horaria de <cargaHoraria> horas, con vigencia desde "<fechaDesde>" hasta "<fechaHasta>"
Given('que tiene una carga horaria de {int} horas, con vigencia desde {string} hasta {string}', 
    function(cargaHoraria, fechaDesde, fechaHasta) {
        this.currentCargo = {
            ...this.currentCargo,
            cargaHoraria: cargaHoraria,
            fechaDesde: fechaDesde,
            fechaHasta: fechaHasta || null
        };
});

// Y que si el tipo es "ESPACIO CURRICULAR", opcionalmente se asigna a la división "<año>" "<número>" "<turno>"
Given('que si el tipo es {string}, opcionalmente se asigna a la división {string} {string} {string}',
    function(tipo, anio, numero, turno) {
        if (tipo === "ESPACIO CURRICULAR" && anio && numero && turno) {
            this.currentCargo = {
                ...this.currentCargo,
                division: {
                    anio: parseInt(anio),
                    numero: parseInt(numero),
                    turno: turno
                }
            };
        }
});

