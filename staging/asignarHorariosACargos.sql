-- Script para asignar horarios a cargos (ejemplos de testing)
-- Esto crea la relación entre cargos y horarios para poder visualizar el mapa
-- La relación se maneja mediante la columna cargo_id en la tabla horario

-- Primero, necesitamos actualizar algunos horarios existentes para asignarlos a cargos
-- Ejemplo de asignaciones para Matemática 1º 1º MAÑANA (supongamos que tiene ID 1)

-- Matemática en 1º 1º turno MAÑANA: Lunes 1º hora, Miércoles 2º hora, Viernes 3º hora
UPDATE horario SET cargo_id = 1 WHERE dia = 'LUNES' AND hora = 1;
UPDATE horario SET cargo_id = 1 WHERE dia = 'MIERCOLES' AND hora = 2;  
UPDATE horario SET cargo_id = 1 WHERE dia = 'VIERNES' AND hora = 3;

-- Ejemplo de asignaciones para Lengua 1º 1º MAÑANA (supongamos que tiene ID 2)
-- Lengua en 1º 1º turno MAÑANA: Martes 1º hora, Jueves 2º hora, Viernes 1º hora
UPDATE horario SET cargo_id = 2 WHERE dia = 'MARTES' AND hora = 1;
UPDATE horario SET cargo_id = 2 WHERE dia = 'JUEVES' AND hora = 2;
UPDATE horario SET cargo_id = 2 WHERE dia = 'VIERNES' AND hora = 1;

-- Ejemplo de asignaciones para Historia 1º 1º MAÑANA (supongamos que tiene ID 3)
-- Historia en 1º 1º turno MAÑANA: Lunes 3º hora, Miércoles 4º hora
UPDATE horario SET cargo_id = 3 WHERE dia = 'LUNES' AND hora = 3;
UPDATE horario SET cargo_id = 3 WHERE dia = 'MIERCOLES' AND hora = 4;

-- Ejemplo de asignaciones para Geografía 1º 1º MAÑANA (supongamos que tiene ID 4)  
-- Geografía en 1º 1º turno MAÑANA: Martes 3º hora, Jueves 4º hora
UPDATE horario SET cargo_id = 4 WHERE dia = 'MARTES' AND hora = 3;
UPDATE horario SET cargo_id = 4 WHERE dia = 'JUEVES' AND hora = 4;

-- Ejemplo de asignaciones para Biología 1º 1º MAÑANA (supongamos que tiene ID 5)
-- Biología en 1º 1º turno MAÑANA: Lunes 2º hora, Miércoles 1º hora, Viernes 2º hora
UPDATE horario SET cargo_id = 5 WHERE dia = 'LUNES' AND hora = 2;
UPDATE horario SET cargo_id = 5 WHERE dia = 'MIERCOLES' AND hora = 1;
UPDATE horario SET cargo_id = 5 WHERE dia = 'VIERNES' AND hora = 2;

-- NOTA: Este script asume que ya existen cargos con esos IDs
-- Para un caso real, deberías ajustar los IDs según los cargos existentes en tu base de datos

-- Ejemplo alternativo usando nombres y subqueries para encontrar los IDs correctos:
-- UPDATE horario SET cargo_id = (
--     SELECT c.id FROM cargo c 
--     JOIN division d ON c.division_id = d.id 
--     WHERE c.nombre = 'Matemática' 
--       AND d.anio = 1 AND d.num_division = 1 AND d.turno = 'MAÑANA'
-- ) WHERE dia = 'LUNES' AND hora = 1;
