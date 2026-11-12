# 4. Reusabilidad y Extensibilidad

## Principio

**Reusabilidad:** el código se puede usar en varios contextos sin modificarlo.
**Extensibilidad:** se agregan funciones nuevas **sin modificar** el código existente. Es el **Principio Abierto/Cerrado** (Open/Closed): abierto a extensión, cerrado a modificación.

> Analogía: una casa con enchufes. Para usar una licuadora nueva la enchufas; no llamas a un electricista para recablear la cocina.

## Ejemplo malo — [BadReport.java](BadReport.java)

- Un método con un `if`/`else` por formato: texto, CSV, HTML.
- Para agregar JSON hay que **abrir y editar** `generate()`, el mismo método que ya funcionaba para los otros tres.
- Nada se reusa: la lógica del CSV solo existe dentro de su `if`.
- La demo pide JSON y responde `unsupported format`.

## Ejemplo bueno — [GoodReport.java](GoodReport.java)

- Una interfaz `ReportFormatter` y una clase por formato.
- `JsonFormatter` se agregó **después**, como clase nueva, sin editar ninguna línea de las otras.
- `CsvFormatter` se reusa para dos listas distintas: ventas y devoluciones.
- Es el **patrón Strategy**: el "cómo" es un objeto que se pasa como parámetro.

## Demo

```bash
java BadReport.java
java GoodReport.java
```

## En Sentinel Fase 3

- [Renderer](../../../sentinel/phase3-java/src/sentinel/render/Renderer.java) es exactamente `ReportFormatter`: `ConsoleRenderer`, `PlainRenderer` y `CsvRenderer` son las estrategias.
- Una regla nueva es una clase nueva que extiende [Rule](../../../sentinel/phase3-java/src/sentinel/rules/Rule.java). `Sentinel` y los renderers no se tocan.
- **Reuso:** [SustainedRule](../../../sentinel/phase3-java/src/sentinel/rules/SustainedRule.java) funciona con cualquier regla, incluso con las que todavía no existen.
- **La excepción honesta:** [Factory](../../../sentinel/phase3-java/src/sentinel/app/Factory.java) sí se modifica, una línea por clase nueva. Algún lugar tiene que traducir el texto `--render=json` a un objeto. El principio no dice "nunca se toca nada", dice que **el núcleo** no se toca.

## Pregunta para la clase

¿Cuántas líneas de `BadReport` cambian para agregar JSON? ¿Cuántas de `GoodReport`? ¿Y cuántas de Sentinel para agregar un `JsonRenderer`?
