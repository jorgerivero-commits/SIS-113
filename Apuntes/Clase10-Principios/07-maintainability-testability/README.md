# 7. Mantenibilidad y Testabilidad

## Principio

**Mantenibilidad:** el código es fácil de entender, modificar y depurar.
**Testabilidad:** el código es fácil de probar por partes, de forma aislada.

Aspectos clave:

- Estructura y nombres claros.
- Funciones **puras** cuando se puede: misma entrada, misma salida, sin efectos secundarios.
- Inyección de dependencias para poder probar.
- Separar el cálculo de la impresión y de la lectura del entorno.

> Analogía: los frenos de un auto se prueban en el taller, con una máquina, cuando quieras. No esperas a la próxima bajada empinada para saber si funcionan.

## Ejemplo malo — [BadCalculator.java](BadCalculator.java)

- Un método hace todo: precios, descuento, impuesto e impresión.
- El descuento de los martes lee **la fecha de hoy** adentro del método. ¿Cómo se prueba un jueves? Esperando al martes.
- Imprime en lugar de devolver: una prueba no puede leer el resultado.
- Números mágicos: 50, 20, 0.9, 5, 1.13, sin nombre.

## Ejemplo bueno — [good-calculator/](good-calculator/)

- Métodos pequeños y **puros**: `priceOf`, `subtotal`, `discount`, `tax` y `total`. No imprimen ni leen la fecha.
- **El día es un parámetro.** La prueba pasa `TUESDAY` o `THURSDAY` cuando quiere.
- Constantes con nombre, incluido `TAX_PERCENT = 13`, el IVA boliviano.
- `Main` es el único que pregunta la fecha y el único que imprime.
- `CalculatorTest` prueba todas las reglas, cualquier día de la semana: 11 de 11.

## Demo

```bash
java BadCalculator.java

cd good-calculator
javac -d out *.java
java -cp out Main
java -cp out CalculatorTest
```

## En Sentinel Fase 3

- **El "martes" de Sentinel es `/proc`:** los datos en vivo cambian cada segundo, así que una prueba con el colector real nunca da el mismo resultado. [ReplayCollector](../../../sentinel/phase3-java/src/sentinel/collect/ReplayCollector.java) es la solución: la misma muestra, los mismos números, siempre. Es exactamente pasar el día como parámetro.
- [SentinelCheck](../../../sentinel/phase3-java/test/sentinel/check/SentinelCheck.java): 46 pruebas que corren en un segundo, sin Linux y sin colector vivo. `./build.sh` las ejecuta cada vez que se compila.
- El `RecordingRenderer` del chequeo es inyección de dependencias al servicio de las pruebas: el motor no imprime nada durante el test.
- Las reglas son casi puras: `matches()` solo mira la lectura que recibe. La excepción es `SustainedRule`, que recuerda la racha, y por eso tiene sus propias pruebas en orden.

## Pregunta para la clase

¿Qué parte de `BadCalculator` es imposible de probar hoy, sin cambiar el código? Señalen la línea exacta.
