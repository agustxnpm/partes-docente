-- Script para cargar horarios básicos del sistema
-- Los horarios se distribuyen de 1º hora hasta 8º hora, de lunes a viernes

-- Primero, agregar la columna cargo_id si no existe (para la relación con cargos)
-- ALTER TABLE horario ADD COLUMN cargo_id BIGINT;
-- ALTER TABLE horario ADD CONSTRAINT fk_horario_cargo FOREIGN KEY (cargo_id) REFERENCES cargo(id);

-- Lunes
INSERT INTO horario (dia, hora) VALUES ('LUNES', 1);
INSERT INTO horario (dia, hora) VALUES ('LUNES', 2);
INSERT INTO horario (dia, hora) VALUES ('LUNES', 3);
INSERT INTO horario (dia, hora) VALUES ('LUNES', 4);
INSERT INTO horario (dia, hora) VALUES ('LUNES', 5);
INSERT INTO horario (dia, hora) VALUES ('LUNES', 6);
INSERT INTO horario (dia, hora) VALUES ('LUNES', 7);
INSERT INTO horario (dia, hora) VALUES ('LUNES', 8);

-- Martes
INSERT INTO horario (dia, hora) VALUES ('MARTES', 1);
INSERT INTO horario (dia, hora) VALUES ('MARTES', 2);
INSERT INTO horario (dia, hora) VALUES ('MARTES', 3);
INSERT INTO horario (dia, hora) VALUES ('MARTES', 4);
INSERT INTO horario (dia, hora) VALUES ('MARTES', 5);
INSERT INTO horario (dia, hora) VALUES ('MARTES', 6);
INSERT INTO horario (dia, hora) VALUES ('MARTES', 7);
INSERT INTO horario (dia, hora) VALUES ('MARTES', 8);

-- Miércoles
INSERT INTO horario (dia, hora) VALUES ('MIERCOLES', 1);
INSERT INTO horario (dia, hora) VALUES ('MIERCOLES', 2);
INSERT INTO horario (dia, hora) VALUES ('MIERCOLES', 3);
INSERT INTO horario (dia, hora) VALUES ('MIERCOLES', 4);
INSERT INTO horario (dia, hora) VALUES ('MIERCOLES', 5);
INSERT INTO horario (dia, hora) VALUES ('MIERCOLES', 6);
INSERT INTO horario (dia, hora) VALUES ('MIERCOLES', 7);
INSERT INTO horario (dia, hora) VALUES ('MIERCOLES', 8);

-- Jueves
INSERT INTO horario (dia, hora) VALUES ('JUEVES', 1);
INSERT INTO horario (dia, hora) VALUES ('JUEVES', 2);
INSERT INTO horario (dia, hora) VALUES ('JUEVES', 3);
INSERT INTO horario (dia, hora) VALUES ('JUEVES', 4);
INSERT INTO horario (dia, hora) VALUES ('JUEVES', 5);
INSERT INTO horario (dia, hora) VALUES ('JUEVES', 6);
INSERT INTO horario (dia, hora) VALUES ('JUEVES', 7);
INSERT INTO horario (dia, hora) VALUES ('JUEVES', 8);

-- Viernes
INSERT INTO horario (dia, hora) VALUES ('VIERNES', 1);
INSERT INTO horario (dia, hora) VALUES ('VIERNES', 2);
INSERT INTO horario (dia, hora) VALUES ('VIERNES', 3);
INSERT INTO horario (dia, hora) VALUES ('VIERNES', 4);
INSERT INTO horario (dia, hora) VALUES ('VIERNES', 5);
INSERT INTO horario (dia, hora) VALUES ('VIERNES', 6);
INSERT INTO horario (dia, hora) VALUES ('VIERNES', 7);
INSERT INTO horario (dia, hora) VALUES ('VIERNES', 8);
