-- Script para agregar espacios curriculares adicionales 
-- Se distribuyen entre todas las divisiones existentes
-- Los tests ya tienen varios espacios curriculares, agregamos más para completar

-- Espacios curriculares para 1º año
-- 1º 2º Tarde (Informática) - ya tiene Matemática y Lengua
INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Historia', 'ESPACIO_CURRICULAR', 3, '2020-03-01', NULL, id FROM division WHERE anio = 1 AND num_division = 2 AND turno = 'Tarde';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Geografía', 'ESPACIO_CURRICULAR', 3, '2020-03-01', NULL, id FROM division WHERE anio = 1 AND num_division = 2 AND turno = 'Tarde';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Biología', 'ESPACIO_CURRICULAR', 3, '2020-03-01', NULL, id FROM division WHERE anio = 1 AND num_division = 2 AND turno = 'Tarde';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Educación Física', 'ESPACIO_CURRICULAR', 3, '2020-03-01', NULL, id FROM division WHERE anio = 1 AND num_division = 2 AND turno = 'Tarde';

-- 1º 1º Tarde (Informática) - nueva división
INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Matemática', 'ESPACIO_CURRICULAR', 5, '2020-03-01', NULL, id FROM division WHERE anio = 1 AND num_division = 1 AND turno = 'Tarde';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Lengua', 'ESPACIO_CURRICULAR', 4, '2020-03-01', NULL, id FROM division WHERE anio = 1 AND num_division = 1 AND turno = 'Tarde';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Historia', 'ESPACIO_CURRICULAR', 3, '2020-03-01', NULL, id FROM division WHERE anio = 1 AND num_division = 1 AND turno = 'Tarde';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Inglés', 'ESPACIO_CURRICULAR', 3, '2020-03-01', NULL, id FROM division WHERE anio = 1 AND num_division = 1 AND turno = 'Tarde';

-- 1º 3º Mañana (Informática) - ya tiene Matemática
INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Lengua', 'ESPACIO_CURRICULAR', 4, '2020-03-01', NULL, id FROM division WHERE anio = 1 AND num_division = 3 AND turno = 'Mañana';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Biología', 'ESPACIO_CURRICULAR', 3, '2020-03-01', NULL, id FROM division WHERE anio = 1 AND num_division = 3 AND turno = 'Mañana';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Arte', 'ESPACIO_CURRICULAR', 2, '2020-03-01', NULL, id FROM division WHERE anio = 1 AND num_division = 3 AND turno = 'Mañana';

-- 1º 4º Mañana (Naturales) - nueva división
INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Matemática', 'ESPACIO_CURRICULAR', 5, '2020-03-01', NULL, id FROM division WHERE anio = 1 AND num_division = 4 AND turno = 'Mañana';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Lengua', 'ESPACIO_CURRICULAR', 4, '2020-03-01', NULL, id FROM division WHERE anio = 1 AND num_division = 4 AND turno = 'Mañana';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Biología', 'ESPACIO_CURRICULAR', 4, '2020-03-01', NULL, id FROM division WHERE anio = 1 AND num_division = 4 AND turno = 'Mañana';

-- Espacios curriculares para 2º año
-- 2º 1º Tarde (Naturales) - ya tiene Física
INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Matemática', 'ESPACIO_CURRICULAR', 4, '2020-03-01', NULL, id FROM division WHERE anio = 2 AND num_division = 1 AND turno = 'Tarde';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Lengua', 'ESPACIO_CURRICULAR', 4, '2020-03-01', NULL, id FROM division WHERE anio = 2 AND num_division = 1 AND turno = 'Tarde';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Química', 'ESPACIO_CURRICULAR', 3, '2020-03-01', NULL, id FROM division WHERE anio = 2 AND num_division = 1 AND turno = 'Tarde';

-- 2º 2º Tarde (Sociales) - nueva división
INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Historia', 'ESPACIO_CURRICULAR', 4, '2020-03-01', NULL, id FROM division WHERE anio = 2 AND num_division = 2 AND turno = 'Tarde';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Geografía', 'ESPACIO_CURRICULAR', 4, '2020-03-01', NULL, id FROM division WHERE anio = 2 AND num_division = 2 AND turno = 'Tarde';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Lengua', 'ESPACIO_CURRICULAR', 4, '2020-03-01', NULL, id FROM division WHERE anio = 2 AND num_division = 2 AND turno = 'Tarde';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Matemática', 'ESPACIO_CURRICULAR', 4, '2020-03-01', NULL, id FROM division WHERE anio = 2 AND num_division = 2 AND turno = 'Tarde';

-- 2º 3º Mañana (Naturales) - ya tiene Física
INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Biología', 'ESPACIO_CURRICULAR', 4, '2020-03-01', NULL, id FROM division WHERE anio = 2 AND num_division = 3 AND turno = 'Mañana';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Matemática', 'ESPACIO_CURRICULAR', 5, '2020-03-01', NULL, id FROM division WHERE anio = 2 AND num_division = 3 AND turno = 'Mañana';

-- 2º 4º Mañana (Artística) - ya tiene Arte  
INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Música', 'ESPACIO_CURRICULAR', 3, '2020-03-01', NULL, id FROM division WHERE anio = 2 AND num_division = 4 AND turno = 'Mañana';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Historia del Arte', 'ESPACIO_CURRICULAR', 2, '2020-03-01', NULL, id FROM division WHERE anio = 2 AND num_division = 4 AND turno = 'Mañana';

-- Espacios curriculares para 3º año
-- 3º 1º Tarde (Sociales) - ya tiene Geografía
INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Historia', 'ESPACIO_CURRICULAR', 4, '2020-03-01', NULL, id FROM division WHERE anio = 3 AND num_division = 1 AND turno = 'Tarde';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Economía', 'ESPACIO_CURRICULAR', 3, '2020-03-01', NULL, id FROM division WHERE anio = 3 AND num_division = 1 AND turno = 'Tarde';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Filosofía', 'ESPACIO_CURRICULAR', 2, '2020-03-01', NULL, id FROM division WHERE anio = 3 AND num_division = 1 AND turno = 'Tarde';

-- 3º 2º Tarde (Sociales)
INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Historia', 'ESPACIO_CURRICULAR', 4, '2020-03-01', NULL, id FROM division WHERE anio = 3 AND num_division = 2 AND turno = 'Tarde';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Geografía', 'ESPACIO_CURRICULAR', 3, '2020-03-01', NULL, id FROM division WHERE anio = 3 AND num_division = 2 AND turno = 'Tarde';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Psicología', 'ESPACIO_CURRICULAR', 2, '2020-03-01', NULL, id FROM division WHERE anio = 3 AND num_division = 2 AND turno = 'Tarde';

-- 3º 3º Tarde (Deportiva) - ya tiene Educación Física
INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Anatomía', 'ESPACIO_CURRICULAR', 3, '2020-03-01', NULL, id FROM division WHERE anio = 3 AND num_division = 3 AND turno = 'Tarde';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Nutrición', 'ESPACIO_CURRICULAR', 2, '2020-03-01', NULL, id FROM division WHERE anio = 3 AND num_division = 3 AND turno = 'Tarde';

-- 3º 4º Mañana (Naturales) - ya tiene Química
INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Física', 'ESPACIO_CURRICULAR', 4, '2020-03-01', NULL, id FROM division WHERE anio = 3 AND num_division = 4 AND turno = 'Mañana';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Biología', 'ESPACIO_CURRICULAR', 4, '2020-03-01', NULL, id FROM division WHERE anio = 3 AND num_division = 4 AND turno = 'Mañana';

-- Espacios curriculares para 4º año
-- 4º 1º Tarde (Naturales) - ya tiene Biología
INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Física', 'ESPACIO_CURRICULAR', 4, '2020-03-01', NULL, id FROM division WHERE anio = 4 AND num_division = 1 AND turno = 'Tarde';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Química', 'ESPACIO_CURRICULAR', 4, '2020-03-01', NULL, id FROM division WHERE anio = 4 AND num_division = 1 AND turno = 'Tarde';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Matemática', 'ESPACIO_CURRICULAR', 4, '2020-03-01', NULL, id FROM division WHERE anio = 4 AND num_division = 1 AND turno = 'Tarde';

-- 4º 2º Mañana (Sociales) - nueva división
INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Historia', 'ESPACIO_CURRICULAR', 4, '2020-03-01', NULL, id FROM division WHERE anio = 4 AND num_division = 2 AND turno = 'Mañana';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Geografía', 'ESPACIO_CURRICULAR', 4, '2020-03-01', NULL, id FROM division WHERE anio = 4 AND num_division = 2 AND turno = 'Mañana';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Sociología', 'ESPACIO_CURRICULAR', 3, '2020-03-01', NULL, id FROM division WHERE anio = 4 AND num_division = 2 AND turno = 'Mañana';

-- 4º 3º Mañana (Informática) - ya tiene Tecnología
INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Programación', 'ESPACIO_CURRICULAR', 4, '2020-03-01', NULL, id FROM division WHERE anio = 4 AND num_division = 3 AND turno = 'Mañana';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Sistemas', 'ESPACIO_CURRICULAR', 3, '2020-03-01', NULL, id FROM division WHERE anio = 4 AND num_division = 3 AND turno = 'Mañana';

-- 4º 4º Tarde (Biológicas) - nueva división
INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Biología', 'ESPACIO_CURRICULAR', 5, '2020-03-01', NULL, id FROM division WHERE anio = 4 AND num_division = 4 AND turno = 'Tarde';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Química Orgánica', 'ESPACIO_CURRICULAR', 4, '2020-03-01', NULL, id FROM division WHERE anio = 4 AND num_division = 4 AND turno = 'Tarde';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Genética', 'ESPACIO_CURRICULAR', 3, '2020-03-01', NULL, id FROM division WHERE anio = 4 AND num_division = 4 AND turno = 'Tarde';

-- Espacios curriculares para 5º año
-- 5º 1º Mañana (Sociales) - ya tiene Historia
INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Geografía', 'ESPACIO_CURRICULAR', 4, '2020-03-01', NULL, id FROM division WHERE anio = 5 AND num_division = 1 AND turno = 'Mañana';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Economía Política', 'ESPACIO_CURRICULAR', 3, '2020-03-01', NULL, id FROM division WHERE anio = 5 AND num_division = 1 AND turno = 'Mañana';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Derecho', 'ESPACIO_CURRICULAR', 3, '2020-03-01', NULL, id FROM division WHERE anio = 5 AND num_division = 1 AND turno = 'Mañana';

-- 5º 2º Mañana (Biológicas) - ya tiene Historia
INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Biología Molecular', 'ESPACIO_CURRICULAR', 4, '2020-03-01', NULL, id FROM division WHERE anio = 5 AND num_division = 2 AND turno = 'Mañana';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Ecología', 'ESPACIO_CURRICULAR', 3, '2020-03-01', NULL, id FROM division WHERE anio = 5 AND num_division = 2 AND turno = 'Mañana';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Biotecnología', 'ESPACIO_CURRICULAR', 3, '2020-03-01', NULL, id FROM division WHERE anio = 5 AND num_division = 2 AND turno = 'Mañana';

-- 5º 3º Tarde (Informática) - nueva división
INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Programación Avanzada', 'ESPACIO_CURRICULAR', 5, '2020-03-01', NULL, id FROM division WHERE anio = 5 AND num_division = 3 AND turno = 'Tarde';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Base de Datos', 'ESPACIO_CURRICULAR', 4, '2020-03-01', NULL, id FROM division WHERE anio = 5 AND num_division = 3 AND turno = 'Tarde';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Redes', 'ESPACIO_CURRICULAR', 3, '2020-03-01', NULL, id FROM division WHERE anio = 5 AND num_division = 3 AND turno = 'Tarde';

-- 5º 4º Mañana (Artística) - nueva división
INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Arte Contemporáneo', 'ESPACIO_CURRICULAR', 4, '2020-03-01', NULL, id FROM division WHERE anio = 5 AND num_division = 4 AND turno = 'Mañana';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Historia del Arte', 'ESPACIO_CURRICULAR', 3, '2020-03-01', NULL, id FROM division WHERE anio = 5 AND num_division = 4 AND turno = 'Mañana';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Teoría del Color', 'ESPACIO_CURRICULAR', 3, '2020-03-01', NULL, id FROM division WHERE anio = 5 AND num_division = 4 AND turno = 'Mañana';

-- Espacios curriculares para 6º año (todas nuevas divisiones)
-- 6º 1º Mañana (Naturales)
INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Física Avanzada', 'ESPACIO_CURRICULAR', 5, '2020-03-01', NULL, id FROM division WHERE anio = 6 AND num_division = 1 AND turno = 'Mañana';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Química Analítica', 'ESPACIO_CURRICULAR', 4, '2020-03-01', NULL, id FROM division WHERE anio = 6 AND num_division = 1 AND turno = 'Mañana';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Matemática Superior', 'ESPACIO_CURRICULAR', 4, '2020-03-01', NULL, id FROM division WHERE anio = 6 AND num_division = 1 AND turno = 'Mañana';

-- 6º 2º Tarde (Sociales)
INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Historia Argentina', 'ESPACIO_CURRICULAR', 4, '2020-03-01', NULL, id FROM division WHERE anio = 6 AND num_division = 2 AND turno = 'Tarde';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Geografía Argentina', 'ESPACIO_CURRICULAR', 3, '2020-03-01', NULL, id FROM division WHERE anio = 6 AND num_division = 2 AND turno = 'Tarde';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Formación Ética y Ciudadana', 'ESPACIO_CURRICULAR', 2, '2020-03-01', NULL, id FROM division WHERE anio = 6 AND num_division = 2 AND turno = 'Tarde';

-- 6º 3º Mañana (Biológicas)
INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Microbiología', 'ESPACIO_CURRICULAR', 4, '2020-03-01', NULL, id FROM division WHERE anio = 6 AND num_division = 3 AND turno = 'Mañana';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Bioquímica', 'ESPACIO_CURRICULAR', 4, '2020-03-01', NULL, id FROM division WHERE anio = 6 AND num_division = 3 AND turno = 'Mañana';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Investigación Científica', 'ESPACIO_CURRICULAR', 3, '2020-03-01', NULL, id FROM division WHERE anio = 6 AND num_division = 3 AND turno = 'Mañana';

-- 6º 4º Tarde (Informática)
INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Desarrollo Web', 'ESPACIO_CURRICULAR', 5, '2020-03-01', NULL, id FROM division WHERE anio = 6 AND num_division = 4 AND turno = 'Tarde';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Inteligencia Artificial', 'ESPACIO_CURRICULAR', 4, '2020-03-01', NULL, id FROM division WHERE anio = 6 AND num_division = 4 AND turno = 'Tarde';

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) 
SELECT 'Seguridad Informática', 'ESPACIO_CURRICULAR', 3, '2020-03-01', NULL, id FROM division WHERE anio = 6 AND num_division = 4 AND turno = 'Tarde';
