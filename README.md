# Proyecto Lenguaje - SableCC

## Requisitos Previos

- **Java**  
  Asegúrate de tener Java instalado. Recomendado:  
  `openjdk version "21.0.5" 2024-10-15 LTS`  
  Verifica tu versión con:
  ```sh
  java -version
  ```

- **SableCC 3.7**  
  Descarga y descomprime SableCC 3.7 en `C:\sablecc-3.7`.

## Instalación

### Windows

1. **Clona el repositorio**  
   Crea una carpeta en `C:\` y clona el repositorio ahí.  
   Ejemplo de ruta final:  
   ```
   C:\SableCC\work2_fdm
   ```

2. **Genera el parser**  
   Abre una terminal en la carpeta `work2_fdm` (clic derecho > "Abrir en terminal aquí") y ejecuta:
   ```sh
   sablecc lenguaje.grammar
   ```
   Si todo es correcto, verás mensajes sobre la construcción del NFA/DFA y la generación del parser.

### Linux

1. **Clona el repositorio**  
   Elige una carpeta de tu preferencia y clona el repositorio ahí.  
   Ejemplo de ruta final:  
   ```
   ~/SableCC/work2_fdm
   ```

2. **Genera el parser**  
   Abre una terminal en la carpeta `work2_fdm` y ejecuta:
   ```sh
   sablecc lenguaje.grammar
   ```
   Si todo es correcto, verás mensajes sobre la construcción del NFA/DFA y la generación del parser.

## Compilación

Desde la terminal en la carpeta `work2_fdm`, ejecuta:

```sh
javac work\Interpreter.java
javac work\Main.java
```

## Ejecución

Ejecuta el programa con:

```sh
java work.Main
```

Esto procesará el archivo `programa.txt` y mostrará el resultado del análisis e interpretación.

## Estructura del Proyecto

- `lenguaje.grammar`: Gramática del lenguaje para SableCC.
- `programa.txt`: Archivo a interpretar.
- `work/`: Código fuente Java generado y propio.
  - `Main.java`: Punto de entrada del programa.
  - `Interpreter.java`: Lógica del intérprete.
  - `lexer/`, `parser/`, `node/`, `analysis/`: Paquetes generados por SableCC.

## Notas

- Si modificas la gramática, recuerda volver a ejecutar `sablecc lenguaje.grammar` y recompilar los archivos Java.
- Los errores de sintaxis o ejecución se mostrarán en la consola.

---
