# 2. Encapsulamiento y Abstracción

## Principio

**Encapsulamiento:** esconder los detalles internos y exponer solo lo necesario.
**Abstracción:** ofrecer una interfaz simple que esconde la complejidad.

> Analogía: en un cajero automático pides "retirar 300". No abres la bóveda, no tocas el registro de movimientos. El cajero verifica tu saldo y anota el movimiento por ti, siempre, en el mismo orden.

Es repaso de la Clase 6, ahora con nombre de principio de diseño.

## Ejemplo malo — [BadBankAccount.java](BadBankAccount.java)

- Los datos son públicos y cualquiera los modifica.
- Cada cliente tiene que conocer las reglas internas: "actualiza el saldo **y** el historial, y revisa los fondos antes".
- Sin validación ni protección. Al final: saldo de **-200** y un historial con un depósito de un millón que nunca existió.

## Ejemplo bueno — [GoodBankAccount.java](GoodBankAccount.java)

- Atributos privados. El saldo solo cambia con `deposit()` y `withdraw()`.
- Las reglas viven adentro: no hay retiros sin fondos ni depósitos negativos.
- El historial se entrega como **copia**: modificarla no cambia la cuenta.
- El cliente trabaja con operaciones simples y no sabe cómo están hechas.

## Demo

```bash
java BadBankAccount.java
java GoodBankAccount.java
```

## En Sentinel Fase 3

- [Snapshot](../../../sentinel/phase3-java/src/sentinel/model/Snapshot.java): siete atributos `private final`, sin setters, y un constructor que rechaza datos imposibles. Una lectura inválida no puede existir.
- [History](../../../sentinel/phase3-java/src/sentinel/model/History.java): la lista es privada y nunca se entrega. Afuera solo existen `add()` y las preguntas.
- [Sentinel](../../../sentinel/phase3-java/src/sentinel/app/Sentinel.java): el constructor guarda una **copia** de la lista de reglas, `new ArrayList<>(rules)`. Si quien la creó la modifica después, el motor no se entera.
- **Abstracción:** [Contract.parse(line)](../../../sentinel/phase3-java/src/sentinel/model/Contract.java) esconde todo el trabajo con `indexOf` y `substring` detrás de un solo método.

## Pregunta para la clase

`Sentinel` guarda una copia de la lista de reglas. ¿Qué problema concreto evita? Escriban las tres líneas de código que lo provocarían si no la copiara.
