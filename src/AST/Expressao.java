package AST;

import Visitor.Visitor;
import compiler.Token;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Uendel
 */
public class Expressao extends Fator{
    public ExpressaoSimples simpleExpression;
    public ExpressaoSimples simpleExpressionR;
    public Token operator;
    
    public void visit(Visitor v){
        v.visitExpressao(this);
    }
}
