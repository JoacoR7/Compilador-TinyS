# Compilador TinyS

Compilador del lenguaje de programación **TinyS**, desarrollado para la materia **Compiladores** de la **Universidad Nacional de Cuyo** durante el año **2026**.

## Autores

- [Joaquin Ruiz](https://github.com/JoacoR7).
- [Víctor Ramírez](https://github.com/VictorRamirez26): desarrollo conjunto hasta la etapa de **chequeo semántico de sentencias inclusive**.

El análisis léxico, el análisis sintáctico y el análisis semántico —tanto de declaraciones como de sentencias— fueron realizados por Joaquin Ruiz y Victor Ramirez. La etapa de generación de código fue desarrollada posteriormente de forma individual por Joaquin Ruiz.

## Descripción

El compilador procesa archivos fuente TinyS (`.s`), valida su estructura y sus reglas semánticas, y genera código ensamblador **MIPS** (`.asm`).

El proyecto incluye las siguientes etapas:

1. **Análisis léxico:** reconocimiento de tokens y detección de errores léxicos.
2. **Análisis sintáctico:** validación del programa según la gramática del lenguaje.
3. **Chequeo de declaraciones:** construcción y validación de la tabla de símbolos.
4. **Chequeo de sentencias:** construcción del árbol de sintaxis abstracta (AST) y comprobación de tipos y reglas semánticas.
5. **Generación de código:** traducción del programa a ensamblador MIPS para su ejecución en **MARS**.

La documentación del lenguaje y de la implementación se encuentra en el [manual del proyecto](Manual_TinyS/manual.pdf), cuyo código fuente está disponible en [LaTeX](Manual_TinyS/manual.tex).

## Tecnologías utilizadas

- **Java 21** para la implementación del compilador.
- **Maven** para la compilación, el empaquetado y la gestión de dependencias.
- **JUnit 6** para las pruebas automatizadas.
- **MARS** para ejecutar el ensamblador MIPS generado.

## Utilización

Se requiere tener instalados **JDK 21** y **Maven**, y ejecutar los comandos desde la raíz del repositorio.

### Compilar el proyecto

Para generar el compilador completo, utilizá el perfil `tinyS`:

```bash
mvn clean package -P tinyS
```

El ejecutable se genera en `target/tinyS.jar`.

### Compilar un programa TinyS

```bash
java -jar target/tinyS.jar <ruta_al_archivo.s>
```

Por ejemplo, para compilar el caso de Fibonacci incluido en el repositorio:

```bash
java -jar target/tinyS.jar tc/tinyS/success/fibonacci.s
```

Si el análisis finaliza correctamente, se generan tres archivos junto al fuente:

- `fibonacci.ts.json`: tabla de símbolos.
- `fibonacci.ast.json`: árbol de sintaxis abstracta.
- `fibonacci.asm`: código ensamblador MIPS, que puede abrirse y ejecutarse en MARS.

### Ejecutar etapas por separado

También se puede generar un ejecutable para cada etapa mediante su perfil de Maven:

| Perfil | Ejecutable | Etapa |
| --- | --- | --- |
| `etapa1` | `target/etapa1.jar` | Análisis léxico |
| `etapa2` | `target/etapa2.jar` | Análisis sintáctico |
| `etapa3` | `target/etapa3.jar` | Chequeo de declaraciones |
| `etapa4` | `target/etapa4.jar` | Chequeo de sentencias |

Por ejemplo:

```bash
mvn clean package -P etapa4
java -jar target/etapa4.jar tc/tinyS/success/fibonacci.s
```

## Pruebas

Los tests automatizados están en [`src/test/java/uncuyo/compiladores`](src/test/java/uncuyo/compiladores), y los casos de prueba del lenguaje se encuentran en [`tc`](tc).

Para ejecutar las pruebas:

```bash
mvn test
```

Las pruebas de generación de código escriben archivos `.asm` en `tc/tinyS/mipsFiles`. La ejecución del ensamblador generado se realiza por separado en MARS.
