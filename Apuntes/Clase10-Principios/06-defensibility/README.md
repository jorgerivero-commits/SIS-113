# 6. Defensividad

## Principio

**Defensividad:** escribir código que falla de forma segura y detecta los errores temprano.

Conceptos clave:

- **Fail-fast (fallar rápido):** detectar y reportar el error en el momento, no tres pasos después.
- **Mínimo privilegio:** dar solo los permisos necesarios.
- **Valores por defecto seguros:** si nadie configuró nada, elegir la opción conservadora.

> Analogía: un buen cajero revisa el billete **antes** de entregar el cambio, no después. Y no cualquier empleado puede abrir la caja fuerte: solo el supervisor tiene la llave.

## Ejemplo malo — [BadPayment.java](BadPayment.java)

- Revisa la tarjeta **después** de cobrar, y esconde el error con un `catch` vacío.
- Acepta un monto negativo: un "pago" que saca dinero.
- Valores por defecto inseguros: cualquier terminal puede devolver dinero, y la moneda empieza en `null`.
- Escribe el número completo de la tarjeta en el log.
- Al final la caja marca **Bs -1350** y no se imprimió ni un error.

## Ejemplo bueno — [GoodPayment.java](GoodPayment.java)

- **Fail-fast:** valida tarjeta y monto **antes** de cobrar, con un mensaje claro.
- **Valores seguros:** moneda `BOB` y devoluciones desactivadas, salvo que se pidan explícitamente.
- **Mínimo privilegio:** solo la terminal del supervisor se crea con permiso de devolución.
- El log muestra solo los últimos 4 dígitos.
- Usa `IllegalStateException` cuando la operación no está permitida **en ese estado**, y `IllegalArgumentException` cuando el dato es inválido.

## Demo

```bash
java BadPayment.java
java GoodPayment.java
```

## En Sentinel Fase 3

- **Fail-fast:** [Config](../../../sentinel/phase3-java/src/sentinel/app/Config.java) valida todas las opciones **antes de empezar**. `--mem=eighty` termina con un mensaje claro y código de salida 2, sin leer una sola línea.
- **Orden defensivo:** [Main](../../../sentinel/phase3-java/src/sentinel/app/Main.java) pide a `Factory` las reglas y la salida primero, y el colector **al final**. Si una opción es inválida, nunca se llega a lanzar un proceso.
- **Fallar seguro:** [Sentinel.run()](../../../sentinel/phase3-java/src/sentinel/app/Sentinel.java) cierra el colector en un bloque `finally`. Aunque la lectura falle, el proceso hijo no queda corriendo.
- **Valores seguros:** sin opciones, Sentinel usa memoria al 80 % y la salida con colores. Y no acepta quedarse sin reglas: un monitor que no vigila nada es un error, no una configuración.
- **Una línea mala no tumba el programa:** `Contract` la rechaza, se cuenta como inválida y se sigue con la próxima.

## Pregunta para la clase

En `BadPayment`, ¿cuándo se entera alguien de que algo salió mal? ¿Y en `GoodPayment`? ¿Cuál de los dos preferirías depurar a las 3 de la mañana?
