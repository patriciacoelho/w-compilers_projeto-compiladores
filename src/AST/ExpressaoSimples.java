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
public class ExpressaoSimples {
    public Termo term;
    public ExpressaoSimples next;
    public Token operator;
    public String type;
    
    public void visit(Visitor v){
        v.visitExpressaoSimples(this);
    }
}
