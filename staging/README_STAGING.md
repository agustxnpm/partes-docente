# Scripts de Staging para Sistema de Partes Docente

Este conjunto de scripts SQL está diseñado para crear un dataset completo de pruebas para el sistema de partes docente.

## Orden de Ejecución

### Prerequisitos
1. Base de datos creada y configurada
2. Tests de Cucumber ejecutados completamente:
   - `create_persona.feature` (22 personas)
   - `create_division.feature` (14 divisiones)
   - `create_cargo.feature` (cargos básicos)
   - `nueva_designacion.feature` (designaciones básicas)
   - `otorgar_licencia.feature` (licencias básicas)

### Ejecución Manual  (copiar y pegar en terminal)
```bash
./lpl staging 01_cleanup &&                           # limpiar la base de datos
./lpl staging 02_cargarArticulosLicencia &&            # cargar los articulos 5A, 36A y 23A para no tener conflictos con los test
./lpl test &&                                          # correr los test 

./lpl staging 03_personas_adicionales &&              # 20 personas adicionales
./lpl staging 04_divisiones_adicionales &&            # Divisiones faltantes
./lpl staging 05_cargos_institucionales &&            # 6 cargos institucionales
./lpl staging 06_espacios_curriculares &&             # Espacios curriculares
./lpl staging 07_designaciones &&                     # Designaciones de todos los años
./lpl staging 08_cargarHorarios                       # cargar los horarios con asignaciones directas a espacios curriculares


