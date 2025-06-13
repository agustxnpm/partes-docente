module.exports = {
  default: {
    paths: [
      "features/create_persona.feature",
      "features/create_division.feature",
      "features/create_cargo.feature",
      "features/nueva_designacion.feature",
      "features/designacion_invalida.feature",
      "features/otorgar_licencia.feature",
      "features/parte_diario.feature",
    ],
    require: ["features/**/*.js", "features/step_definitions/*.js"],
    formatOptions: {
      snippetInterface: "synchronous",
    },
  },
};
