# language: es

Característica: Gestión de divisiones
Módulo responsable de administrar a las divisiones (espacios físicos) de una escuela

Esquema del escenario: ingresar nueva división
   Dada el espacio físico división con <año> <número> "<orientación>" "<turno>" 
   Cuando se presiona el botón de guardar
   Entonces se espera el siguiente <status> con la "<respuesta>"

   Ejemplos:
   | año | número | orientación | turno  | status | respuesta                                            |
   | 5   | 2      | Biológicas  | Mañana | 200    | División 5º 2º turno Mañana ingresada correctamente  |
   | 3   | 1      | Sociales    | Tarde  | 200    | División 3º 1º turno Tarde ingresada correctamente   |
   | 3   | 2      | Sociales    | Tarde  | 200    | División 3º 2º turno Tarde ingresada correctamente   |
   | 2   | 3      | Naturales   | Mañana | 200    | División 2º 3º turno Mañana ingresada correctamente   |
   | 4   | 3      | Informática | Mañana | 200    | División 4º 3º turno Mañana ingresada correctamente   |
   | 1   | 2      | Informática | Tarde  | 200    | División 1º 2º turno Tarde ingresada correctamente   |
   | 1   | 1      | Informática | Tarde  | 200    | División 1º 1º turno Tarde ingresada correctamente   |
   | 1   | 3      | Informática | Mañana | 200    | División 1º 3º turno Mañana ingresada correctamente  |
   | 2   | 1      | Naturales   | Tarde  | 200    | División 2º 1º turno Tarde ingresada correctamente   |
   | 2   | 4      | Artística   | Mañana | 200    | División 2º 4º turno Mañana ingresada correctamente  |
   | 3   | 3      | Deportiva   | Tarde  | 200    | División 3º 3º turno Tarde ingresada correctamente   |
   | 3   | 4      | Naturales   | Mañana | 200    | División 3º 4º turno Mañana ingresada correctamente  |
   | 4   | 1      | Naturales   | Tarde  | 200    | División 4º 1º turno Tarde ingresada correctamente   |
   | 5   | 1      | Sociales    | Mañana | 200    | División 5º 1º turno Mañana ingresada correctamente  |
