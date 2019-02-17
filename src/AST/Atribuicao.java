/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import Visitor.Visitor;


/**
 *
 * @author Uendel
 */
public class Atribuicao extends Comando{
    public Variavel variable;
    public Expressao expression;
    public String type;
    
    public void visit(Visitor v){
        v.visitAtribuicao(this);
    }
}
