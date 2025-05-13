# language: es
Característica: Gestión de personas
Módulo responsable de administrar a las personas del sistema

  Esquema del escenario: ingresar nuevas personas
    Dada la persona con "<nombre>" <apellido> <DNI> <CUIL> <sexo> <título> <domicilio> <teléfono>
    Cuando se presiona el botón de guardar
    Entonces se espera el siguiente <status> con la <respuesta>

    Ejemplos:
      | DNI      | nombre        | apellido     | CUIL          | sexo | título                    | domicilio      | teléfono             | status | respuesta                                                        |
      | 10100100 | Alberto       | Lopez        | 27-10100100-9 | M    | "Profesor de Biología"    | "Charcas 54"   | "+54 (280) 411-1111" |    200 | "Alberto Lopez con DNI 10100100 ingresado/a correctamente"       |
      | 20200200 | Susana        | Álvarez      | 20-20200200-9 | F    | "Profesora de historia"   | "Mitre 154"    | "+54 (280) 422-2222" |    200 | "Susana Álvarez con DNI 20200200 ingresado/a correctamente"      |
      | 30300300 | Pedro         | Benítez      | 27-30300300-9 | M    | ""                        | "Jujuy 255"    | "+54 (280) 433-3333" |    200 | "Pedro Benítez con DNI 30300300 ingresado/a correctamente"       |
      | 40400400 | Marisa        | Amuchástegui | 20-40400400-9 | F    | "Profesora de historia"   | "Zar 555"      | "+54 (280) 444-4444" |    200 | "Marisa Amuchástegui con DNI 40400400 ingresado/a correctamente" |
      | 50500500 | Raúl          | Gómez        | 27-50500500-9 | M    | "Profesor de Geografía"   | "Roca 2458"    | "+54 (280) 455-5555" |    200 | "Raúl Gómez con DNI 50500500 ingresado/a correctamente"          |
      | 60600600 | Inés          | Torres       | 20-60600600-9 | F    | "Licenciada en Geografía" | "La Pampa 322" | "+54 (280) 466-6666" |    200 | "Inés Torres con DNI 60600600 ingresado/a correctamente"         |
      | 70700700 | Jorge         | Dismal       | 27-70700700-9 | M    | ""                        | "Mitre 1855"   | "+54 (280) 477-7777" |    200 | "Jorge Dismal con DNI 70700700 ingresado/a correctamente"        |
      | 20000000 | Rosalía       | Fernandez    | 20-20000000-9 | F    | "Maestra de grado"        | "Maiz 356"     | "+54 (280) 420-0000" |    200 | "Rosalía Fernandez con DNI 20000000 ingresado/a correctamente"   |
      | 80800800 | Analía        | Rojas        | 20-80800800-9 | F    | "Técnica superior"        | "Rosa 556"     | "+54 (280) 488-8888" |    200 | "Analía Rojas con DNI 80800800 ingresado/a correctamente"        |
      | 99100000 | Ermenegildo   | Sabat        | 27-99100000-9 | M    | ""                        | "Mitre 32"     | "+54 (280) 477-4721" |    200 | "Ermenegildo Sabat con DNI 99100000 ingresado/a correctamente"   |
      | 99200000 | María Rosa    | Gallo        | 20-99200000-9 | F    | "Maestra de grado"        | "Maiz 512"     | "+54 (280) 420-5124" |    200 | "María Rosa Gallo con DNI 99200000 ingresado/a correctamente"    |
      | 99300000 | Homero        | Manzi        | 20-99300000-9 | M    | "Técnico superior"        | "Rosa 123"     | "+54 (280) 488-4231" |    200 | "Homero Manzi con DNI 99300000 ingresado/a correctamente"        |
