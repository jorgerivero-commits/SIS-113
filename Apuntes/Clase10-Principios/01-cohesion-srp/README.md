# 1. Cohesión y Principio de Responsabilidad Única (SRP)

## Principio

**SRP:** una clase debe tener **una, y solo una, razón para cambiar**.
**Cohesión:** qué tan relacionadas y enfocadas están las responsabilidades de un módulo.

> Analogía: una navaja suiza hace de todo, y si se rompe la tijera, se va al taller entera. Una caja de herramientas tiene una herramienta por trabajo: cambias el destornillador sin tocar el martillo.

## Ejemplo malo — [BadUserManager.java](BadUserManager.java)

- `UserManager` valida, guarda en la base de datos, envía correos y genera reportes.
- **Baja cohesión:** cuatro responsabilidades sin relación, mezcladas en un método.
- Cuatro razones para cambiar: las reglas, la base de datos, el proveedor de correo, el formato del reporte.
- No se puede probar la validación sola: cada llamada también guarda y manda un correo.

## Ejemplo bueno — [GoodUserManager.java](GoodUserManager.java)

- Cinco clases pequeñas, cada una con **una** razón para cambiar: `UserValidator`, `UserRepository`, `EmailService`, `UserReport` y `RegistrationService`, que solo coordina el orden de los pasos.
- **Alta cohesión:** lo que va junto está junto.
- Cada pieza funciona sola. Al final, el validador se prueba sin base de datos ni correo.

## Demo

```bash
java BadUserManager.java
java GoodUserManager.java
```

Las dos imprimen lo mismo. La diferencia está en las últimas líneas: el bueno puede usar el validador solo.

## En Sentinel

**Lo malo ya lo vivieron:** [procedural/Sentinel.java](../../../sentinel/phase2-java/procedural/Sentinel.java), de la Clase 5. Un solo `main` que parsea el JSON, valida, decide alertas, imprime y lleva el resumen. Cinco razones para cambiar en un archivo.

**Lo bueno, en la Fase 3:** un paquete por responsabilidad, y una clase por razón de cambio.

| Clase | Su única razón para cambiar |
|---|---|
| [Config](../../../sentinel/phase3-java/src/sentinel/app/Config.java) | las opciones de la línea de comandos |
| [Factory](../../../sentinel/phase3-java/src/sentinel/app/Factory.java) | qué clases concretas existen |
| [Sentinel](../../../sentinel/phase3-java/src/sentinel/app/Sentinel.java) | el orden del ciclo: leer, parsear, evaluar, mostrar |
| [Contract](../../../sentinel/phase3-java/src/sentinel/model/Contract.java) | el formato de la línea JSON |
| [ConsoleRenderer](../../../sentinel/phase3-java/src/sentinel/render/ConsoleRenderer.java) | cómo se ve la salida en la terminal |

## Pregunta para la clase

Si mañana el contrato JSON gana un campo `hostname`, ¿qué clases de la Fase 3 cambian? ¿Y en la versión procedural?
