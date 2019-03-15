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
    public static void main(String args[]) throws Exception{
        Programa program;
        boolean lexical_debug = true;

        String testes = "src/programa.pas";

        if ( lexical_debug ) {
        	System.out.println("============ Lexical Debugger ============\n");
        	Scanner scanner = new Scanner(testes);
	        Token tk;
	        System.out.printf("%20s | %4s | %4s | %3s \n", "SPEELING", "KIND", "LIN", "COL");
	        System.out.println("_____________________|______|______|______");
	        do{
	            tk = scanner.scan();
	            tk.print();
	            // System.out.println(tk.toString());
	        } while(tk.kind != Token.EOF);
        } else {
	        Parser parser = new Parser();
	        Printer printer = new Printer();
	        Checker checker = new Checker();
	        //Coder coder = new Coder();
	        program = parser.parse(testes);
	        printer.print(program);
	        checker.check(program);
	        //coder.code(p);
        }


    }



}
