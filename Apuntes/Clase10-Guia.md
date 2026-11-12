# Clase 10

**SIS-113 Programación II**

> Después de la POO, los criterios. Clases, interfaces y herencia son **herramientas**. Esta clase trata de **cuándo y cómo** usarlas bien: ocho principios de diseño, cada uno con un ejemplo malo y uno bueno. Es la base de Clean Architecture, que viene después.

Material: [Clase10-Principios/](Clase10-Principios/), una carpeta por principio.

---

## Objetivo de la sesión

Al terminar la clase, cada estudiante:

1. Nombra los ocho principios y explica cada uno con una frase propia y un ejemplo
2. Reconoce en un código malo **qué principio se viola** y qué dolor concreto causa
3. Explica qué cambió en el código bueno para arreglarlo
4. Señala en Sentinel Fase 3 dónde se aplica cada principio, y dónde todavía no
5. Entiende que los principios a veces **se tensionan entre sí**, como YAGNI y Abierto/Cerrado, y que elegir es parte del diseño

---

## Reglas de la sesión

- **Primero la idea, después el código.** Cada principio empieza con una analogía, sin pantalla.
- **Predecir antes de ejecutar.** Antes de correr el ejemplo malo, alguien dice qué problema va a mostrar.
- **Sin IA.** Hoy no se escribe código nuevo: se lee, se ejecuta y se discute.

---

## Desarrollo

### Parte 1 — ¿Para qué sirven los principios? (10 min)

Dos archivos en pantalla, uno al lado del otro:

- [procedural/Sentinel.java](../sentinel/phase2-java/procedural/Sentinel.java), de la Clase 5
- [phase3-java/](../sentinel/phase3-java/), de las Clases 8 y 9

Pregunta: **los dos funcionan**. ¿Cuál preferirían cambiar mañana, y por qué?

Las respuestas van a sonar así: "el segundo, porque cada cosa está en su lugar", "porque puedo agregar una regla sin tocar lo demás", "porque se puede probar". Cada una de esas frases es un principio. Hoy les ponemos nombre.

Un principio **no es una regla de compilador**: el código malo de hoy compila y corre. Es un criterio para que el código siga siendo fácil de cambiar cuando crezca.

### Cómo se trabaja cada principio (≈12 min cada uno)

Los ocho siguen el mismo ritmo:

1. **La analogía**, sin código. Está al inicio de cada README.
2. **Predecir:** se abre el ejemplo malo y alguien dice qué problema va a mostrar.
3. **Ejecutar el malo** y ver el dolor en la salida.
4. **Ejecutar el bueno** y señalar qué cambió.
5. **La pregunta** del final del README.

### Bloque A — Diseñar una clase

**[1. Cohesión y SRP](Clase10-Principios/01-cohesion-srp/)**: una clase, una razón para cambiar.
Analogía: la navaja suiza contra la caja de herramientas.
El malo hace cuatro cosas en un método: validar, guardar, mandar un correo y reportar. El bueno las separa, y al final prueba el validador solo, sin base de datos ni correo.

**[2. Encapsulamiento y Abstracción](Clase10-Principios/02-encapsulation-abstraction/)**: repaso rápido de la Clase 6, ahora con nombre de principio de diseño.
Analogía: el cajero automático.
El malo termina con saldo negativo y un historial con un depósito falso de un millón, y nada lo impidió.

### Bloque B — Relacionar clases

**[3. Bajo acoplamiento](Clase10-Principios/03-coupling-modularity/)**: depender de interfaces y recibirlas desde afuera.
Analogía: el cargador soldado contra el puerto USB-C.
Es la primera vez que aparece el nombre **inyección de dependencias**. Ya la usaron sin saberlo: `Order.totalWith(PaymentMethod)` en la Clase 9. El `FakeNotifier` del chequeo es la razón práctica: probar sin mandar correos.

**[4. Abierto/Cerrado](Clase10-Principios/04-reusability-extensibility/)**: agregar sin modificar.
Analogía: los enchufes de una casa.
El malo responde `unsupported format: json`. En el bueno, `JsonFormatter` es una clase nueva y nada más cambia. Es el patrón **Strategy**, que ya conocen: los renderers de Sentinel.

### Bloque C — Sobrevivir al mundo real

**[5. Portabilidad](Clase10-Principios/05-portability/)**: sin rutas fijas ni comandos de un solo sistema.
Analogía: la receta que dice "mi horno, perilla 3".
Correr el malo en WSL: falla en las dos operaciones. Pregunta para el grupo: ¿en cuántas computadoras de esta aula funcionaría?

**[6. Defensividad](Clase10-Principios/06-defensibility/)**: fallar rápido, valores seguros, mínimo privilegio.
Analogía: el cajero que revisa el billete antes de dar el cambio.
El malo termina con la caja en **Bs -1350** y sin un solo error. El bueno rechaza cinco operaciones con mensajes claros.

### Bloque D — Vivir con el código

**[7. Testabilidad](Clase10-Principios/07-maintainability-testability/)**: funciones puras, el entorno como parámetro.
Analogía: probar los frenos en el taller y no en la bajada.
El malo lee la fecha de hoy adentro del cálculo: el descuento del martes solo se puede probar un martes. El bueno recibe el día como parámetro y lo prueba en los siete días.

**[8. KISS, DRY, YAGNI](Clase10-Principios/08-simplicity/)**: lo simple, sin repetir, y solo lo necesario.
Analogía: la grúa para mover una silla.
Los dos imprimen lo mismo. Comparar el tamaño de los archivos.

> **Si el tiempo no alcanza:** el Bloque C es el más independiente de los otros. Puede quedar como lectura de la tarea.

### Parte final — Sentinel bajo la lupa (10 min)

Cada README tiene una sección **En Sentinel**. La tabla completa, para la pizarra:

| Principio | Dónde lo aplica la Fase 3 | Deuda pendiente |
|---|---|---|
| SRP | un paquete por responsabilidad; `Config`, `Factory`, `Sentinel` y los renderers tienen una razón de cambio cada uno | — |
| Encapsulamiento | `Snapshot` inmutable, `History` privada, el motor copia la lista de reglas | — |
| Bajo acoplamiento | el motor recibe `Collector`, `Rule` y `Renderer` por el constructor | — |
| Abierto/Cerrado | una regla o una salida nueva es una clase nueva | `Factory` cambia una línea, a propósito |
| Portabilidad | `Path`, UTF-8 explícito, el colector Linux aislado tras una interfaz | `SentinelCheck` tiene escrita la ruta de las muestras |
| Defensividad | `Config` falla antes de empezar, `finally` cierra el colector, valores seguros | — |
| Testabilidad | `ReplayCollector` es el "día como parámetro"; 46 pruebas sin Linux | — |
| KISS / DRY / YAGNI | `PercentRule` evita repetir; sin plugins ni configuración que nadie pidió | `ConsoleRenderer` repite un bucle dos veces |

Las dos deudas son reales y quedan como tarea. **Ningún proyecto cumple todos los principios todo el tiempo.** Lo importante es saber dónde está la deuda y por qué.

### Cierre — Hacia Clean Architecture (5 min)

Lo que hoy se hizo con clases, Clean Architecture lo hace con capas enteras. La idea central ya está en Sentinel: el **núcleo**, el motor y las reglas, no depende de los **detalles**, como la terminal, el archivo CSV o `/proc`. Los detalles dependen del núcleo, a través de interfaces.

Pregunta para dejar abierta: si mañana Sentinel tuviera una página web en lugar de la terminal, ¿qué clases cambiarían? Si la respuesta es "solo agregar un renderer nuevo", la arquitectura ya está bien.

---

## Verificación de salida

Antes de irse, cada estudiante responde en una hoja:

- [ ] Elige tres principios y da un ejemplo propio de cada uno, que no sea de los de hoy
- [ ] Mira este método y dice qué principios viola:
  ```java
  void process(String file) {
      String text = readFile("C:/Users/admin/" + file);
      if (LocalDate.now().getDayOfWeek() == DayOfWeek.FRIDAY) { /* ... */ }
      new EmailSender().send("boss@company.com", text);
  }
  ```
  Respuesta esperada: portabilidad, por la ruta fija; testabilidad, porque lee el día de hoy adentro; acoplamiento, por el `new` interno.
- [ ] Explica con sus palabras por qué YAGNI y Abierto/Cerrado pueden chocar

---

## Tarea para la casa

En `bitacora-terminal/java/clase10/`, con commits por parte.

### Parte 1 — Los principios en tu propio código

Sobre lo que ya escribiste: Biblioteca, Cine o Estacionamiento.

1. En `investigacion/principios.md`, una tabla con al menos **cinco** de los ocho principios:

   | Principio | Dónde lo violé (archivo y método) | Qué dolor causa | Cómo lo arreglaría |
   |---|---|---|---|

2. Arreglar **dos** de esas violaciones. Un commit antes y uno después de cada arreglo, para que el `diff` muestre el cambio.

### Parte 2 — Pagar las deudas de Sentinel

En la copia propia de `phase3-java`:

1. **DRY:** en `ConsoleRenderer`, `describeAll()` y `namesOf()` repiten el mismo bucle. Reemplazarlos por un solo método.
2. **Portabilidad:** que `SentinelCheck` lea la carpeta de muestras de la variable de entorno `SENTINEL_SAMPLES`, con la ruta actual como valor por defecto. El README del principio 5 tiene un esqueleto.
3. Después de cada cambio, `./build.sh` tiene que seguir dando **46/46**. Esa es la prueba de que arreglaste sin romper.

### Parte 3 — Una pregunta escrita

En `investigacion/yagni-vs-ocp.md`, media página: ¿cuándo conviene preparar el código para extenderse, y cuándo es sobre-diseño? Dar un ejemplo de cada caso, tomado de Sentinel o de su propio código.

### Entrega

- Link al repositorio actualizado, antes de la próxima clase.
- Commits por parte; un solo commit final es una tarea incompleta.

> **Regla de IA:** verde para entender un principio o pedir un ejemplo distinto. Los arreglos de las Partes 1 y 2 se hacen a mano. En la próxima clase, cualquier estudiante puede ser invitado a explicar uno de sus arreglos y el principio que aplicó.

---

## Referencias

*(contenido en inglés — leer documentación técnica en inglés es parte del oficio)*

| Recurso | Descripción |
|---------|-------------|
| [Refactoring Guru — Strategy](https://refactoring.guru/design-patterns/strategy) | El patrón del principio 4, con diagramas y ejemplos en Java. |
| [Refactoring Guru — Code Smells](https://refactoring.guru/refactoring/smells) | El catálogo de "olores" del código malo: casi todos son un principio violado. |
| [Martin Fowler — Yagni](https://martinfowler.com/bliki/Yagni.html) | El artículo de referencia sobre YAGNI, incluida su relación con el diseño extensible. |
| [Martin Fowler — Inversion of Control and Dependency Injection](https://martinfowler.com/articles/injection.html) | El texto clásico sobre inyección de dependencias, el corazón del principio 3. |
| [Robert C. Martin — The Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html) | El artículo que da nombre a lo que viene después de esta clase. |
