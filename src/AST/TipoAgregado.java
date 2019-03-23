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
public class TipoAgregado extends Tipo {
    public Literal literal1, literal2;
    public Tipo typo;
    
    public int calculateSize(){
        return typo.calculateSize() * (
                Integer.parseInt(literal2.name.value) 
                - Integer.parseInt(literal1.name.value)
                + 1
                );
    }    
    public void visit(Visitor v){
        v.visitTipoAgregado(this);
    }
}
