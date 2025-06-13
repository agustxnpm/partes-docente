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
      | 88888888 | Marisa        | Balaguer     | 20-88888888-9 | F    | "Profesora de Literatura" | "Rawson 123"   | "+54 (280) 444-8888" |    200 | "Marisa Balaguer con DNI 88888888 ingresado/a correctamente"     |
      | 99999999 | Raúl          | Guitierrez   | 20-99999999-9 | M    | "Profesor de Química"     | "San Martín 456"| "+54 (280) 499-9999"|    200 | "Raúl Guitierrez con DNI 99999999 ingresado/a correctamente"     |
      | 88100000 | Raúl          | Orellanos    | 27-88100000-9 | M    | "Profesor de Matemática"  | "Belgrano 123" | "+54 (280) 481-0000" |    200 | "Raúl Orellanos con DNI 88100000 ingresado/a correctamente"      |
      | 88200000 | Matías        | Barto        | 27-88200000-9 | M    | "Profesor de Física"      | "Rivadavia 456"| "+54 (280) 482-0000" |    200 | "Matías Barto con DNI 88200000 ingresado/a correctamente"        |
      | 88300000 | Andrea        | Sosa         | 20-88300000-9 | F    | "Profesora de Química"    | "Sarmiento 789"| "+54 (280) 483-0000" |    200 | "Andrea Sosa con DNI 88300000 ingresado/a correctamente"         |
      | 88400000 | Laura         | Barrientos   | 20-88400000-9 | F    | "Profesora de Biología"   | "Moreno 321"   | "+54 (280) 484-0000" |    200 | "Laura Barrientos con DNI 88400000 ingresado/a correctamente"    |
      | 88500000 | Natalia       | Zabala       | 20-88500000-9 | F    | "Profesora de Historia"   | "Alem 654"     | "+54 (280) 485-0000" |    200 | "Natalia Zabala con DNI 88500000 ingresado/a correctamente"      |
      | 88600000 | Marta         | Ríos         | 20-88600000-9 | F    | "Profesora de Lengua"     | "Independencia 987"| "+54 (280) 486-0000"|    200 | "Marta Ríos con DNI 88600000 ingresado/a correctamente"          |
      | 88700000 | Rosalía       | Ramón        | 20-88700000-9 | F    | "Profesora de Arte"       | "Libertad 147" | "+54 (280) 487-0000" |    200 | "Rosalía Ramón con DNI 88700000 ingresado/a correctamente"       |
      | 88800000 | José          | Pérez        | 27-88800000-9 | M    | "Profesor de Educación Física"| "25 de Mayo 258"| "+54 (280) 488-0000"|    200 | "José Pérez con DNI 88800000 ingresado/a correctamente"          |
