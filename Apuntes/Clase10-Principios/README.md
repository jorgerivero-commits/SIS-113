# Clase 10 — Principios de diseño

Ocho principios, cada uno en su carpeta, con un ejemplo **malo** y uno **bueno** que hacen lo mismo. Todos se ejecutan desde la terminal, sin compilar aparte salvo las dos carpetas con varios archivos. La guía de la clase está en [Clase10-Guia.md](../Clase10-Guia.md).

| # | Principio | Ejemplo | En una frase |
|---|---|---|---|
| 1 | [Cohesión y SRP](01-cohesion-srp/) | registro de usuarios | una clase, una razón para cambiar |
| 2 | [Encapsulamiento y Abstracción](02-encapsulation-abstraction/) | cuenta bancaria | el dato tiene dueño; el cliente ve el qué, no el cómo |
| 3 | [Bajo acoplamiento y Modularidad](03-coupling-modularity/) | notificaciones de pedidos | depender de interfaces y recibirlas desde afuera |
| 4 | [Reusabilidad y Extensibilidad](04-reusability-extensibility/) | reportes en varios formatos | agregar sin modificar: Abierto/Cerrado |
| 5 | [Portabilidad](05-portability/) | guardar archivos | sin rutas fijas ni comandos de un solo sistema |
| 6 | [Defensividad](06-defensibility/) | pagos con tarjeta | fallar rápido, valores seguros, mínimo privilegio |
| 7 | [Mantenibilidad y Testabilidad](07-maintainability-testability/) | calculadora con descuento | funciones puras: lo que no depende del entorno se puede probar |
| 8 | [Simplicidad: KISS, DRY, YAGNI](08-simplicity/) | utilidades de texto | lo simple, sin repetir, y solo lo necesario |

Cada README termina con una sección **En Sentinel**, que muestra dónde aplica la Fase 3 ese principio, o dónde no lo aplica todavía y cómo arreglarlo.

## Cómo correr un ejemplo

```bash
cd Apuntes/Clase10-Principios/01-cohesion-srp
java BadUserManager.java
java GoodUserManager.java
```

Las carpetas `03-coupling-modularity/good-notification/` y `07-maintainability-testability/good-calculator/` tienen varios archivos y se compilan con `javac -d out *.java`. Su README tiene los comandos.
