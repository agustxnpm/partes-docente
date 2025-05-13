class EndpointStrategy {
  static strategies = new Map([
    [
      "currentDesignation",
      {
        endpoint: "designaciones",
        getData: (context) => ({
          persona: context.currentDesignation.persona,
          cargo: context.currentDesignation.cargo,
          situacionRevista: context.currentDesignation.situacionRevista,
          fechaInicio: context.currentDesignation.fechaInicio,
          fechaFin: context.currentDesignation.fechaFin,
        }),
      },
    ],
    [
      "currentDivision",
      { endpoint: "divisiones", getData: (context) => context.currentDivision },
    ],
    [
      "currentPersona",
      { endpoint: "personas", getData: (context) => context.currentPersona },
    ],
    [
      "currentCargo",
      { endpoint: "cargos", getData: (context) => context.currentCargo },
    ],
  ]);

  static getEndpointInfo(context) {
    for (const [key, strategy] of this.strategies) {
      if (context[key]) {
        return {
          endpoint: strategy.endpoint,
          data: strategy.getData(context),
        };
      }
    }
    throw new Error("No se encontró un contexto válido para la operación.");
  }
}

module.exports = EndpointStrategy;