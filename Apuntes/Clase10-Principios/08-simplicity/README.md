# 8. Simplicidad: KISS, DRY, YAGNI

## Principios

**KISS** (*Keep It Simple, Stupid*): preferir la solución simple a la compleja.
**DRY** (*Don't Repeat Yourself*): no duplicar código.
**YAGNI** (*You Aren't Gonna Need It*): no agregar funciones hasta que hagan falta.

Los tres sirven para no sobre-diseñar.

> Analogía: para mover una silla no se alquila una grúa (KISS). Si cada vez que cocinas escribes la receta del arroz desde cero, tarde o temprano una versión tendrá un error (DRY). Y no se compra un horno industrial "por si algún día abro un restaurante" (YAGNI).

## Ejemplo malo — [BadStringUtils.java](BadStringUtils.java)

La tarea: poner en mayúscula la primera letra de nombres y ciudades.

- **Contra KISS:** una interfaz, una clase abstracta, una fábrica y un *pipeline* para capitalizar una palabra.
- **Contra DRY:** `formatName()` y `formatCity()` son el mismo código, dos veces.
- **Contra YAGNI:** `leetSpeak()`, `reverseWords()` y una bandera `enabled` que nadie pidió y nadie usa.

## Ejemplo bueno — [GoodStringUtils.java](GoodStringUtils.java)

- Dos métodos simples.
- Nombres y ciudades usan **el mismo** método.
- Solo lo que se necesita hoy. Cuando alguien necesite invertir palabras, se escribe ese día, para esa necesidad real.

## Demo

```bash
java BadStringUtils.java
java GoodStringUtils.java
```

Las dos imprimen exactamente lo mismo. Comparen el tamaño de los archivos.

## En Sentinel Fase 3

- **DRY aplicado:** [PercentRule](../../../sentinel/phase3-java/src/sentinel/rules/PercentRule.java) existe para que `MemoryRule` y `CpuRule` no repitan la validación 1..100 ni la comparación con el umbral.
- **DRY violado, como ejercicio:** en [ConsoleRenderer](../../../sentinel/phase3-java/src/sentinel/render/ConsoleRenderer.java), `describeAll()` y `namesOf()` son casi el mismo bucle, y [CsvRenderer](../../../sentinel/phase3-java/src/sentinel/render/CsvRenderer.java) repite uno parecido en `show()`. Extraer un solo método que reciba el separador y diga si quiere el nombre o la descripción.
- **YAGNI aplicado:** Sentinel no tiene archivo de configuración, ni reglas que se cargan como plugins, ni salida JSON. Son ideas razonables, pero nadie las necesita todavía.
- **KISS, la tensión honesta:** la Fase 2 tenía 6 archivos y la Fase 3 tiene 21. Para un programa que nunca va a cambiar, eso sería sobre-diseño. Se justifica porque Sentinel **sí** crece: cada clase nueva de las Clases 8 y 9 entró sin tocar el motor. **La simplicidad se mide contra el problema real, no contra el número de archivos.**

## Pregunta para la clase

YAGNI dice "no lo agregues hasta que haga falta". Abierto/Cerrado dice "diseña para que se pueda extender". ¿Se contradicen? ¿Cuándo gana cada uno?
