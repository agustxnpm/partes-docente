-- Primero eliminar registros de tablas con foreign keys
TRUNCATE TABLE designacion CASCADE;
-- TRUNCATE TABLE licencia CASCADE;
TRUNCATE TABLE horario CASCADE;
TRUNCATE TABLE cargo CASCADE;
TRUNCATE TABLE division CASCADE;
TRUNCATE TABLE persona CASCADE;
-- TRUNCATE TABLE articulo_licencia CASCADE;

-- Reiniciar las secuencias
ALTER SEQUENCE designacion_id_seq RESTART WITH 1;
-- ALTER SEQUENCE licencia_id_seq RESTART WITH 1;
ALTER SEQUENCE horario_id_seq RESTART WITH 1;
ALTER SEQUENCE cargo_id_seq RESTART WITH 1;
ALTER SEQUENCE division_id_seq RESTART WITH 1;
ALTER SEQUENCE persona_id_seq RESTART WITH 1;
-- ALTER SEQUENCE articulo_licencia_id_seq RESTART WITH 1;