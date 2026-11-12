# 3. Bajo acoplamiento y Modularidad

## Principio

**Acoplamiento:** cuánto depende un módulo de otro.
**Modularidad:** dividir el sistema en componentes separados e intercambiables.

**Bajo acoplamiento** significa que los componentes dependen de **abstracciones**, no de implementaciones concretas.

> Analogía: un teléfono con el cargador soldado contra un teléfono con puerto USB-C. Con el puerto sirve cualquier cargador, incluso uno que todavía no se fabricó.

## Ejemplo malo — [BadNotification.java](BadNotification.java)

- `OrderService` crea su propio `MailSender` con `new`: queda soldado a él.
- Si un cliente pide SMS, hay que editar `OrderService`.
- Cada prueba de un pedido envía un correo real.
- Si `MailSender` cambia su constructor, `OrderService` se rompe.

## Ejemplo bueno — [good-notification/](good-notification/)

- `OrderProcessor` depende de `Notifier`, una interfaz.
- **Inyección de dependencias:** el notificador llega por el constructor. `OrderProcessor` nunca hace `new EmailNotifier()`.
- `SmsNotifier` se agregó sin tocar `OrderProcessor`.
- `OrderProcessorCheck` prueba los pedidos con un `FakeNotifier` que no envía nada y solo recuerda lo que le pidieron.
- Un archivo por clase: cada módulo tiene un límite claro.

## Demo

```bash
java BadNotification.java

cd good-notification
javac -d out *.java
java -cp out Main
java -cp out OrderProcessorCheck
```

## En Sentinel Fase 3

- [Sentinel](../../../sentinel/phase3-java/src/sentinel/app/Sentinel.java) recibe su `Collector`, sus `Rule` y su `Renderer` **por el constructor**: es la misma inyección de dependencias. Revisen sus `import`: no nombra ninguna regla, fuente ni salida concreta.
- [Factory](../../../sentinel/phase3-java/src/sentinel/app/Factory.java) es el único lugar que elige las clases concretas, como el `Main` de `good-notification`.
- [SentinelCheck](../../../sentinel/phase3-java/test/sentinel/check/SentinelCheck.java) usa un `RecordingRenderer`: el mismo truco que `FakeNotifier`.
- **El contrato JSON es bajo acoplamiento entre programas:** `collector.sh` y Java no comparten código, solo el formato de la línea. Por eso el colector en C podrá reemplazar al de Bash sin tocar Java.

## Pregunta para la clase

En `BadNotification`, ¿qué tendrías que hacer para probar `OrderService` sin enviar correos? ¿Y en la versión buena?
