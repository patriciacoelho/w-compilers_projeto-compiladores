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
        Token tk = new Token((byte)1,"0",0,0);
        while(tk.kind != Token.EOF){
            tk = scanner.scan();
            tk.imprimir();
        }
        
        arquivo.close();
       
        
        
    }
    
}
