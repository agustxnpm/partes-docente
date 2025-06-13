# language: es
Característica: designar una persona a un cargo docente
   actividad central de información de la escuela secundaria

  Esquema del escenario: Designación de persona en cargos NO cubiertos aún en el perído indicado de forma existosa
    Dada la persona con <DNI> "<nombre>" y "<apellido>"
    Y que se asigna al cargo con tipo de designación "<tipo>" y "<nombreDesignacion>"
    Y si es espacio curricular asignada a la división <anio> <numero> "<turno>"
    Y se designa por el período "<fechaDesde>" "<fechaHasta>"
    Cuando se presiona el botón de guardar
    Entonces se espera el siguiente <status> con la "<respuesta>"

    Ejemplos:
      | DNI      | nombre      | apellido     | tipo               | nombreDesignacion | anio | numero | turno  | fechaDesde | fechaHasta | status | respuesta                                                                                                      |
      | 10100100 | Alberto     | Lopez        | CARGO              | Vicedirector/a    |    0 |      0 |        | 2023-03-01 |            |    200 | Alberto Lopez ha sido designado/a como Vicedirector/a exitosamente                                             |
      | 40400400 | Marisa      | Amuchástegui | ESPACIO_CURRICULAR | Historia          |    5 |      2 | Mañana | 2023-03-01 |            |    200 | Marisa Amuchástegui ha sido designado/a a la asignatura Historia a la división 5º 2º turno Mañana exitosamente |
      | 50500500 | Raúl        | Gómez        | ESPACIO_CURRICULAR | Geografía         |    3 |      1 | Tarde  | 2023-03-01 | 2025-12-31 |    200 | Raúl Gómez ha sido designado/a a la asignatura Geografía a la división 3º 1º turno Tarde exitosamente          |
      | 99100000 | Ermenegildo | Sabat        | ESPACIO_CURRICULAR | Física            |    2 |      3 | Mañana | 2023-03-01 |            |    200 | Ermenegildo Sabat ha sido designado/a a la asignatura Física a la división 2º 3º turno Mañana exitosamente     |
      | 99200000 | María Rosa  | Gallo        | ESPACIO_CURRICULAR | Matemática        |    1 |      1 | Tarde  | 2023-03-01 |            |    200 | María Rosa Gallo ha sido designado/a a la asignatura Matemática a la división 1º 1º turno Tarde exitosamente   |
      | 99300000 | Homero      | Manzi        | ESPACIO_CURRICULAR | Tecnología        |    4 |      3 | Mañana | 2023-03-01 |            |    200 | Homero Manzi ha sido designado/a a la asignatura Tecnología a la división 4º 3º turno Mañana exitosamente      |
      | 88888888 | Marisa      | Balaguer     | CARGO              | Auxiliar ADM      |    0 |      0 |        | 2023-12-31 |            |    200 | Marisa Balaguer ha sido designado/a como Auxiliar ADM exitosamente                                             |
      | 20000000 | Rosalía     | Fernandez    | CARGO              | Auxiliar ADM      |    0 |      0 |        | 2022-03-01 | 2022-12-31 |    200 | Rosalía Fernandez ha sido designado/a como Auxiliar ADM exitosamente                                           |
      | 20200200 | Susana      | Álvarez      | CARGO              | Preceptor/a       |    0 |      0 |        | 2023-03-01 | 2023-12-31 |    200 | Susana Álvarez ha sido designado/a como Preceptor/a exitosamente                                               |
      | 20000000 | Rosalía     | Fernandez    | CARGO              | Preceptor/a       |    0 |      0 |        | 2024-01-01 | 2024-12-31 |    200 | Rosalía Fernandez ha sido designado/a como Preceptor/a exitosamente                                            |
      | 88100000 | Raúl        | Orellanos    | ESPACIO_CURRICULAR | Matemática        |    1 |      3 | Mañana | 2023-03-01 | 2023-12-31 |    200 | Raúl Orellanos ha sido designado/a a la asignatura Matemática a la división 1º 3º turno Mañana exitosamente   |
      | 88200000 | Matías      | Barto        | ESPACIO_CURRICULAR | Física            |    2 |      1 | Tarde  | 2023-03-01 | 2023-12-31 |    200 | Matías Barto ha sido designado/a a la asignatura Física a la división 2º 1º turno Tarde exitosamente          |
      | 88300000 | Andrea      | Sosa         | ESPACIO_CURRICULAR | Química           |    3 |      4 | Mañana | 2023-03-01 | 2023-12-31 |    200 | Andrea Sosa ha sido designado/a a la asignatura Química a la división 3º 4º turno Mañana exitosamente         |
      | 88400000 | Laura       | Barrientos   | ESPACIO_CURRICULAR | Biología          |    4 |      1 | Tarde  | 2023-03-01 | 2023-12-31 |    200 | Laura Barrientos ha sido designado/a a la asignatura Biología a la división 4º 1º turno Tarde exitosamente    |
      | 88500000 | Natalia     | Zabala       | ESPACIO_CURRICULAR | Historia          |    5 |      1 | Mañana | 2023-03-01 | 2023-12-31 |    200 | Natalia Zabala ha sido designado/a a la asignatura Historia a la división 5º 1º turno Mañana exitosamente     |
      | 88600000 | Marta       | Ríos         | ESPACIO_CURRICULAR | Lengua            |    1 |      2 | Tarde  | 2023-03-01 | 2023-12-31 |    200 | Marta Ríos ha sido designado/a a la asignatura Lengua a la división 1º 2º turno Tarde exitosamente            |
      | 88700000 | Rosalía     | Ramón        | ESPACIO_CURRICULAR | Arte              |    2 |      4 | Mañana | 2023-03-01 | 2023-12-31 |    200 | Rosalía Ramón ha sido designado/a a la asignatura Arte a la división 2º 4º turno Mañana exitosamente          |
      | 88800000 | José        | Pérez        | ESPACIO_CURRICULAR | Educación Física  |    3 |      3 | Tarde  | 2023-03-01 | 2023-12-31 |    200 | José Pérez ha sido designado/a a la asignatura Educación Física a la división 3º 3º turno Tarde exitosamente  |
