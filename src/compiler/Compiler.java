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
    
    public static void main(String args[]) throws Exception{
        String path = (args.length == 0) ? "src/programa.pas" : args[0];
        Programa program;
        
        for (String arg : args){
            switch(arg) {
                case "--lexical": _LEXICAL_DEBUG_ = true; break;
                case "--parser" : _PARSER_DEBUG_  = true; break;
                case "--checker" : _CHECKER_DEBUG_ = true; break;
                case "--coder"  : _CODER_DEBUG_   = true; break;
                case "--all"    : _LEXICAL_DEBUG_ = true;
                                  _PARSER_DEBUG_  = true;
                                  _CHECKER_DEBUG_ = true;
                                  _CODER_DEBUG_   = true;
            }
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
        }
        Parser parser = new Parser();
        Printer printer = new Printer();
        Checker checker = new Checker();
        Coder coder = new Coder();
        program = parser.parse(path);
        if (_PARSER_DEBUG_) {
            printer.print(program);
            System.out.println("\n\n");
        }
        checker.check(program);
        if (_CHECKER_DEBUG_) {
            checker.table.print();
        }
        coder.code(program);
    }
}
