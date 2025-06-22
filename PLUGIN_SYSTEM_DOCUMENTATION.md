# Sistema de Plugins para Validación de Designaciones

## Introducción

Se ha implementado un sistema de plugins flexible para las validaciones de designaciones utilizando el patrón Plugin con Class Loader dinámico. Este sistema permite:

- ✅ **Desacoplamiento total** del core de validación
- ✅ **Carga dinámica** de validadores sin recompilar
- ✅ **Extensibilidad** sin modificar código existente
- ✅ **Configuración flexible** mediante archivos properties

## Arquitectura del Sistema

### Componentes Principales

```
Core System
├── IDesignacionRule                 # Interfaz común para todos los plugins
├── DesignacionValidator             # Orquestador principal
├── DesignacionRuleFactory           # Fábrica para cargar plugins dinámicamente
└── ServiceLocator                   # Inyector de dependencias para plugins

Plugin System
├── plugins/                         # Directorio de plugins
│   ├── BasicDesignacionRule         # Plugin de validaciones básicas
│   ├── TipoDesignacionDesignacionRule # Plugin de validaciones de tipo
│   ├── ConflictoDesignacionRule     # Plugin de validaciones de conflictos
│   └── FechaLimiteDesignacionRule   # Plugin extensible de ejemplo
└── validation-rules.properties      # Configuración de plugins activos
```

## Convención de Nombres

El sistema utiliza una convención específica para cargar plugins dinámicamente:

- **Nombre de configuración**: `basic`, `tipoDesignacion`, `conflicto`, etc.
- **Nombre de clase**: `[NombreRegla]DesignacionRule`
- **Paquete**: `unpsjb.labprog.backend.business.validaciones.plugins`
- **Método requerido**: `getInstance()` (patrón Singleton)

### Ejemplos de Mapeo

| Configuración | Clase Real |
|---------------|------------|
| `basic` | `BasicDesignacionRule` |
| `tipoDesignacion` | `TipoDesignacionDesignacionRule` |
| `conflicto` | `ConflictoDesignacionRule` |
| `fechaLimite` | `FechaLimiteDesignacionRule` |

## Configuración

### Archivo: `validation-rules.properties`

```properties
# Orden de ejecución de las reglas (separadas por comas)
rules.order=basic,tipoDesignacion,conflicto

# Para agregar una nueva regla, simplemente agregar a la lista:
# rules.order=basic,tipoDesignacion,conflicto,fechaLimite,nuevaRegla
```

## Cómo Crear un Nuevo Plugin

### Paso 1: Crear la Clase Plugin

```java
package unpsjb.labprog.backend.business.validaciones.plugins;

import unpsjb.labprog.backend.business.interfaces.validaciones.IDesignacionRule;
import unpsjb.labprog.backend.model.Designacion;

public class MiNuevoDesignacionRule implements IDesignacionRule {
    
    // Singleton (requerido)
    private static MiNuevoDesignacionRule instance = null;
    
    private MiNuevoDesignacionRule() {}
    
    public static MiNuevoDesignacionRule getInstance() {
        if (instance == null) {
            instance = new MiNuevoDesignacionRule();
        }
        return instance;
    }

    @Override
    public void validate(Designacion designacion) {
        // Implementar lógica de validación
        if (/* condición de falla */) {
            throw new IllegalArgumentException("Mensaje de error");
        }
    }

    @Override
    public String getRuleName() {
        return "Mi Nueva Validación";
    }
}
```

### Paso 2: Agregar a la Configuración

En `validation-rules.properties`:

```properties
# Agregar 'miNuevo' a la lista
rules.order=basic,tipoDesignacion,conflicto,miNuevo
```

### Paso 3: ¡Listo!

El plugin se carga automáticamente sin necesidad de:
- Recompilar el core
- Modificar el `DesignacionValidator`
- Agregar anotaciones Spring
- Modificar otros archivos

## Inyección de Dependencias en Plugins

Para plugins que necesitan servicios de Spring:

### 1. Agregar métodos setter en el plugin:

```java
public class MiPluginConServicios implements IDesignacionRule {
    private IMiServicio miServicio;
    
    // Método para inyectar dependencias
    public void setMiServicio(IMiServicio servicio) {
        this.miServicio = servicio;
    }
    
    // ...resto de la implementación
}
```

### 2. Actualizar la fábrica:

En `DesignacionRuleFactory.injectServices()`:

```java
private void injectServices(IDesignacionRule rule) {
    // ...existing code...
    
    // Inyectar en el nuevo plugin
    if (rule instanceof MiPluginConServicios) {
        MiPluginConServicios plugin = (MiPluginConServicios) rule;
        plugin.setMiServicio(ServiceLocator.getMiServicio());
    }
}
```

### 3. Agregar servicio al ServiceLocator:

```java
public static IMiServicio getMiServicio() {
    try {
        return applicationContext.getBean(IMiServicio.class);
    } catch (Exception e) {
        System.err.println("Error obteniendo MiServicio: " + e.getMessage());
        return null;
    }
}
```

## Ventajas del Sistema

### 🎯 **Flexibilidad Total**
- Agregar/quitar validaciones sin tocar el core
- Cambiar orden de ejecución con configuración
- Activar/desactivar reglas dinámicamente

### 🔧 **Mantenibilidad**
- Cada plugin es independiente
- Testing individual de cada regla
- Debugging más sencillo

### 🚀 **Escalabilidad**
- Nuevos plugins sin impacto en performance
- Sistema preparado para carga en runtime
- Posibilidad de plugins externos

### 📦 **Desacoplamiento**
- Core no conoce plugins específicos
- Plugins no conocen entre sí
- Interfaz clara y estable

## Uso Avanzado

### Recarga Dinámica de Plugins

```java
// Recargar configuración y limpiar cache
designacionValidator.reloadRules();
```

### Verificar Disponibilidad de Plugin

```java
DesignacionRuleFactory factory = DesignacionRuleFactory.getInstance();
if (factory.isRuleAvailable("miNuevaRegla")) {
    // El plugin está disponible
}
```

### Plugin Condicional

```java
@Override
public void validate(Designacion designacion) {
    // Solo validar en ciertas condiciones
    if (shouldValidate(designacion)) {
        // Aplicar validación
    }
}
```

## Migración desde Sistema Anterior

El nuevo sistema mantiene **100% compatibilidad** con la funcionalidad anterior:

1. ✅ **Mismas validaciones**: Todas las reglas anteriores están implementadas
2. ✅ **Mismo orden**: basic → tipoDesignacion → conflicto
3. ✅ **Mismos errores**: Mensajes de error idénticos
4. ✅ **Misma interfaz**: `IDesignacionValidator.validarDesignacion()` sin cambios

## Ejemplo de Extensión Futura

Para agregar validación de presupuesto:

1. **Crear**: `PresupuestoDesignacionRule.java`
2. **Configurar**: Agregar `presupuesto` a `rules.order`  
3. **Deploy**: El plugin se activa automáticamente

¡El sistema está completamente preparado para futuras extensiones sin modificaciones al código base!
