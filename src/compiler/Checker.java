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
    }

    public void check(Programa program){
//        System.out.println ("---> Iniciando Análise de Contexto");
        program.visit(this);
    }

    @Override
    public void visitAtribuicao(Atribuicao becomes) {
        becomes.variable.visit(this);
        becomes.expression.visit(this);

        //Verificacao de tipos
        if(becomes.variable.type != null || becomes.expression.value != null){
            if(becomes.variable.type.equals(becomes.expression.type)){
                
            } else if(becomes.variable.type.equals("real") && becomes.expression.type.equals("integer")){
            
            } else {
                System.out.print("ERRO DE CONTEXTO: ");
                System.out.println("Atribuicao de tipos incompatíveis [linha:"+becomes.variable.id.line+"].");
                System.exit(1);
            }
        }
        //System.out.println(becomes.variable.id.value+" valor variavel="+becomes.variable.value+becomes.expression.value);
    }

    @Override
    public void visitBoolLit(BoolLit boolLit) {
        boolLit.type = "boolean";
    }

    @Override
    public void visitComandoComposto(ComandoComposto compositeCommands) {
        if(compositeCommands.listOfCommands != null){
            compositeCommands.listOfCommands.visit(this);
        }
    }

    @Override
    public void visitCondicional(Condicional conditional) {
        if(conditional.expression != null){
            conditional.expression.visit(this);
            //decoracao arvore
            if(!conditional.expression.type.equals("boolean")){
                System.out.print("ERRO DE CONTEXTO: ");//to do Token linha
                System.out.println("Esperava encontrar uma expressão booleana ao invés de "+conditional.expression.type+".");
                System.exit(1);
            }
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
        if(body.declarations!=null){
           body.declarations.visit(this);
        }
        body.compositeCommand.visit(this);
    }

    @Override
    public void visitDeclaracaoDeVariavel(DeclaracaoDeVariavel variableDeclaration) {
        ListaDeIds aux = variableDeclaration.listOfIds;
       
        while(aux != null){
            table.enter(aux.id, variableDeclaration);
            aux = aux.next;
        }

        if(variableDeclaration.type instanceof TipoAgregado){
            ((TipoAgregado)variableDeclaration.type).visit(this);

        } else{
            if(variableDeclaration.type instanceof TipoSimples){
                ((TipoSimples)variableDeclaration.type).visit(this);
            }
        }
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
            expression.type = expression.simpleExpression.type;
        }
        if(expression.simpleExpressionR != null){
            expression.simpleExpressionR.visit(this);
            if("real".equals(expression.simpleExpressionR.type) || expression.simpleExpressionR.type.equals("integer") ){
                expression.type = "boolean";
            } else {
                System.out.print("ERRO DE CONTEXTO: ");
                System.out.println("Incompatibilidade de tipos dos operandos [linha: "+expression.operator.line+"].");
                System.exit(1);
            }
        }
    }

    @Override
    public void visitExpressaoSimples(ExpressaoSimples simpleExpression) { //o problema ta aqui e em termo
        ExpressaoSimples aux = simpleExpression;
        String place = null;
       
        while(aux != null){
            if(aux.term != null){
                aux.term.visit(this);
                if( place == null ){
                    place = aux.term.type;
                }
                if(aux.operator != null){
                    switch(aux.operator.kind){
                        case Token.SUM:
                        case Token.SUB:
                            switch (place) {
                                case "integer":
                                	switch (aux.term.type){
                                            case "integer":
                                		place = "integer";
                                            break;
                                            case "real":
                                		place = "real";
                                            break;
                                            default:
                                                System.out.print("ERRO DE CONTEXTO: ");
                                		System.out.println("Incompatibilidade de tipos dos operandos [linha: "+aux.operator.line+"].");
                                                System.exit(1);
                                	}
                                break;
                                case "real":
                                    if ( aux.term.type.equals("boolean") ) {
                                    	System.out.print("ERRO DE CONTEXTO: ");
                                	System.out.println("Incompatibilidade de tipos dos operandos [linha: "+aux.operator.line+"].");
                                        System.exit(1);
                                    }
                                    place = "real";
                                break;
                                default:
                                    System.out.print("ERRO DE CONTEXTO: ");
                                    System.out.println("Incompatibilidade de tipos dos operandos [linha: "+aux.operator.line+"].");
                                    System.exit(1);
                            }
                            
                        break;
                        case Token.OR:
                            if(!place.equals("boolean") || !aux.term.type.equals("boolean")){
                                System.out.print("ERRO DE CONTEXTO: ");
                                System.out.println("Incompatibilidade de tipos dos operandos [linha: "+aux.operator.line+"].");
                                System.exit(1);
                            }
                            place = "boolean";
                        break;
                    }
                }
            }
            aux = aux.next;
        }
        simpleExpression.type = place;
        
    }

    @Override
    public void visitIterativo(Iterativo iterative) {
        if(iterative.expression != null){
            iterative.expression.visit(this);
        }

        if(!iterative.expression.type.equals("boolean")){
            System.out.print("ERRO DE CONTEXTO: ");//to do Token linha
            System.out.println("Esperava encontrar uma expressão booleana ao invés de "+iterative.expression.type+".");
            System.exit(1);
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
        switch(literal.name.kind){
            case Token.INT_LIT:
                literal.type = "integer";
            break;
            case Token.FLOAT_LIT:
                literal.type = "real";
            break;
            case Token.BOOLEAN:
                literal.type = "boolean";
            break;
        }
    }

    @Override
    public void visitPrograma(Programa program) {
        program.body.visit(this);
    }

    @Override
    public void visitSeletor(Seletor selector) {
        Seletor aux = selector;
        while(aux != null){
            aux.expression.visit(this);  
            if(!aux.expression.type.equals("integer")){
                System.out.print("ERRO DE CONTEXTO: ");//to do Token linha
                System.out.println("Esperava encontrar inteiro como indice do seletor.");
                System.exit(1);
            }
            aux = aux.next;
        }


    }

    @Override
    public void visitTermo(Termo term) { // problema aqui tb
        Termo aux = term;
        String place = null;
        while(aux != null){
            if(aux.factor != null){

                if(aux.factor instanceof Variavel){
                    ((Variavel)aux.factor).visit(this);
                    aux.type = aux.factor.type;
                    aux.value = aux.factor.value;
                } else if(aux.factor instanceof Literal){
                    ((Literal)aux.factor).visit(this);
                    aux.type = aux.factor.type;
                    aux.value = aux.factor.value;
                }  else if(aux.factor instanceof Expressao){
                    ((Expressao)aux.factor).visit(this);
                    aux.type = aux.factor.type;
                    aux.value = aux.factor.value;
                }
//                if(aux.operator != null && aux.operator.value.equals("/")){
//                    term.type = "real";
//                }


                if( place == null ){
                    place = aux.type;
                }
                if(aux.operator != null){
                	//System.out.println(place + " op-mul  " + term.type);
                	switch(aux.operator.kind){
                            case Token.DIV:
                        	if(place.equals("boolean") || aux.type.equals("boolean")){
                                    System.out.print("ERRO DE CONTEXTO: ");
                                    System.out.println("Incompatibilidade de tipos dos operandos [linha: "+aux.operator.line+"].");
                                    System.exit(1);
                                }
                        	place = "real";
                            break;
                            case Token.MULT:
                                switch (place) {
                                    case "integer":
                                	switch(aux.type){
                                            case "integer":
                                		place = "integer";
                                            break;
                                            case "real":
                                		place = "real";
                                            break;
                                            default:
                                		System.out.print("ERRO DE CONTEXTO: ");
                                                System.out.println("Incompatibilidade de tipos dos operandos [linha: "+aux.operator.line+"].");
                                                System.exit(1);
                                            }
                                    break;
                                    case "real":
                                	switch (aux.type){
                                            case "integer":
                                            case "real":
                                		place = "real";
                                            break;
                                            default:
                                		System.out.print("ERRO DE CONTEXTO: ");
                                                System.out.println("Incompatibilidade de tipos dos operandos [linha: "+aux.operator.line+"].");
                                                System.exit(1);
                                	}
                                    break;
                                    default:
                                        System.out.print("ERRO DE CONTEXTO: ");
                                        System.out.println("Incompatibilidade de tipos dos operandos [linha: "+aux.operator.line+"].");
                                        System.exit(1);
                            }
                        break;
                        case Token.AND:
                            if(!place.equals("boolean") || !aux.type.equals("boolean")){
                               System.out.print("ERRO DE CONTEXTO: ");
                               System.out.println("Incompatibilidade de tipos dos operandos [linha: "+aux.operator.line+"].");
                               System.exit(1);
                            }
                            place = "boolean";
                        break;
                    }
                }

            }
            aux = aux.next;

        }
        term.type = place;
    }

    @Override
    public void visitTipoAgregado(TipoAgregado type) {
        if(type.typo instanceof TipoAgregado){
            ((TipoAgregado)type.typo).visit(this);
        } else {
            if(type.typo instanceof TipoSimples){
               ((TipoSimples)type.typo).visit(this);
            }
        }
        type.type = type.typo.type;
        
        if(Integer.parseInt(type.literal1.name.value) > Integer.parseInt(type.literal2.name.value)){
            System.out.print("ERRO DE CONTEXTO: ");
            System.out.println("Limite superior do array menor que o limite inferior[linha: "+type.literal2.name.line+"].");
            System.exit(1);
        }
    }

    @Override
    public void visitTipoSimples(TipoSimples type) {
        type.type = type.typo.value;
    }

    @Override
    public void visitVariavel(Variavel variable) {
        //variable.value = null;
        DeclarationPointer declarationPointer = table.retrieve(variable.id); 
        variable.declaration = declarationPointer.declaracao;
        variable.index = declarationPointer.indice;
 
       if(variable.declaration != null){
           variable.type = variable.declaration.type.type;
       }

       if(variable.selector != null){
           variable.selector.visit(this);
       }
    }
}
