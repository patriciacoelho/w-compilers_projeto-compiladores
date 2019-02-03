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
public class TipoSimples extends Tipo{
    public Token typo;
    
    public void visit(Visitor v){
        v.visitTipoSimples(this);
    }
}
