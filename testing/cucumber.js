module.exports = {
  default: {
    paths: [
      'features/personas_features/create_persona.feature', // Asegúrate de que exista
      'features/divisiones_features/create_division.feature',
      'features/cargos_features/nuevo_cargo.feature'
    ],
    require: [
      'features/**/*.js',
      'features/step_definitions/**/*.js'
    ],
    formatOptions: {
      snippetInterface: "synchronous"
    }
  }
};