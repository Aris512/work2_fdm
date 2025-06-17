package work;
import work.lexer.*;
import work.node.*;
import work.parser.*;
import java.io.*;


public class Main {
public static void main(String[] args) {
        try {
            // Leer archivo fuente
            PushbackReader reader = new PushbackReader(new FileReader("programa.txt"));
            Lexer lexer = new Lexer(reader);
            Parser parser = new Parser(lexer);
            
            // Generar AST
            Start tree = parser.parse();
            System.out.println("¡Parseo exitoso!");

            // Interpretar el árbol
            Interpreter interpreter = new Interpreter();
            tree.apply(interpreter);

        } catch (Exception e) {
            System.out.println("Error de sintaxis o ejecución:");
            e.printStackTrace();
        }
    }
}
