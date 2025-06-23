-- Script para cargar horarios con asignaciones directas a espacios curriculares


-- LUNES - Distribución completa por año y división
-- 1ª hora - Cobertura de todas las divisiones por año
-- 1º AÑO (Divisiones 1-4)
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 1, 3);   -- Historia División 1
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 1, 4);   -- Geografía División 2
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 1, 49);  -- Historia División 2
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 1, 45);  -- Biología División 4
-- 2º AÑO (Divisiones 5-8)
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 1, 7);   -- Tecnología División 5
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 1, 25);  -- Geografía División 6
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 1, 28);  -- Matemática División 7
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 1, 32);  -- Lengua División 8
-- 3º AÑO (Divisiones 9-12)
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 1, 38);  -- Matemática División 9
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 1, 47);  -- Música División 10
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 1, 55);  -- Anatomía División 11
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 1, 57);  -- Física División 12
-- 4º AÑO (Divisiones 13-16)
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 1, 60);  -- Química División 13
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 1, 70);  -- Geografía División 14
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 1, 35);  -- Matemática División 15
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 1, 41);  -- Historia División 16
-- 5º AÑO (Divisiones 17-20)
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 1, 62);  -- Historia División 17
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 1, 67);  -- Biología División 18
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 1, 76);  -- Programación Avanzada División 19
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 1, 79);  -- Arte Contemporáneo División 20
-- 6º AÑO (Divisiones 21-24)
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 1, 82);  -- Física Avanzada División 21
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 1, 85);  -- Historia Argentina División 22
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 1, 88);  -- Microbiología División 23
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 1, 91);  -- Desarrollo Web División 24

-- 2ª hora - Nuevas asignaciones para todas las divisiones
-- 1º AÑO
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 2, 73);  -- Biología Molecular División 1
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 2, 50);  -- Economía División 2
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 2, 52);  -- Historia División 3
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 2, 46);  -- Matemática División 4
-- 2º AÑO
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 2, 65);  -- Programación División 5
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 2, 26);  -- Biología División 6
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 2, 29);  -- Lengua División 7
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 2, 33);  -- Biología División 8
-- 3º AÑO
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 2, 39);  -- Lengua División 9
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 2, 48);  -- Historia del Arte División 10
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 2, 56);  -- Nutrición División 11
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 2, 58);  -- Biología División 12
-- 4º AÑO
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 2, 61);  -- Matemática División 13
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 2, 71);  -- Economía Política División 14
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 2, 36);  -- Lengua División 15
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 2, 42);  -- Geografía División 16
-- 5º AÑO
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 2, 63);  -- Geografía División 17
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 2, 68);  -- Química Orgánica División 18
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 2, 77);  -- Base de Datos División 19
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 2, 80);  -- Historia del Arte División 20
-- 6º AÑO
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 2, 83);  -- Química Analítica División 21
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 2, 86);  -- Geografía Argentina División 22
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 2, 89);  -- Bioquímica División 23
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 2, 92);  -- Inteligencia Artificial División 24

-- 3ª hora - Continuando la distribución
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 3, 25);  -- Geografía División 6
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 3, 30);  -- Historia División 7
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 3, 47);  -- Música División 10
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 3, 59);  -- Física División 13
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 3, 72);  -- Derecho División 14

-- 4ª hora - Más variedad
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 4, 26);  -- Biología División 6
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 4, 31);  -- Inglés División 7
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 4, 48);  -- Historia del Arte División 10
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 4, 60);  -- Química División 13
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 4, 73);  -- Biología Molecular División 1

-- 5ª hora - Continuando
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 5, 6);   -- Física División 4
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 5, 32);  -- Lengua División 8
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 5, 49);  -- Historia División 2
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 5, 61);  -- Matemática División 13
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 5, 74);  -- Ecología División 1

-- 6ª hora - Más asignaciones
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 6, 7);   -- Tecnología División 5
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 6, 33);  -- Biología División 8
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 6, 50);  -- Economía División 2
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 6, 62);  -- Historia División 17
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 6, 75);  -- Biotecnología División 1

-- 7ª hora - Penúltima hora
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 7, 34);  -- Arte División 8
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 7, 51);  -- Filosofía División 2
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 7, 63);  -- Geografía División 17
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 7, 76);  -- Programación Avanzada División 19

-- 8ª hora - Última hora del día
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 8, 35);  -- Matemática División 15
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 8, 52);  -- Historia División 3
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 8, 64);  -- Sociología División 17
INSERT INTO horario (dia, hora, cargo_id) VALUES ('LUNES', 8, 77);  -- Base de Datos División 19

-- MARTES - Nueva distribución evitando repetir divisiones en el mismo horario
-- 1ª hora
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MARTES', 1, 24);  -- Historia División 6
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MARTES', 1, 36);  -- Lengua División 15
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MARTES', 1, 53);  -- Geografía División 3
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MARTES', 1, 65);  -- Programación División 5
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MARTES', 1, 78);  -- Redes División 19

-- 2ª hora
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MARTES', 2, 3);   -- Historia División 1
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MARTES', 2, 37);  -- Biología División 15
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MARTES', 2, 54);  -- Psicología División 3
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MARTES', 2, 66);  -- Sistemas División 5
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MARTES', 2, 79);  -- Arte Contemporáneo División 20

-- 3ª hora
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MARTES', 3, 25);  -- Geografía División 6
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MARTES', 3, 38);  -- Matemática División 9
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MARTES', 3, 55);  -- Anatomía División 11
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MARTES', 3, 67);  -- Biología División 18
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MARTES', 3, 80);  -- Historia del Arte División 20

-- 4ª hora
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MARTES', 4, 27);  -- Educación Física División 6
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MARTES', 4, 39);  -- Lengua División 9
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MARTES', 4, 56);  -- Nutrición División 11
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MARTES', 4, 68);  -- Química Orgánica División 18
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MARTES', 4, 81);  -- Teoría del Color División 20

-- 5ª hora
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MARTES', 5, 6);   -- Física División 4
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MARTES', 5, 40);  -- Química División 9
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MARTES', 5, 82);  -- Física Avanzada División 21
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MARTES', 5, 83);  -- Química Analítica División 21

-- 6ª hora
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MARTES', 6, 28);  -- Matemática División 7
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MARTES', 6, 41);  -- Historia División 16
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MARTES', 6, 84);  -- Matemática Superior División 21

-- 7ª hora
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MARTES', 7, 29);  -- Lengua División 7
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MARTES', 7, 42);  -- Geografía División 16
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MARTES', 7, 85);  -- Historia Argentina División 22

-- 8ª hora
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MARTES', 8, 30);  -- Historia División 7
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MARTES', 8, 43);  -- Lengua División 16
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MARTES', 8, 86);  -- Geografía Argentina División 22

-- MIÉRCOLES - Continuando con variedad y distribución realista
-- 1ª hora
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MIERCOLES', 1, 4);   -- Geografía División 2
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MIERCOLES', 1, 44);  -- Matemática División 16
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MIERCOLES', 1, 87);  -- Formación Ética y Ciudadana División 22
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MIERCOLES', 1, 88);  -- Microbiología División 23

-- 2ª hora
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MIERCOLES', 2, 24);  -- Historia División 6
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MIERCOLES', 2, 31);  -- Inglés División 7
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MIERCOLES', 2, 89);  -- Bioquímica División 23
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MIERCOLES', 2, 90);  -- Investigación Científica División 23

-- 3ª hora
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MIERCOLES', 3, 26);  -- Biología División 6
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MIERCOLES', 3, 32);  -- Lengua División 8
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MIERCOLES', 3, 91);  -- Desarrollo Web División 24
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MIERCOLES', 3, 92);  -- Inteligencia Artificial División 24

-- 4ª hora
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MIERCOLES', 4, 27);  -- Educación Física División 6
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MIERCOLES', 4, 33);  -- Biología División 8
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MIERCOLES', 4, 93);  -- Seguridad Informática División 24

-- 5ª hora
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MIERCOLES', 5, 7);   -- Tecnología División 5
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MIERCOLES', 5, 34);  -- Arte División 8
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MIERCOLES', 5, 45);  -- Biología División 4

-- 6ª hora
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MIERCOLES', 6, 25);  -- Geografía División 6
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MIERCOLES', 6, 46);  -- Matemática División 4
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MIERCOLES', 6, 57);  -- Física División 12

-- 7ª hora
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MIERCOLES', 7, 29);  -- Lengua División 7
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MIERCOLES', 7, 47);  -- Música División 10
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MIERCOLES', 7, 58);  -- Biología División 12

-- 8ª hora
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MIERCOLES', 8, 30);  -- Historia División 7
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MIERCOLES', 8, 48);  -- Historia del Arte División 10
INSERT INTO horario (dia, hora, cargo_id) VALUES ('MIERCOLES', 8, 59);  -- Física División 13

-- JUEVES - Distribución que maximiza el uso de espacios curriculares
-- 1ª hora
INSERT INTO horario (dia, hora, cargo_id) VALUES ('JUEVES', 1, 3);   -- Historia División 1
INSERT INTO horario (dia, hora, cargo_id) VALUES ('JUEVES', 1, 35);  -- Matemática División 15
INSERT INTO horario (dia, hora, cargo_id) VALUES ('JUEVES', 1, 60);  -- Química División 13
INSERT INTO horario (dia, hora, cargo_id) VALUES ('JUEVES', 1, 70);  -- Geografía División 14

-- 2ª hora
INSERT INTO horario (dia, hora, cargo_id) VALUES ('JUEVES', 2, 24);  -- Historia División 6
INSERT INTO horario (dia, hora, cargo_id) VALUES ('JUEVES', 2, 36);  -- Lengua División 15
INSERT INTO horario (dia, hora, cargo_id) VALUES ('JUEVES', 2, 61);  -- Matemática División 13
INSERT INTO horario (dia, hora, cargo_id) VALUES ('JUEVES', 2, 71);  -- Economía Política División 14

-- 3ª hora
INSERT INTO horario (dia, hora, cargo_id) VALUES ('JUEVES', 3, 26);  -- Biología División 6
INSERT INTO horario (dia, hora, cargo_id) VALUES ('JUEVES', 3, 37);  -- Biología División 15
INSERT INTO horario (dia, hora, cargo_id) VALUES ('JUEVES', 3, 72);  -- Derecho División 14

-- 4ª hora
INSERT INTO horario (dia, hora, cargo_id) VALUES ('JUEVES', 4, 27);  -- Educación Física División 6
INSERT INTO horario (dia, hora, cargo_id) VALUES ('JUEVES', 4, 38);  -- Matemática División 9
INSERT INTO horario (dia, hora, cargo_id) VALUES ('JUEVES', 4, 65);  -- Programación División 5

-- 5ª hora
INSERT INTO horario (dia, hora, cargo_id) VALUES ('JUEVES', 5, 7);   -- Tecnología División 5
INSERT INTO horario (dia, hora, cargo_id) VALUES ('JUEVES', 5, 39);  -- Lengua División 9
INSERT INTO horario (dia, hora, cargo_id) VALUES ('JUEVES', 5, 66);  -- Sistemas División 5

-- 6ª hora
INSERT INTO horario (dia, hora, cargo_id) VALUES ('JUEVES', 6, 28);  -- Matemática División 7
INSERT INTO horario (dia, hora, cargo_id) VALUES ('JUEVES', 6, 40);  -- Química División 9
INSERT INTO horario (dia, hora, cargo_id) VALUES ('JUEVES', 6, 67);  -- Biología División 18

-- 7ª hora
INSERT INTO horario (dia, hora, cargo_id) VALUES ('JUEVES', 7, 31);  -- Inglés División 7
INSERT INTO horario (dia, hora, cargo_id) VALUES ('JUEVES', 7, 41);  -- Historia División 16
INSERT INTO horario (dia, hora, cargo_id) VALUES ('JUEVES', 7, 68);  -- Química Orgánica División 18

-- 8ª hora
INSERT INTO horario (dia, hora, cargo_id) VALUES ('JUEVES', 8, 32);  -- Lengua División 8
INSERT INTO horario (dia, hora, cargo_id) VALUES ('JUEVES', 8, 42);  -- Geografía División 16
INSERT INTO horario (dia, hora, cargo_id) VALUES ('JUEVES', 8, 73);  -- Biología Molecular División 1

-- VIERNES - Cierre de semana con distribución equilibrada
-- 1ª hora
INSERT INTO horario (dia, hora, cargo_id) VALUES ('VIERNES', 1, 4);   -- Geografía División 2
INSERT INTO horario (dia, hora, cargo_id) VALUES ('VIERNES', 1, 43);  -- Lengua División 16
INSERT INTO horario (dia, hora, cargo_id) VALUES ('VIERNES', 1, 74);  -- Ecología División 1
INSERT INTO horario (dia, hora, cargo_id) VALUES ('VIERNES', 1, 82);  -- Física Avanzada División 21

-- 2ª hora
INSERT INTO horario (dia, hora, cargo_id) VALUES ('VIERNES', 2, 25);  -- Geografía División 6
INSERT INTO horario (dia, hora, cargo_id) VALUES ('VIERNES', 2, 44);  -- Matemática División 16
INSERT INTO horario (dia, hora, cargo_id) VALUES ('VIERNES', 2, 75);  -- Biotecnología División 1
INSERT INTO horario (dia, hora, cargo_id) VALUES ('VIERNES', 2, 83);  -- Química Analítica División 21

-- 3ª hora
INSERT INTO horario (dia, hora, cargo_id) VALUES ('VIERNES', 3, 26);  -- Biología División 6
INSERT INTO horario (dia, hora, cargo_id) VALUES ('VIERNES', 3, 49);  -- Historia División 2
INSERT INTO horario (dia, hora, cargo_id) VALUES ('VIERNES', 3, 76);  -- Programación Avanzada División 19
INSERT INTO horario (dia, hora, cargo_id) VALUES ('VIERNES', 3, 84);  -- Matemática Superior División 21

-- 4ª hora
INSERT INTO horario (dia, hora, cargo_id) VALUES ('VIERNES', 4, 6);   -- Física División 4
INSERT INTO horario (dia, hora, cargo_id) VALUES ('VIERNES', 4, 50);  -- Economía División 2
INSERT INTO horario (dia, hora, cargo_id) VALUES ('VIERNES', 4, 77);  -- Base de Datos División 19
INSERT INTO horario (dia, hora, cargo_id) VALUES ('VIERNES', 4, 85);  -- Historia Argentina División 22

-- 5ª hora
INSERT INTO horario (dia, hora, cargo_id) VALUES ('VIERNES', 5, 7);   -- Tecnología División 5
INSERT INTO horario (dia, hora, cargo_id) VALUES ('VIERNES', 5, 51);  -- Filosofía División 2
INSERT INTO horario (dia, hora, cargo_id) VALUES ('VIERNES', 5, 78);  -- Redes División 19
INSERT INTO horario (dia, hora, cargo_id) VALUES ('VIERNES', 5, 86);  -- Geografía Argentina División 22

-- 6ª hora
INSERT INTO horario (dia, hora, cargo_id) VALUES ('VIERNES', 6, 33);  -- Biología División 8
INSERT INTO horario (dia, hora, cargo_id) VALUES ('VIERNES', 6, 52);  -- Historia División 3
INSERT INTO horario (dia, hora, cargo_id) VALUES ('VIERNES', 6, 79);  -- Arte Contemporáneo División 20
INSERT INTO horario (dia, hora, cargo_id) VALUES ('VIERNES', 6, 87);  -- Formación Ética y Ciudadana División 22

-- 7ª hora
INSERT INTO horario (dia, hora, cargo_id) VALUES ('VIERNES', 7, 34);  -- Arte División 8
INSERT INTO horario (dia, hora, cargo_id) VALUES ('VIERNES', 7, 53);  -- Geografía División 3
INSERT INTO horario (dia, hora, cargo_id) VALUES ('VIERNES', 7, 80);  -- Historia del Arte División 20
INSERT INTO horario (dia, hora, cargo_id) VALUES ('VIERNES', 7, 88);  -- Microbiología División 23

-- 8ª hora
INSERT INTO horario (dia, hora, cargo_id) VALUES ('VIERNES', 8, 35);  -- Matemática División 15
INSERT INTO horario (dia, hora, cargo_id) VALUES ('VIERNES', 8, 54);  -- Psicología División 3
INSERT INTO horario (dia, hora, cargo_id) VALUES ('VIERNES', 8, 81);  -- Teoría del Color División 20
INSERT INTO horario (dia, hora, cargo_id) VALUES ('VIERNES', 8, 89);  -- Bioquímica División 23
