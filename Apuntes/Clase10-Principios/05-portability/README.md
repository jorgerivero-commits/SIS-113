# 5. Portabilidad

## Principio

**Portabilidad:** el código funciona en distintas plataformas, entornos y configuraciones **sin modificarlo**.

Aspectos clave:

- Usar bibliotecas multiplataforma.
- No escribir rutas fijas en el código.
- Aislar lo que depende del entorno.
- Leer la configuración de afuera: variables de entorno, archivos, argumentos.

> Analogía: una receta que dice "hornea en mi horno, en la perilla 3" solo sirve en tu cocina. Una que dice "hornea a 180 °C" sirve en cualquiera.

## Ejemplo malo — [BadFileHandler.java](BadFileHandler.java)

- Una ruta fija de Windows: `C:/Users/admin/reports`, que solo existe en la máquina del autor.
- Un comando que solo existe en Windows, `cmd /c dir`, para listar archivos.
- Un fin de línea de Windows escrito a mano, `\r\n`.
- En WSL falla en las dos operaciones: *No such file or directory* y *Cannot run program "cmd"*. En la computadora de un compañero con Windows, también.

## Ejemplo bueno — [GoodFileHandler.java](GoodFileHandler.java)

- La carpeta viene de una variable de entorno, `SIS113_REPORTS`. Si no está definida, usa la carpeta temporal del sistema, que existe en todos lados.
- `Path` arma las rutas con el separador correcto de cada sistema.
- Java lista los archivos por sí mismo: ningún comando del shell.
- `Files.write` termina cada línea con el separador del sistema.

## Demo

```bash
java BadFileHandler.java
java GoodFileHandler.java
SIS113_REPORTS=$HOME/reportes java GoodFileHandler.java    # otra carpeta, sin tocar el código
```

## En Sentinel Fase 3

- [ReplayCollector](../../../sentinel/phase3-java/src/sentinel/collect/ReplayCollector.java) recibe un `Path`, no una ruta escrita en el código.
- [StdinCollector](../../../sentinel/phase3-java/src/sentinel/collect/StdinCollector.java) y [ProcessCollector](../../../sentinel/phase3-java/src/sentinel/collect/ProcessCollector.java) leen con `StandardCharsets.UTF_8` explícito: una "ñ" en el nombre de un proceso se lee igual en Windows y en Linux.
- [ProcessCollector](../../../sentinel/phase3-java/src/sentinel/collect/ProcessCollector.java) **no es portable**: necesita Linux. Lo que sí está bien es que está **aislado** detrás de la interfaz `Collector`. En Windows se usa `--replay` y el resto de Sentinel funciona igual. Si alguien intenta `--run` donde no se puede, el error es claro, no un fallo misterioso.
- **Lo que falta, como ejercicio:** [SentinelCheck](../../../sentinel/phase3-java/test/sentinel/check/SentinelCheck.java) tiene escrita la ruta `../phase1-bash/samples/...`. Solo funciona si se corre desde `phase3-java`, y por eso `build.sh` hace `cd` a su propia carpeta antes. Mejorarlo como `GoodFileHandler`:

  ```java
  String configured = System.getenv("SENTINEL_SAMPLES");
  Path samples = (configured != null) ? Path.of(configured) : Path.of("../phase1-bash/samples");
  Path clean = samples.resolve("muestra-2026-09-17.txt");
  ```

## Pregunta para la clase

`collector.sh` lee `/proc`. ¿Es portable? ¿Qué parte del proyecto Sentinel nunca podrá correr en Windows sin WSL, y por qué eso no impide trabajar en el resto?
