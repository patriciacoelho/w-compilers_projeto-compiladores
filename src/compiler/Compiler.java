package compiler;

import java.io.BufferedReader;
import java.io.FileReader;

public class Compiler {
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        //BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String filename = "src/programa.pas";
        try (BufferedReader file = new BufferedReader(new FileReader(filename))) {
            Scanner scanner = new Scanner(file);
            Token tk;
            do{
                tk = scanner.scan();
                tk.imprimir();
            }while(tk.kind != Token.EOF);
        }
    }
    
}
