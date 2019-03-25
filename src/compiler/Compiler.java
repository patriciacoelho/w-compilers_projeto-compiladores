/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler;

import AST.Programa;

/**
 *
 * @author Uendel
 */
public class Compiler {
    
    public static boolean _LEXICAL_DEBUG_ = false;
    public static boolean _PARSER_DEBUG_ = false;
    public static boolean _CHECKER_DEBUG_ = false;
    public static boolean _CODER_DEBUG_ =false;
    public static boolean _ALL_ = false;
    public static String path = null;
    
    public static void main(String args[]) throws Exception{
        Programa program;
        for (int i = 0; i < args.length ; i++){
            String arg = args[i];
            switch(arg) {
                case "--lexical": _LEXICAL_DEBUG_ = true; break;
                case "--parser" : _PARSER_DEBUG_  = true; break;
                case "--checker": _CHECKER_DEBUG_ = true; break;
                case "--coder"  : _CODER_DEBUG_   = true; break;
                case "--all"    : _LEXICAL_DEBUG_ = true;
                                  _PARSER_DEBUG_  = true;
                                  _CHECKER_DEBUG_ = true;
                                  _CODER_DEBUG_   = true;
                                  _ALL_           = true; break;
                case "--file"   : 
                                  if (i+1 >= args.length) {
                                      System.out.println("Erro: o caminho do arquivo deve ser informado após --file");
                                      System.exit(1);
                                  }   
                                  path = args[i+1];
                                  break;
            }
        }
        if (path == null){
            path = "src/programa.pas";
        }
        if (_LEXICAL_DEBUG_) {
        	System.out.println("============ Lexical Debugger ============\n");
        	Scanner scanner = new Scanner(path);
	        Token tk;
	        System.out.printf("%20s | %4s | %4s | %3s \n", "SPEELING", "KIND", "LIN", "COL");
	        System.out.println("_____________________|______|______|______");
	        do{
	            tk = scanner.scan();
	            tk.print();
	        } while(tk.kind != Token.EOF);
                System.out.println("\n\n");
                if (!_ALL_) {
                    System.exit(0);
                }
        }
        Parser parser = new Parser();
        program = parser.parse(path);
        if (_PARSER_DEBUG_) {
            Printer printer = new Printer();
            printer.print(program);
            System.out.println("\n\n");
            if (!_ALL_) {
                System.exit(0);
            }
        }
        Checker checker = new Checker();
        checker.check(program);
        if (_CHECKER_DEBUG_) {
            checker.table.print();
            if (!_ALL_) {
                System.exit(0);
            }
        }
        Coder coder = new Coder();
        coder.code(program);
    }
}
