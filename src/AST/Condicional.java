/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

/**
 *
 * @author Uendel
 */
public class Condicional extends Comando{
    public Expressao expression;
    public Comando command;
    public Comando commandElse;
    
}
