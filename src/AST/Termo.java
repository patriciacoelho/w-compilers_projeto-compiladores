/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import compiler.Token;

/**
 *
 * @author Uendel
 */
public class Termo {
    public Fator factor;
    public Termo next;
    public Token operator;
}
