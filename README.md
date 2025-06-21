Se debe tener:
> Instalado java
version: openjdk version "21.0.5" 2024-10-15 LTS
para revisar colocar en el cmd: java -version

Intalado Sablecc-3.7 
ubicado en la carpeta "C:\sablecc-3.7"

Para ubicar el programa
>Crear un carpeta en C: y luego clonar el repositorio en esa carpeta
/tal que "path" quede: "C:\SableCC\work2_fdm"

Crear el arbol NFA y generar el parser
debe abrir el cmd en la carpeta de "work2_fdm"
click derecho "abrir en terminal"
ejecute el siguiente comando:
sablecc lenguaje.grammar
>Si todo esta correcto debe aparecer:
" State: INITIAL
 - Constructing NFA.
......................................................................................................................................................
 - Constructing DFA.
..............................................................................................................................................................................................................................................................
............................................................................................
 - resolving ACCEPT states.
Generating the parser..........................................................................................................................
.........................................................................................................................
.........................................................................................................................
..
........................................................................................................................."

Para ejecutarlo deben seguir los pasos:
>1 compilar los archivos desde la consola
comandos:  
javac work\Main.java  
javac work\Interpreter.java    

>2 ejecutar el programa desde consola 
comando:
java work\Main.java