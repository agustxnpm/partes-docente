-- Desactiva las restricciones para poder borrar sin conflictos
SET session_replication_role = replica;

-- Borra los datos de todas las tablas
TRUNCATE TABLE
  cargo,
  designacion,
  division,
  horario,
  persona,
  licencia,
  licencia_designacion
RESTART IDENTITY CASCADE;

-- Restaura las restricciones
SET session_replication_role = DEFAULT;
