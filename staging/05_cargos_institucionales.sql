-- Script para agregar cargos institucionales adicionales
-- Los tests ya tienen: Vicedirector/a, Preceptor/a, Auxiliar ADM
-- Agregamos más cargos necesarios para una escuela

INSERT INTO cargo (nombre, tipo_designacion, carga_horaria, fecha_inicio, fecha_fin, division_id) VALUES 
('Director/a', 'CARGO', 40, '2020-03-01', NULL, NULL),
('Secretario/a', 'CARGO', 36, '2020-03-01', NULL, NULL),
('Bibliotecario/a', 'CARGO', 30, '2020-03-01', NULL, NULL),
('Coordinador/a Pedagógico/a', 'CARGO', 25, '2020-03-01', NULL, NULL),
('Auxiliar de Laboratorio', 'CARGO', 20, '2020-03-01', NULL, NULL),
('Portero/a', 'CARGO', 35, '2020-03-01', NULL, NULL);
