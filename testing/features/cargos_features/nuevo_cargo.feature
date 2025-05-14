# language: es
Característica: Gestión de cargos institucionales
  Módulo responsable de administrar los cargos y espacios curriculares en una escuela.

  Esquema del escenario: ingresar nuevo cargo institucional
    Dado el cargo institucional cuyo "<nombre>" que da título al mismo
    Y que es del tipo de designación "<tipoDesignación>"
    Y que tiene una carga horaria de <cargaHoraria> horas, con vigencia desde "<fechaDesde>" hasta "<fechaHasta>"
    Y que si el tipo es "ESPACIO CURRICULAR", opcionalmente se asigna a la división "<año>" "<número>" "<orientacion>" "<turno>"
    Cuando se presiona el botón de guardar
    Entonces se espera el siguiente <status> con la "<respuesta>"

    Ejemplos:
      | nombre         | tipoDesignación    | cargaHoraria | fechaDesde | fechaHasta | año | número | orientacion | turno  | status | respuesta                                                                               |
      | Vicedirector/a | CARGO              |           36 | 2020-03-01 |            |     |        |             |        |    200 | Cargo de Vicedirector/a ingresado correctamente                                         |
      | Preceptor/a    | CARGO              |           36 | 2020-03-01 |            |     |        |             |        |    200 | Cargo de Preceptor/a ingresado correctamente                                            |
      | Historia       | ESPACIO_CURRICULAR |            4 | 2020-03-01 |            |   5 |      2 | Biológicas  | Mañana |    200 | Espacio curricular Historia para la división 5º 2º Turno Mañana ingresado correctamente |
      | Geografía      | ESPACIO_CURRICULAR |            3 | 2020-03-01 |            |   3 |      1 | Sociales    | Tarde  |    200 | Espacio curricular Geografía para la división 3º 1º Turno Tarde ingresado correctamente |
      | Auxiliar ADM   | CARGO              |           30 | 2020-03-01 |            |     |        |             |        |    200 | Cargo de Auxiliar ADM ingresado correctamente                                           |
      | Auxiliar ACAD  | CARGO              |           30 | 2020-03-01 |            |   1 |      1 | Informática | Tarde  |    501 | Cargo de Auxiliar ACAD es CARGO y no corresponde asignar división                       |
      | Economia       | ESPACIO_CURRICULAR |            6 | 2020-03-01 |            |     |        |             |        |    501 | Espacio curricular Economia falta asignar división                                    |
      | Física         | ESPACIO_CURRICULAR |           30 | 2020-03-01 |            |   2 |     3  |  Naturales  | Mañana |    200 | Espacio curricular Física para la división 2º 3º Turno Mañana ingresado correctamente    |
      | Tecnología     | ESPACIO_CURRICULAR |           30 | 2020-03-01 |            |   4 |      3 | Informática | Mañana |    200 | Espacio curricular Tecnología para la división 4º 3º Turno Mañana ingresado correctamente|
      | Matemática     | ESPACIO_CURRICULAR |           30 | 2020-03-01 |            |   1 |      2 | Informática | Tarde  |    200 | Espacio curricular Matemática para la división 1º 2º Turno Tarde ingresado correctamente|
      | Matemática     | ESPACIO_CURRICULAR |           30 | 2020-03-01 |            |   1 |      1 | Informática | Tarde  |    200 | Espacio curricular Matemática para la división 1º 1º Turno Tarde ingresado correctamente|
