/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler;

import AST.DeclaracaoDeVariavel;
import java.util.HashMap;

/**
 *
 * @author Uendel
 */
public class IdentificationTable {
    
    HashMap table;
    IdentificationTable(){
        table = new HashMap();
    }
    
    public void enter(Token id, DeclaracaoDeVariavel declaration){
        if(table.put(id.value, declaration) != null){
            System.out.println("Identificador "+id.value+" já declarado. linha= "+id.line+" col="+id.col);
            System.exit(1);
        }
    }
    
    public DeclaracaoDeVariavel retrieve(Token id){
        if(table.containsKey(id.value) == false){
            System.out.println("Identificador "+id.value+" não declarado. linha= "+id.line+" col="+id.col);
            System.exit(1);
        } else {
            return (DeclaracaoDeVariavel)table.get(id.value);
        }
        return null;
    }
    
    public void print(){
        System.out.println("---> Imprimindo tabela");
        table.keySet().forEach((name) -> {
            String key = name.toString();
            String value = table.get(name).toString();
            System.out.println(key + " " + value);
        });
    }
}

