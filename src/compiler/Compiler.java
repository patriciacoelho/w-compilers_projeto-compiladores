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
        String testes = "src/programa.pas";
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
