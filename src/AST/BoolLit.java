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
public class BoolLit extends Literal {
    
    @Override
    public void visit(Visitor v){
        v.visitBoolLit(this);
    }
}
