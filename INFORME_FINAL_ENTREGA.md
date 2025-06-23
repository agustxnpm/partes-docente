# Informe Final de Entrega
## Sistema de Gestión de Novedades Docentes - Escuela 775

**Laboratorio de Programación y Lenguajes**  
**Trabajo Práctico Final**  
**Fecha de Entrega:** 23/06/2025

---

## 1. Introducción al Sistema

### 1.1 Problemática Abordada
El sistema desarrollado resuelve la gestión integral de novedades docentes para la Escuela 775 de Puerto Madryn, abarcando:

- **Administración de Personal y Designaciones**: Gestión completa de docentes, cargos y espacios curriculares
- **Gestión de Licencias**: Otorgamiento y validación automatizada según normativas del ministerio
- **Suplencias y Reemplazos**: Control de cobertura temporal de cargos
- **Reportes y Parte Diario**: Generación automática de documentación requerida por el Ministerio de Educación

### 1.2 Arquitectura del Sistema
La solución implementa una **arquitectura en capas** con separación clara de responsabilidades:

```
┌─────────────────────────────────────────┐
│           FRONTEND (Angular)            │
│     - Interfaz de Usuario Reactiva      │
│     - Componentes Modulares             │
│     - Validación en Tiempo Real         │
└─────────────────────────────────────────┘
                    ↓ REST API
┌─────────────────────────────────────────┐
│         BACKEND (Spring Boot)           │
│  ┌───────────────────────────────────┐  │
│  │        CAPA DE SERVICIOS          │  │
│  │   - Lógica de Negocio             │  │
│  │   - Validaciones Dinámicas        │  │
│  │   - Sistema de Plugins            │  │
│  └───────────────────────────────────┘  │
│  ┌───────────────────────────────────┐  │
│  │       CAPA DE PERSISTENCIA        │  │
│  │   - Repositorios JPA              │  │
│  │   - Modelo de Dominio             │  │
│  └───────────────────────────────────┘  │
└─────────────────────────────────────────┘
                    ↓ JPA/Hibernate
┌─────────────────────────────────────────┐
│          BASE DE DATOS                  │
│     - Persistencia de Entidades         │
│     - Integridad Referencial            │
└─────────────────────────────────────────┘
```

---

## 2. Metodologías y Patrones Implementados

### 2.1 Principios SOLID Aplicados

#### **S - Single Responsibility Principle (SRP)**
- **Servicios especializados**: `HorarioService`, `DesignacionService`, `LicenciaService`
- **Controladores específicos**: Cada endpoint maneja una responsabilidad única
- **Componentes Angular**: Separación clara entre lógica de presentación y datos

#### **O - Open/Closed Principle (OCP)**
- **Sistema de Plugins**: Extensible sin modificar código core
- **Validadores dinámicos**: Nuevas reglas sin alterar la lógica existente
- **Interfaces bien definidas**: `IDesignacionRule`, `ILicenciaValidator`

#### **L - Liskov Substitution Principle (LSP)**
- **Jerarquía de entidades**: `Cargo`, `EspacioCurricular` hereda comportamientos base
- **Implementaciones intercambiables**: Plugins de validación polimórficos

#### **I - Interface Segregation Principle (ISP)**
- **Interfaces específicas**: Separación entre `IDesignacionRule` y `ILicenciaValidator`
- **Contratos mínimos**: Cada interfaz expone solo métodos necesarios

#### **D - Dependency Inversion Principle (DIP)**
- **Inyección de dependencias**: Spring IoC Container
- **Inversión de control**: Servicios dependen de abstracciones
- **ServiceLocator**: Para plugins dinámicos

### 2.2 Patrones de Diseño Implementados

#### **Plugin Pattern**
```java
// Interfaz común para reglas especificas
public interface IDesignacionRule {
    void validate(Designacion designacion);
}

// Carga dinámica de plugins
public class DesignacionRuleFactory {
    IDesignacionRule loadRule(String ruleName) {
        // Carga las reglas que existan en validation-rules.properties
    }
}
```

#### **Strategy Pattern**
- **Validaciones dinámicas**: Diferentes estrategias según tipo de licencia
- **Algoritmos intercambiables**: Múltiples implementaciones de `IDesignacionRule`

#### **Factory Pattern**
- **Creación de validadores**: `DesignacionRuleFactory`
- **Instanciación de servicios para plugins**: `ServiceLocator`

#### **Repository Pattern**
- **Abstracción de persistencia**: JPA Repositories
- **Separación datos/lógica**: Separar el acceso a los datos de la logica de negocio

### 2.3 Metodología TDD (Test-Driven Development)

#### **Estructura de Testing**
```
testing/
├── cucumber.js              # Configuración BDD
├── features/                # Especificaciones Gherkin
│   ├── create_cargo.feature
│   ├── nueva_designacion.feature
│   ├── otorgar_licencia.feature
│   └── parte_diario.feature
└── step_definitions/        # Implementación de pasos
    ├── common_steps.js
    ├── nueva_designacion.js
    └── otorgar_licencia.js
```

#### **Ciclo TDD Aplicado**
1. **Red**: Escribir test que falle
2. **Green**: Implementar código mínimo para pasar
3. **Refactor**: Mejorar diseño manteniendo funcionalidad

### 2.4 Metodología BDD (Behavior-Driven Development)

#### **Especificaciones en Lenguaje Natural**
```gherkin
Esquema del escenario: ingresar nuevo cargo institucional
    Dado el cargo institucional cuyo "<nombre>" que da título al mismo
    Y que es del tipo de designación "<tipoDesignación>"
    Y que tiene una carga horaria de <cargaHoraria> horas, con vigencia desde "<fechaDesde>" hasta "<fechaHasta>"
    Y que si el tipo es "ESPACIO CURRICULAR", opcionalmente se asigna a la división "<año>" "<número>" "<orientacion>" "<turno>"
    Cuando se presiona el botón de guardar
    Entonces se espera el siguiente <status> con la "<respuesta>"
```

#### **Ventajas del Enfoque BDD**
- **Comunicación clara**: Especificaciones comprensibles por stakeholders
- **Documentación viva**: Tests que documentan comportamiento esperado
- **Validación de requisitos**: Verificación automática de casos de uso

---

## 3. Sistema de Validaciones Dinámicas

### 3.1 Arquitectura de Plugins

El sistema implementa un **mecanismo de plugins** para validaciones que permite:

#### **Características Principales**
- ✅ **Desacoplamiento total** del core de validación
- ✅ **Carga dinámica** de validadores sin recompilar
- ✅ **Extensibilidad** sin modificar código existente
- ✅ **Configuración flexible** mediante archivos properties

#### **Componentes del Sistema**
```java
// Interfaz común para todos los plugins (mostrando solo los de designación)
public interface IDesignacionRule {
    void validate(Designacion designacion);
}

// Orquestador principal
@Component
public class DesignacionValidator {
    public void validarDesignacion(Designacion designacion) {
        IDesignacionRule rule = ruleFactory.getRule(ruleName);
        // Ejecuta todas las reglas cargadas dinámicamente
    }
}
```

### 3.2 Configuración Dinámica

#### **Archivo: `validation-rules.properties`**
```properties
# Orden de ejecución de las reglas
rules.order=BasicDesignacionRule,TipoDesignacionDesignacionRule,ConflictoDesignacionRule

```

#### **Plugins Implementados**
1. **BasicDesignacionRule**: Validaciones fundamentales (fechas, datos obligatorios)
2. **TipoDesignacionDesignacionRule**: Validaciones específicas por tipo de cargo
3. **ConflictoDesignacionRule**: Detección de conflictos horarios y solapamientos
4. **FinDeSemanaDesignacionRule**: Ejemplo extensible para nuevas validaciones

### 3.3 Ventajas del Sistema de Plugins

#### **Para el Desarrollo**
- **Modularidad**: Cada validación es independiente
- **Mantenibilidad**: Cambios localizados sin afectar el core
- **Testabilidad**: Testing individual de cada plugin

#### **Para el Despliegue**
- **Hot-swapping**: Agregar validaciones sin reiniciar
- **Configuración externa**: Activar/desactivar reglas dinámicamente
- **Versionado independiente**: Evolución por separado de cada plugin

---


## 4. Arquitectura y Tecnologías

### 4.1 Stack Tecnológico

#### **Backend**
- **Framework**: Spring Boot
- **Persistencia**: JPA/Hibernate
- **API**: RESTful services con JSON
- **Testing**: BDD con Cucumber
- **Build**: Maven

#### **Frontend**
- **Framework**: Angular 15+
- **UI Components**: Bootstrap
- **HTTP**: Angular HttpClient
- **Routing**: Angular Router
- **Forms**: Reactive Forms con validaciones

#### **Infraestructura**
- **Contenización**: Docker + Docker Compose
- **Base de Datos**: PostgreSQL

### 4.2 Ventajas de la Arquitectura Elegida

#### **Separación de Responsabilidades**
- **Frontend**: Lógica de presentación y UX
- **Backend**: Lógica de negocio y validaciones
- **Base de Datos**: Persistencia y integridad

---

## 5. Funcionalidades Clave Implementadas

### 5.1 Gestión de Designaciones
- ✅ CRUD completo de personas, cargos y divisiones
- ✅ Validación automática de conflictos
- ✅ Trazabilidad de cambios
- ✅ Interfaz typeahead para selección eficiente

### 5.2 Sistema de Licencias
- ✅ Otorgamiento con validación de artículos ministeriales
- ✅ Cálculo automático de días disponibles
- ✅ Verificación de requisitos por tipo de licencia
- ✅ Gestión de suplencias automática

### 5.3 Reportes y Consultas
- ✅ Parte diario automatizado
- ✅ Mapa de horarios por división
- ✅ Reporte de concepto anual
- ✅ Espacios curriculares sin cobertura

### 5.4 Validaciones Inteligentes
- ✅ Sistema de plugins extensible
- ✅ Validaciones en tiempo real
- ✅ Reglas configurables externamente
- ✅ Mensajes de error descriptivos

---

## 6. Casos de Uso Verificados

### 6.1 Designaciones
- ✅ Alta de personas con validación de duplicados
- ✅ Asignación a cargos disponibles
- ✅ Detección de conflictos horarios
- ✅ Designaciones de suplencia

### 6.2 Licencias
- ✅ Validación por artículos del estatuto docente
- ✅ Cálculo de días según tipo
- ✅ Verificación de topes anuales
- ✅ Licencias con solapamiento detectado

### 6.3 Reportes
- ✅ Parte diario con licencias activas
- ✅ Suplentes asignados correctamente
- ✅ Actualización automática por vencimientos
- ✅ Mapa de horarios actualizado

---

**Fin del Informe**

*Este documento representa el trabajo realizado en el marco del Laboratorio de Programación y Lenguajes, demostrando la aplicación práctica de metodologías de desarrollo modernas, patrones de diseño y arquitecturas escalables en la resolución de problemáticas reales del dominio educativo.*
