# Sistema de Plugins para Validación de Designaciones y Licencias

## Introducción

Se ha implementado un sistema de plugins flexible para las validaciones de **designaciones** y **licencias** utilizando el patrón Plugin con Class Loader dinámico. Este sistema permite:

- ✅ **Desacoplamiento total** del core de validación
- ✅ **Carga dinámica** de validadores sin recompilar
- ✅ **Extensibilidad** sin modificar código existente
- ✅ **Configuración flexible** mediante archivos properties
- ✅ **Doble sistema de validación** para dos dominios diferentes

## Arquitectura del Sistema

### Componentes Principales

#### Sistema de Designaciones
```
Core System
├── IDesignacionRule                 # Interfaz común para todos los plugins
├── DesignacionValidator             # Orquestador principal
├── DesignacionRuleFactory           # Fábrica para cargar plugins dinámicamente
└── ServiceLocator                   # Inyector de dependencias para plugins

Plugin System
├── plugins/designaciones/           # Directorio de plugins de designaciones
    ├── BasicDesignacionRule         # Plugin de validaciones básicas
    ├── TipoDesignacionDesignacionRule # Plugin de validaciones de tipo
    ├── ConflictoDesignacionRule     # Plugin de validaciones de conflictos
    └── FinDeSemanaDesignacionRule   # Plugin extensible de ejemplo
resources
├──validation-rules.properties      # Configuración de plugins activos
```

#### Sistema de Licencias
```
Core System
├── ILicenciaRule                    # Interfaz común para todos los plugins
├── LicenciaValidator                # Orquestador principal
├── LicenciaRuleFactory              # Fábrica para cargar plugins dinámicamente
└── ServiceLocator                   # Inyector de dependencias para plugins

Plugin System
├── plugins/licencias/               # Directorio de plugins de licencias
│   ├── BasicLicenciaRule            # Plugin de validaciones básicas
│   ├── PersonaLicenciaRule          # Plugin de validaciones de persona
│   ├── DesignacionesActivasLicenciaRule # Plugin de validaciones de designaciones
│   ├── SuperposicionLicenciaRule    # Plugin de validaciones de superposición
│   ├── Articulo5ALicenciaRule       # Plugin específico artículo 5A
│   ├── Articulo23ALicenciaRule      # Plugin específico artículo 23A
│   └── Articulo36ALicenciaRule      # Plugin específico artículo 36A

resources
├── validation-rules.properties      # Configuración de plugins activos
└── articulos-licencia.properties    # Configuración de artículos específicos
```

### Archivos de Configuración

#### **Archivo: `validation-rules.properties`**

```properties
# === REGLAS DE DESIGNACIONES ===
# Orden de ejecución de las reglas de designaciones (separadas por comas)
designacion.rules.order=BasicDesignacionRule,TipoDesignacionDesignacionRule,ConflictoDesignacionRule

# === REGLAS DE LICENCIAS ===
# Orden de ejecución de las reglas generales de licencias (separadas por comas)
licencia.rules.order=BasicLicenciaRule,PersonaLicenciaRule,DesignacionesActivasLicenciaRule,SuperposicionLicenciaRule

# === REGLAS DE ARTÍCULOS ESPECÍFICOS ===
# Orden de ejecución de las reglas por artículo de licencia (separadas por comas)
licencia.rules.articulos=Articulo5ALicenciaRule,Articulo23ALicenciaRule,Articulo36ALicenciaRule
```

#### **Archivo: `articulos-licencia.properties`**

```properties
# Configuración de Artículos de Licencia
# Este archivo define los artículos de licencia que serán registrados automáticamente

# Para agregar un nuevo artículo:
# 1. Primero se desarrolla el plugin de validación Articulo[Codigo]LicenciaRule
# 2. Se agrega el plugin a licencia.rules.articulos en validation-rules.properties
# 3. El artículo se registra automáticamente en la base de datos

```

## Cómo Crear un Nuevo Plugin

### Para Designaciones

#### **Paso 1: Crear la Clase Plugin**

```java
package unpsjb.labprog.backend.business.validaciones.plugins.designaciones;

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
        return "Mi Nueva Validación de Designación";
    }
}
```

#### **Paso 2: Agregar a la Configuración**

En `validation-rules.properties`:

```properties
# Agregar 'MiNuevoDesignacionRule' a la lista
designacion.rules.order=BasicDesignacionRule,TipoDesignacionDesignacionRule,ConflictoDesignacionRule,MiNuevoDesignacionRule
```

### Para Licencias

#### **Paso 1: Crear la Clase Plugin**

```java
package unpsjb.labprog.backend.business.validaciones.plugins.licencias;

import unpsjb.labprog.backend.business.interfaces.validaciones.ILicenciaRule;
import unpsjb.labprog.backend.model.Licencia;

public class MiNuevaLicenciaRule implements ILicenciaRule {
    
    // Singleton (requerido)
    private static MiNuevaLicenciaRule instance = null;
    
    private MiNuevaLicenciaRule() {}
    
    public static MiNuevaLicenciaRule getInstance() {
        if (instance == null) {
            instance = new MiNuevaLicenciaRule();
        }
        return instance;
    }

    @Override
    public void validate(Licencia licencia) {
        // Implementar lógica de validación
        if (/* condición de falla */) {
            throw new IllegalArgumentException("Mensaje de error");
        }
    }

    @Override
    public String getRuleName() {
        return "Mi Nueva Validación de Licencia";
    }
}
```

#### **Paso 2: Agregar a la Configuración**

Para **reglas generales** en `validation-rules.properties`:

```properties
# Agregar 'MiNuevaLicenciaRule' a la lista general
licencia.rules.order=BasicLicenciaRule,PersonaLicenciaRule,DesignacionesActivasLicenciaRule,SuperposicionLicenciaRule,MiNuevaLicenciaRule
```

Para **artículos específicos** (ej: Articulo19LicenciaRule):

```properties
# Agregar a la lista de artículos específicos
licencia.rules.articulos=Articulo5ALicenciaRule,Articulo23ALicenciaRule,Articulo36ALicenciaRule,Articulo19LicenciaRule
```

#### **Paso 3: Listo**

Los plugins se cargan automáticamente sin necesidad de:
- Recompilar el core
- Modificar los Validators
- Agregar anotaciones Spring
- Modificar otros archivos

## Inyección de Dependencias en Plugins

### Para Plugins de Designaciones

#### **1. Agregar métodos setter en el plugin:**

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

#### **2. Actualizar la fábrica:**

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

### Para Plugins de Licencias

#### **1. Agregar métodos setter en el plugin:**

```java
public class MiPluginLicenciaConServicios implements ILicenciaRule {
    private ILicenciaService licenciaService;
    private IDesignacionService designacionService;
    
    // Métodos para inyectar dependencias
    public void setLicenciaService(ILicenciaService licenciaService) {
        this.licenciaService = licenciaService;
    }
    
    public void setDesignacionService(IDesignacionService designacionService) {
        this.designacionService = designacionService;
    }
    
    // ...resto de la implementación
}
```

#### **2. Actualizar la fábrica:**

En `LicenciaRuleFactory.injectServices()`:

```java
private void injectServices(ILicenciaRule rule) {
    // Inyección automática para plugins conocidos
    if (rule instanceof Articulo5ALicenciaRule) {
        Articulo5ALicenciaRule plugin = (Articulo5ALicenciaRule) rule;
        plugin.setLicenciaService(ServiceLocator.getLicenciaService());
    }
    
    if (rule instanceof DesignacionesActivasLicenciaRule) {
        DesignacionesActivasLicenciaRule plugin = (DesignacionesActivasLicenciaRule) rule;
        plugin.setDesignacionService(ServiceLocator.getDesignacionService());
    }
    
    // Inyectar en nuevos plugins
    if (rule instanceof MiPluginLicenciaConServicios) {
        MiPluginLicenciaConServicios plugin = (MiPluginLicenciaConServicios) rule;
        plugin.setLicenciaService(ServiceLocator.getLicenciaService());
        plugin.setDesignacionService(ServiceLocator.getDesignacionService());
    }
}
```

#### **3. Agregar servicios al ServiceLocator:**

```java
public static ILicenciaService getLicenciaService() {
    try {
        return applicationContext.getBean(ILicenciaService.class);
    } catch (Exception e) {
        System.err.println("Error obteniendo LicenciaService: " + e.getMessage());
        return null;
    }
}

public static IDesignacionService getDesignacionService() {
    try {
        return applicationContext.getBean(IDesignacionService.class);
    } catch (Exception e) {
        System.err.println("Error obteniendo DesignacionService: " + e.getMessage());
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

### 🚀 **Escalabilidad**
- Nuevos plugins sin impacto en performance
- Sistema preparado para carga en runtime
- Posibilidad de plugins externos

### 📦 **Desacoplamiento**
- Core no conoce plugins específicos
- Plugins no se conocen entre sí

### Configuración Condicional

```properties
# Activar/desactivar reglas específicas comentándolas
designacion.rules.order=BasicDesignacionRule,TipoDesignacionDesignacionRule
# ,ConflictoDesignacionRule  <-- Desactivada temporalmente

# Cambiar orden de ejecución según prioridades
licencia.rules.order=PersonaLicenciaRule,BasicLicenciaRule,DesignacionesActivasLicenciaRule,SuperposicionLicenciaRule
```

## Ejemplos de Extensión Futura

### Para Designaciones

Para agregar validación de presupuesto:

1. **Crear**: `PresupuestoDesignacionRule.java`
2. **Configurar**: Agregar `PresupuestoDesignacionRule` a `designacion.rules.order`
3. **Deploy**: El plugin se activa automáticamente

### Para Licencias

Para agregar un nuevo artículo (ej: Artículo 17 - Licencia Deportiva):

1. **Crear**: `Articulo17LicenciaRule.java`
```java
public class Articulo17LicenciaRule implements ILicenciaRule {
    @Override
    public void validate(Licencia licencia) {
        if (!"17".equals(licencia.getArticuloLicencia().getArticulo())) {
            return;
        }
        
        // Validaciones específicas para licencia deportiva
        validarCompromisoDeportivo(licencia);
        validarLimitesDeportivos(licencia);
    }
}
```

2. **Configurar**: Agregar `Articulo17LicenciaRule` a `licencia.rules.articulos`
3. **Actualizar**: Agregar entrada en `articulos-licencia.properties`:
```properties
articulos.17.descripcion=Licencia por actividad deportiva
```

4. **Deploy**: El plugin y el artículo se registran automáticamente

