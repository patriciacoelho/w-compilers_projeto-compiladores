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
    
    public int  calculateSize() {
        switch (typo.kind){
            case Token.INTEGER : return 4;
            case Token.REAL : return 8;
            case Token.BOOLEAN: return 1;
            default: return 0;
        }
    }
    
    public void visit(Visitor v){
        v.visitTipoSimples(this);
    }
}
