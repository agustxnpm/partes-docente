-- Script para agregar divisiones adicionales
-- Evitando conflictos con las ya existentes en los tests

-- Divisiones adicionales para 1º año 
INSERT INTO division (anio, num_division, orientacion, turno) VALUES 
(1, 4, 'Naturales', 'Mañana');

-- Divisiones adicionales para 2º año 
INSERT INTO division (anio, num_division, orientacion, turno) VALUES 
(2, 2, 'Sociales', 'Tarde');


-- Divisiones adicionales para 4º año 
INSERT INTO division (anio, num_division, orientacion, turno) VALUES 
(4, 2, 'Sociales', 'Mañana'),
(4, 4, 'Biológicas', 'Tarde');

-- Divisiones adicionales para 5º año 
INSERT INTO division (anio, num_division, orientacion, turno) VALUES 
(5, 3, 'Informática', 'Tarde'),
(5, 4, 'Artística', 'Mañana');

-- Divisiones adicionales para 6º año (todas nuevas)
INSERT INTO division (anio, num_division, orientacion, turno) VALUES 
(6, 1, 'Naturales', 'Mañana'),
(6, 2, 'Sociales', 'Tarde'),
(6, 3, 'Biológicas', 'Mañana'),
(6, 4, 'Informática', 'Tarde');
