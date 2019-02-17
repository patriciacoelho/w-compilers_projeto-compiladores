/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler;

import AST.Atribuicao;
import AST.BoolLit;
import AST.ComandoComposto;
import AST.Condicional;
import AST.Corpo;
import AST.DeclaracaoDeVariavel;
import AST.Declaracoes;
import AST.Expressao;
import AST.ExpressaoSimples;
import AST.Iterativo;
import AST.ListaDeComandos;
import AST.ListaDeIds;
import AST.Literal;
import AST.Programa;
import AST.Seletor;
import AST.Termo;
import AST.TipoAgregado;
import AST.TipoSimples;
import AST.Variavel;
import Visitor.Visitor;

/**
 *
 * @author Uendel
 */
public class Checker implements Visitor{
    
    IdentificationTable table;
    
    Checker(){
        table = new IdentificationTable();
        //table.print();
    }
    
    public void check(Programa program){
        System.out.println ("---> Iniciando identificacao de nomes");
        program.visit(this);
        
    }
    
    @Override
    public void visitAtribuicao(Atribuicao becomes) {
        becomes.variable.visit(this);
        becomes.expression.visit(this);
        
        
        //Verificacao de tipos
        /*
        if(becomes.variable.type.equals(becomes.expression.type)){
            becomes.type = becomes.variable.type;
        } else if(becomes.variable.type.equals("real") && becomes.expression.type.equals("integer")){
                becomes.type = becomes.variable.type;
        } else {
            System.out.println("Atribuicao de valores incompatíveis linha:"+becomes.variable.id.line);
        } */
    }

    @Override
    public void visitBoolLit(BoolLit boolLit) {
        //
        boolLit.type = "boolean";
    }

    @Override
    public void visitComandoComposto(ComandoComposto compositeCommands) {
        compositeCommands.listOfCommands.visit(this);
    }

    @Override
    public void visitCondicional(Condicional conditional) {
        if(conditional.expression != null){
            conditional.expression.visit(this);
            //decoracao arvore
            /*
            if(!conditional.expression.type.equals("boolean")){
                System.out.println("Expressão inválida");//to do Token linha e coluna
            } */
        }


        if(conditional.command instanceof Atribuicao){
            ((Atribuicao)conditional.command).visit(this);
        } else if(conditional.command instanceof ComandoComposto){
            ((ComandoComposto)conditional.command).visit(this);
        } else if(conditional.command instanceof Iterativo){
            ((Iterativo)conditional.command).visit(this);
        } else if(conditional.command instanceof Condicional){
            ((Condicional)conditional.command).visit(this);
        }

        if(conditional.commandElse instanceof Atribuicao){
            ((Atribuicao)conditional.commandElse).visit(this);
        } else if(conditional.commandElse instanceof ComandoComposto){
            ((ComandoComposto)conditional.commandElse).visit(this);
        } else if(conditional.commandElse instanceof Iterativo){
            ((Iterativo)conditional.commandElse).visit(this);
        } else if(conditional.commandElse instanceof Condicional){
            ((Condicional)conditional.commandElse).visit(this);
        }
        
        
        
    }

    @Override
    public void visitCorpo(Corpo body) {
        body.declarations.visit(this);
        body.compositeCommand.visit(this);
        
    }

    @Override
    public void visitDeclaracaoDeVariavel(DeclaracaoDeVariavel variableDeclaration) {
       ListaDeIds aux = variableDeclaration.listOfIds;
       
       while(aux != null){
               table.enter(aux.id, variableDeclaration); 
               aux = aux.next;
               
        }
        
       /* 
       if(variableDeclaration.type instanceof TipoAgregado){
                
                ((TipoAgregado)variableDeclaration.type).visit(this);
                
       } else {
                if(variableDeclaration.type instanceof TipoSimples){
                   
                    ((TipoSimples)variableDeclaration.type).visit(this);
                    
                }
       }*/
    }

    @Override
    public void visitDeclaracoes(Declaracoes declarations) {
        Declaracoes aux = declarations;
        while(aux != null){
            aux.declarationOfVariable.visit(this);
            aux = aux.next;
        }
    }

    @Override
    public void visitExpressao(Expressao expression) {
        if(expression.simpleExpression != null){
            expression.simpleExpression.visit(this);
        }
        if(expression.simpleExpressionR != null){
            expression.simpleExpressionR.visit(this);
        }
      
    }

    @Override
    public void visitExpressaoSimples(ExpressaoSimples simpleExpression) {
        ExpressaoSimples aux = simpleExpression;
        while(aux != null){
            if(aux.term != null){
                aux.term.visit(this);
            }
            aux = aux.next;
        }
        
        
    }

    @Override
    public void visitIterativo(Iterativo iterative) {
        if(iterative.expression != null){
            iterative.expression.visit(this);
        }


        if(iterative.command instanceof Atribuicao){
            ((Atribuicao)iterative.command).visit(this);
        } else if(iterative.command instanceof ComandoComposto){
            ((ComandoComposto)iterative.command).visit(this);
        } else if(iterative.command instanceof Iterativo){
            ((Iterativo)iterative.command).visit(this);
        } else if(iterative.command instanceof Condicional){
            ((Condicional)iterative.command).visit(this);
        }
    }

    @Override
    public void visitListaDeComandos(ListaDeComandos listOfCommands) {
        ListaDeComandos aux = listOfCommands;
        while(aux != null){
            if(aux.command instanceof Atribuicao){
                ((Atribuicao)aux.command).visit(this);
            } else if(aux.command instanceof ComandoComposto){
                ((ComandoComposto)aux.command).visit(this);
            } else if(aux.command instanceof Iterativo){
                ((Iterativo)aux.command).visit(this);
            } else if(aux.command instanceof Condicional){
                ((Condicional)aux.command).visit(this);
            }
            aux = aux.next;
        }
    }

    @Override
    public void visitListaDeIds(ListaDeIds listOfIds) {
        
    }

    @Override
    public void visitLiteral(Literal literal) {
        
    }

    @Override
    public void visitPrograma(Programa program) {
        program.body.visit(this);
    }

    @Override
    public void visitSeletor(Seletor selector) {
        Seletor aux = selector;
        while(aux!= null){
            aux.expression.visit(this);
            aux = aux.next;
        }
    }

    @Override
    public void visitTermo(Termo term) {
        Termo aux = term;
        while(aux != null){
            if(aux.factor != null){
                if(aux.factor instanceof Variavel){
                    ((Variavel)aux.factor).visit(this);
                } else if(aux.factor instanceof Literal){
                    ((Literal)aux.factor).visit(this);
                }  else if(aux.factor instanceof Expressao){
                    ((Expressao)aux.factor).visit(this);
                }
            }
            
            aux = aux.next;
        }
    }

    @Override
    public void visitTipoAgregado(TipoAgregado type) {
       
    }

    @Override
    public void visitTipoSimples(TipoSimples type) {
        
    }

    @Override
    public void visitVariavel(Variavel variable) {
       table.retrieve(variable.id);
    }
    
}
