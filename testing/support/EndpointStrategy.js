class EndpointStrategy {
  static strategies = new Map([
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
    [
      "currentDesignation",
      {
        endpoint: "designaciones",
        getData: (context) => ({
          persona: context.currentPerson,
          cargo: context.currentCargo,
          division: context.currentDivision || null,
          periodo: context.currentPeriod,
        }),
      },
    ],
    // agregar más estrategias según se necesiten
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
