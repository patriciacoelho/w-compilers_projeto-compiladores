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
public class DeclaracaoDeVariavel {
    public ListaDeIds listOfIds;
    public Tipo type;
    public int size;
    public int address;
    public int line;
    public int column;
    
    public void visit(Visitor v){
        v.visitDeclaracaoDeVariavel(this);
    }
    
    public int calculateSize(){
        return type.calculateSize() * listOfIds.numItens;
    }
    
}
