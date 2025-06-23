-- Script para designar personas a cargos y espacios curriculares
-- Se evitan conflictos con las designaciones ya existentes en los tests

-- Designaciones de cargos institucionales adicionales
-- Director
INSERT INTO designacion (persona_id, cargo_id, fecha_inicio, fecha_fin, situacion_revista) 
SELECT 
    (SELECT id FROM persona WHERE dni = 11111111 LIMIT 1), 
    (SELECT id FROM cargo WHERE nombre = 'Director/a' AND tipo_designacion = 'CARGO' LIMIT 1), 
    '2023-03-01', NULL, 'Titular';

-- Secretario
INSERT INTO designacion (persona_id, cargo_id, fecha_inicio, fecha_fin, situacion_revista) 
SELECT 
    (SELECT id FROM persona WHERE dni = 22222222 LIMIT 1), 
    (SELECT id FROM cargo WHERE nombre = 'Secretario/a' AND tipo_designacion = 'CARGO' LIMIT 1), 
    '2023-03-01', NULL, 'Titular';

-- Bibliotecario
INSERT INTO designacion (persona_id, cargo_id, fecha_inicio, fecha_fin, situacion_revista) 
SELECT 
    (SELECT id FROM persona WHERE dni = 33333333 LIMIT 1), 
    (SELECT id FROM cargo WHERE nombre = 'Bibliotecario/a' AND tipo_designacion = 'CARGO' LIMIT 1), 
    '2023-03-01', NULL, 'Titular';

-- Coordinador Pedagógico
INSERT INTO designacion (persona_id, cargo_id, fecha_inicio, fecha_fin, situacion_revista) 
SELECT 
    (SELECT id FROM persona WHERE dni = 44444444 LIMIT 1), 
    (SELECT id FROM cargo WHERE nombre = 'Coordinador/a Pedagógico/a' AND tipo_designacion = 'CARGO' LIMIT 1), 
    '2023-03-01', NULL, 'Titular';

-- Auxiliar de Laboratorio
INSERT INTO designacion (persona_id, cargo_id, fecha_inicio, fecha_fin, situacion_revista) 
SELECT 
    (SELECT id FROM persona WHERE dni = 55555555 LIMIT 1), 
    (SELECT id FROM cargo WHERE nombre = 'Auxiliar de Laboratorio' AND tipo_designacion = 'CARGO' LIMIT 1), 
    '2023-03-01', NULL, 'Titular';

-- Portero
INSERT INTO designacion (persona_id, cargo_id, fecha_inicio, fecha_fin, situacion_revista) 
SELECT 
    (SELECT id FROM persona WHERE dni = 66666666 LIMIT 1), 
    (SELECT id FROM cargo WHERE nombre = 'Portero/a' AND tipo_designacion = 'CARGO' LIMIT 1), 
    '2023-03-01', NULL, 'Titular';

-- Designaciones de espacios curriculares nuevos
-- 1º año
-- 1º 1º Tarde - Matemática
INSERT INTO designacion (persona_id, cargo_id, fecha_inicio, fecha_fin, situacion_revista) 
SELECT 
    (SELECT id FROM persona WHERE dni = 77777777 LIMIT 1), 
    (SELECT id FROM cargo WHERE nombre = 'Matemática' AND tipo_designacion = 'ESPACIO_CURRICULAR' AND division_id = (SELECT id FROM division WHERE anio = 1 AND num_division = 1 AND turno = 'Tarde' LIMIT 1) LIMIT 1), 
    '2023-03-01', NULL, 'Titular';

-- 1º 1º Tarde - Lengua  
INSERT INTO designacion (persona_id, cargo_id, fecha_inicio, fecha_fin, situacion_revista) 
SELECT 
    (SELECT id FROM persona WHERE dni = 12345678 LIMIT 1), 
    (SELECT id FROM cargo WHERE nombre = 'Lengua' AND tipo_designacion = 'ESPACIO_CURRICULAR' AND division_id = (SELECT id FROM division WHERE anio = 1 AND num_division = 1 AND turno = 'Tarde' LIMIT 1) LIMIT 1), 
    '2023-03-01', '2023-12-31', 'Titular';

-- 1º 1º Tarde - Historia
INSERT INTO designacion (persona_id, cargo_id, fecha_inicio, fecha_fin, situacion_revista) 
SELECT 
    (SELECT id FROM persona WHERE dni = 23456789 LIMIT 1), 
    (SELECT id FROM cargo WHERE nombre = 'Historia' AND tipo_designacion = 'ESPACIO_CURRICULAR' AND division_id = (SELECT id FROM division WHERE anio = 1 AND num_division = 1 AND turno = 'Tarde' LIMIT 1) LIMIT 1), 
    '2023-03-01', '2023-12-31', 'Titular';

-- 1º 1º Tarde - Inglés
INSERT INTO designacion (persona_id, cargo_id, fecha_inicio, fecha_fin, situacion_revista) 
SELECT 
    (SELECT id FROM persona WHERE dni = 34567890 LIMIT 1), 
    (SELECT id FROM cargo WHERE nombre = 'Inglés' AND tipo_designacion = 'ESPACIO_CURRICULAR' AND division_id = (SELECT id FROM division WHERE anio = 1 AND num_division = 1 AND turno = 'Tarde' LIMIT 1) LIMIT 1), 
    '2023-03-01', '2023-12-31', 'Titular';

-- 1º 2º Tarde - Historia
INSERT INTO designacion (persona_id, cargo_id, fecha_inicio, fecha_fin, situacion_revista) 
SELECT 
    (SELECT id FROM persona WHERE dni = 45678901 LIMIT 1), 
    (SELECT id FROM cargo WHERE nombre = 'Historia' AND tipo_designacion = 'ESPACIO_CURRICULAR' AND division_id = (SELECT id FROM division WHERE anio = 1 AND num_division = 2 AND turno = 'Tarde' LIMIT 1) LIMIT 1), 
    '2023-03-01', '2023-12-31', 'Titular';

-- 1º 2º Tarde - Geografía
INSERT INTO designacion (persona_id, cargo_id, fecha_inicio, fecha_fin, situacion_revista) 
SELECT 
    (SELECT id FROM persona WHERE dni = 56789012 LIMIT 1), 
    (SELECT id FROM cargo WHERE nombre = 'Geografía' AND tipo_designacion = 'ESPACIO_CURRICULAR' AND division_id = (SELECT id FROM division WHERE anio = 1 AND num_division = 2 AND turno = 'Tarde' LIMIT 1) LIMIT 1), 
    '2023-03-01', '2023-12-31', 'Titular';

-- 1º 2º Tarde - Biología
INSERT INTO designacion (persona_id, cargo_id, fecha_inicio, fecha_fin, situacion_revista) 
SELECT 
    (SELECT id FROM persona WHERE dni = 67890123 LIMIT 1), 
    (SELECT id FROM cargo WHERE nombre = 'Biología' AND tipo_designacion = 'ESPACIO_CURRICULAR' AND division_id = (SELECT id FROM division WHERE anio = 1 AND num_division = 2 AND turno = 'Tarde' LIMIT 1) LIMIT 1), 
    '2023-03-01', '2023-12-31', 'Titular';

-- 1º 2º Tarde - Educación Física
INSERT INTO designacion (persona_id, cargo_id, fecha_inicio, fecha_fin, situacion_revista) 
SELECT 
    (SELECT id FROM persona WHERE dni = 78901234 LIMIT 1), 
    (SELECT id FROM cargo WHERE nombre = 'Educación Física' AND tipo_designacion = 'ESPACIO_CURRICULAR' AND division_id = (SELECT id FROM division WHERE anio = 1 AND num_division = 2 AND turno = 'Tarde' LIMIT 1) LIMIT 1), 
    '2023-03-01', '2023-12-31', 'Titular';

-- 1º 3º Mañana - Lengua
INSERT INTO designacion (persona_id, cargo_id, fecha_inicio, fecha_fin, situacion_revista) 
SELECT 
    (SELECT id FROM persona WHERE dni = 89012345 LIMIT 1), 
    (SELECT id FROM cargo WHERE nombre = 'Lengua' AND tipo_designacion = 'ESPACIO_CURRICULAR' AND division_id = (SELECT id FROM division WHERE anio = 1 AND num_division = 3 AND turno = 'Mañana' LIMIT 1) LIMIT 1), 
    '2023-03-01', '2023-12-31', 'Titular';

-- 1º 3º Mañana - Biología
INSERT INTO designacion (persona_id, cargo_id, fecha_inicio, fecha_fin, situacion_revista) 
SELECT 
    (SELECT id FROM persona WHERE dni = 90123456 LIMIT 1), 
    (SELECT id FROM cargo WHERE nombre = 'Biología' AND tipo_designacion = 'ESPACIO_CURRICULAR' AND division_id = (SELECT id FROM division WHERE anio = 1 AND num_division = 3 AND turno = 'Mañana' LIMIT 1) LIMIT 1), 
    '2023-03-01', '2023-12-31', 'Titular';

-- 1º 3º Mañana - Arte
INSERT INTO designacion (persona_id, cargo_id, fecha_inicio, fecha_fin, situacion_revista) 
SELECT 
    (SELECT id FROM persona WHERE dni = 10987654 LIMIT 1), 
    (SELECT id FROM cargo WHERE nombre = 'Arte' AND tipo_designacion = 'ESPACIO_CURRICULAR' AND division_id = (SELECT id FROM division WHERE anio = 1 AND num_division = 3 AND turno = 'Mañana' LIMIT 1) LIMIT 1), 
    '2023-03-01', '2023-12-31', 'Titular';

-- 1º 4º Mañana - Matemática
INSERT INTO designacion (persona_id, cargo_id, fecha_inicio, fecha_fin, situacion_revista) 
SELECT 
    (SELECT id FROM persona WHERE dni = 21098765 LIMIT 1), 
    (SELECT id FROM cargo WHERE nombre = 'Matemática' AND tipo_designacion = 'ESPACIO_CURRICULAR' AND division_id = (SELECT id FROM division WHERE anio = 1 AND num_division = 4 AND turno = 'Mañana' LIMIT 1) LIMIT 1), 
    '2023-03-01', '2023-12-31', 'Titular';

-- 1º 4º Mañana - Lengua
INSERT INTO designacion (persona_id, cargo_id, fecha_inicio, fecha_fin, situacion_revista) 
SELECT 
    (SELECT id FROM persona WHERE dni = 32109876 LIMIT 1), 
    (SELECT id FROM cargo WHERE nombre = 'Lengua' AND tipo_designacion = 'ESPACIO_CURRICULAR' AND division_id = (SELECT id FROM division WHERE anio = 1 AND num_division = 4 AND turno = 'Mañana' LIMIT 1) LIMIT 1), 
    '2023-03-01', '2023-12-31', 'Titular';

-- 1º 4º Mañana - Biología
INSERT INTO designacion (persona_id, cargo_id, fecha_inicio, fecha_fin, situacion_revista) 
SELECT 
    (SELECT id FROM persona WHERE dni = 43210987 LIMIT 1), 
    (SELECT id FROM cargo WHERE nombre = 'Biología' AND tipo_designacion = 'ESPACIO_CURRICULAR' AND division_id = (SELECT id FROM division WHERE anio = 1 AND num_division = 4 AND turno = 'Mañana' LIMIT 1) LIMIT 1), 
    '2023-03-01', '2023-12-31', 'Titular';

-- 2º año
-- 2º 1º Tarde - Matemática
INSERT INTO designacion (persona_id, cargo_id, fecha_inicio, fecha_fin, situacion_revista) 
SELECT 
    (SELECT id FROM persona WHERE dni = 70700700 LIMIT 1), 
    (SELECT id FROM cargo WHERE nombre = 'Matemática' AND tipo_designacion = 'ESPACIO_CURRICULAR' AND division_id = (SELECT id FROM division WHERE anio = 2 AND num_division = 1 AND turno = 'Tarde' LIMIT 1) LIMIT 1), 
    '2023-03-01', '2023-12-31', 'Titular';

-- 2º 1º Tarde - Lengua
INSERT INTO designacion (persona_id, cargo_id, fecha_inicio, fecha_fin, situacion_revista) 
SELECT 
    (SELECT id FROM persona WHERE dni = 80800800 LIMIT 1), 
    (SELECT id FROM cargo WHERE nombre = 'Lengua' AND tipo_designacion = 'ESPACIO_CURRICULAR' AND division_id = (SELECT id FROM division WHERE anio = 2 AND num_division = 1 AND turno = 'Tarde' LIMIT 1) LIMIT 1), 
    '2023-03-01', '2023-12-31', 'Titular';

-- 2º 1º Tarde - Química
INSERT INTO designacion (persona_id, cargo_id, fecha_inicio, fecha_fin, situacion_revista) 
SELECT 
    (SELECT id FROM persona WHERE dni = 88300000 LIMIT 1), 
    (SELECT id FROM cargo WHERE nombre = 'Química' AND tipo_designacion = 'ESPACIO_CURRICULAR' AND division_id = (SELECT id FROM division WHERE anio = 2 AND num_division = 1 AND turno = 'Tarde' LIMIT 1) LIMIT 1), 
    '2024-01-01', '2024-12-31', 'Titular';

-- 2º 2º Tarde - Historia
INSERT INTO designacion (persona_id, cargo_id, fecha_inicio, fecha_fin, situacion_revista) 
SELECT 
    (SELECT id FROM persona WHERE dni = 30300300 LIMIT 1), 
    (SELECT id FROM cargo WHERE nombre = 'Historia' AND tipo_designacion = 'ESPACIO_CURRICULAR' AND division_id = (SELECT id FROM division WHERE anio = 2 AND num_division = 2 AND turno = 'Tarde' LIMIT 1) LIMIT 1), 
    '2023-03-01', '2023-12-31', 'Titular';

-- 2º 2º Tarde - Geografía
INSERT INTO designacion (persona_id, cargo_id, fecha_inicio, fecha_fin, situacion_revista) 
SELECT 
    (SELECT id FROM persona WHERE dni = 60600600 LIMIT 1), 
    (SELECT id FROM cargo WHERE nombre = 'Geografía' AND tipo_designacion = 'ESPACIO_CURRICULAR' AND division_id = (SELECT id FROM division WHERE anio = 2 AND num_division = 2 AND turno = 'Tarde' LIMIT 1) LIMIT 1), 
    '2023-03-01', '2023-12-31', 'Titular';

-- 2º 2º Tarde - Lengua
INSERT INTO designacion (persona_id, cargo_id, fecha_inicio, fecha_fin, situacion_revista) 
SELECT 
    (SELECT id FROM persona WHERE dni = 88600000 LIMIT 1), 
    (SELECT id FROM cargo WHERE nombre = 'Lengua' AND tipo_designacion = 'ESPACIO_CURRICULAR' AND division_id = (SELECT id FROM division WHERE anio = 2 AND num_division = 2 AND turno = 'Tarde' LIMIT 1) LIMIT 1), 
    '2024-01-01', '2024-12-31', 'Titular';

-- 2º 2º Tarde - Matemática
INSERT INTO designacion (persona_id, cargo_id, fecha_inicio, fecha_fin, situacion_revista) 
SELECT 
    (SELECT id FROM persona WHERE dni = 88100000 LIMIT 1), 
    (SELECT id FROM cargo WHERE nombre = 'Matemática' AND tipo_designacion = 'ESPACIO_CURRICULAR' AND division_id = (SELECT id FROM division WHERE anio = 2 AND num_division = 2 AND turno = 'Tarde' LIMIT 1) LIMIT 1), 
    '2024-01-01', '2024-12-31', 'Titular';

-- 2º 3º Mañana - Biología
INSERT INTO designacion (persona_id, cargo_id, fecha_inicio, fecha_fin, situacion_revista) 
SELECT 
    (SELECT id FROM persona WHERE dni = 10100100 LIMIT 1), 
    (SELECT id FROM cargo WHERE nombre = 'Biología' AND tipo_designacion = 'ESPACIO_CURRICULAR' AND division_id = (SELECT id FROM division WHERE anio = 2 AND num_division = 3 AND turno = 'Mañana' LIMIT 1) LIMIT 1), 
    '2024-01-01', '2024-12-31', 'Titular';

-- 2º 3º Mañana - Matemática
INSERT INTO designacion (persona_id, cargo_id, fecha_inicio, fecha_fin, situacion_revista) 
SELECT 
    (SELECT id FROM persona WHERE dni = 88100000 LIMIT 1), 
    (SELECT id FROM cargo WHERE nombre = 'Matemática' AND tipo_designacion = 'ESPACIO_CURRICULAR' AND division_id = (SELECT id FROM division WHERE anio = 2 AND num_division = 3 AND turno = 'Mañana' LIMIT 1) LIMIT 1), 
    '2024-01-01', '2024-12-31', 'Titular';

-- 2º 4º Mañana - Música
INSERT INTO designacion (persona_id, cargo_id, fecha_inicio, fecha_fin, situacion_revista) 
SELECT 
    (SELECT id FROM persona WHERE dni = 56789012 LIMIT 1), 
    (SELECT id FROM cargo WHERE nombre = 'Música' AND tipo_designacion = 'ESPACIO_CURRICULAR' AND division_id = (SELECT id FROM division WHERE anio = 2 AND num_division = 4 AND turno = 'Mañana' LIMIT 1) LIMIT 1), 
    '2024-01-01', '2024-12-31', 'Titular';

-- 2º 4º Mañana - Historia del Arte
INSERT INTO designacion (persona_id, cargo_id, fecha_inicio, fecha_fin, situacion_revista) 
SELECT 
    (SELECT id FROM persona WHERE dni = 12345678 LIMIT 1), 
    (SELECT id FROM cargo WHERE nombre = 'Historia del Arte' AND tipo_designacion = 'ESPACIO_CURRICULAR' AND division_id = (SELECT id FROM division WHERE anio = 2 AND num_division = 4 AND turno = 'Mañana' LIMIT 1) LIMIT 1), 
    '2024-01-01', '2024-12-31', 'Titular';

-- 3º año
-- 3º 1º Tarde - Historia
INSERT INTO designacion (persona_id, cargo_id, fecha_inicio, fecha_fin, situacion_revista) 
SELECT 
    (SELECT id FROM persona WHERE dni = 40400400 LIMIT 1), 
    (SELECT id FROM cargo WHERE nombre = 'Historia' AND tipo_designacion = 'ESPACIO_CURRICULAR' AND division_id = (SELECT id FROM division WHERE anio = 3 AND num_division = 1 AND turno = 'Tarde' LIMIT 1) LIMIT 1), 
    '2024-01-01', '2024-12-31', 'Titular';

-- 3º 1º Tarde - Economía
INSERT INTO designacion (persona_id, cargo_id, fecha_inicio, fecha_fin, situacion_revista) 
SELECT 
    (SELECT id FROM persona WHERE dni = 90123456 LIMIT 1), 
    (SELECT id FROM cargo WHERE nombre = 'Economía' AND tipo_designacion = 'ESPACIO_CURRICULAR' AND division_id = (SELECT id FROM division WHERE anio = 3 AND num_division = 1 AND turno = 'Tarde' LIMIT 1) LIMIT 1), 
    '2024-01-01', '2024-12-31', 'Titular';

-- 3º 1º Tarde - Filosofía
INSERT INTO designacion (persona_id, cargo_id, fecha_inicio, fecha_fin, situacion_revista) 
SELECT 
    (SELECT id FROM persona WHERE dni = 78901234 LIMIT 1), 
    (SELECT id FROM cargo WHERE nombre = 'Filosofía' AND tipo_designacion = 'ESPACIO_CURRICULAR' AND division_id = (SELECT id FROM division WHERE anio = 3 AND num_division = 1 AND turno = 'Tarde' LIMIT 1) LIMIT 1), 
    '2024-01-01', '2024-12-31', 'Titular';


-- 4º 1º Tarde - Física
INSERT INTO designacion (persona_id, cargo_id, fecha_inicio, fecha_fin, situacion_revista) 
SELECT 
    (SELECT id FROM persona WHERE dni = 88200000 LIMIT 1), 
    (SELECT id FROM cargo WHERE nombre = 'Física' AND tipo_designacion = 'ESPACIO_CURRICULAR' AND division_id = (SELECT id FROM division WHERE anio = 4 AND num_division = 1 AND turno = 'Tarde' LIMIT 1) LIMIT 1), 
    '2024-01-01', '2024-12-31', 'Titular';

-- 5º 3º Tarde - Programación Avanzada
INSERT INTO designacion (persona_id, cargo_id, fecha_inicio, fecha_fin, situacion_revista) 
SELECT 
    (SELECT id FROM persona WHERE dni = 99300000 LIMIT 1), 
    (SELECT id FROM cargo WHERE nombre = 'Programación Avanzada' AND tipo_designacion = 'ESPACIO_CURRICULAR' AND division_id = (SELECT id FROM division WHERE anio = 5 AND num_division = 3 AND turno = 'Tarde' LIMIT 1) LIMIT 1), 
    '2024-01-01', '2024-12-31', 'Titular';

-- 6º 4º Tarde - Desarrollo Web
INSERT INTO designacion (persona_id, cargo_id, fecha_inicio, fecha_fin, situacion_revista) 
SELECT 
    (SELECT id FROM persona WHERE dni = 32109876 LIMIT 1), 
    (SELECT id FROM cargo WHERE nombre = 'Desarrollo Web' AND tipo_designacion = 'ESPACIO_CURRICULAR' AND division_id = (SELECT id FROM division WHERE anio = 6 AND num_division = 4 AND turno = 'Tarde' LIMIT 1) LIMIT 1), 
    '2024-01-01', '2024-12-31', 'Titular';

