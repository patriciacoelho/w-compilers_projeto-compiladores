package compilador;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;

public class Compilador {
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        //BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String file = "C:\\Users\\uende\\DESKTOP\\programa.txt";
        BufferedReader arquivo = new BufferedReader(new FileReader(file));
        
        Scanner scanner = new Scanner(arquivo);
        Token tk;
        do{
            tk = scanner.scan();
            tk.imprimir();
        }while(tk.kind != Token.EOF);
        
        arquivo.close();
       
        
        
    }
    
}
