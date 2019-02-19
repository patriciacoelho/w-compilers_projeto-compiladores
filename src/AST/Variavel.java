/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import Visitor.Visitor;
import compiler.Token;

/**
 *
 * @author Uendel
 */
public class Variavel extends Fator{
    public Token id;
    public Seletor selector;
    public DeclaracaoDeVariavel declaration;
    
    
    public void visit(Visitor v){
        v.visitVariavel(this);
    }
}
