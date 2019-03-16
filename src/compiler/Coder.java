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
public class Coder implements Visitor{
    
    public void code(Programa program){ //feito
        System.out.println("Iniciando Geracao de Codigo");
        System.out.println("");
        program.visit(this);
        System.out.println("HALT");
    }
    
    
    @Override
    public void visitAtribuicao(Atribuicao becomes) { //feito
        becomes.expression.visit(this);
        
        //retorno de valor
        becomes.variable.value =  becomes.expression.value;
        //System.out.println(becomes.variable.id.value+" "+becomes.variable.value+becomes.expression.value);
        
        //Geracao Codigo
        System.out.println("STORE "+becomes.variable.id.value); //substituido para endereço
    }

    @Override
    public void visitBoolLit(BoolLit boolLit) {
        boolLit.value = boolLit.name.value; //add retorno literal
        
    }

    @Override
    public void visitComandoComposto(ComandoComposto compositeCommands) {
        if(compositeCommands.listOfCommands != null){
            compositeCommands.listOfCommands.visit(this);
        }
    }

    @Override
    public void visitCondicional(Condicional conditional) { //feito
        
        if(conditional.expression != null){
            conditional.expression.visit(this);
        }
        
        System.out.println("JUMPIF(0) g");
        
        if(conditional.command instanceof Atribuicao){
            ((Atribuicao)conditional.command).visit(this);
        } else if(conditional.command instanceof ComandoComposto){
            ((ComandoComposto)conditional.command).visit(this);
        } else if(conditional.command instanceof Iterativo){
            ((Iterativo)conditional.command).visit(this);
        } else if(conditional.command instanceof Condicional){
            ((Condicional)conditional.command).visit(this);
        }
        
        System.out.println("JUMP h");
        
        System.out.print("g: ");
        if(conditional.commandElse instanceof Atribuicao){
            ((Atribuicao)conditional.commandElse).visit(this);
        } else if(conditional.commandElse instanceof ComandoComposto){
            ((ComandoComposto)conditional.commandElse).visit(this);
        } else if(conditional.commandElse instanceof Iterativo){
            ((Iterativo)conditional.commandElse).visit(this);
        } else if(conditional.commandElse instanceof Condicional){
            ((Condicional)conditional.commandElse).visit(this);
        }
        
        System.out.print("h: ");
    }

    @Override
    public void visitCorpo(Corpo body) {
        System.out.println("PUSH ");
        if(body.declarations!=null){
           body.declarations.visit(this);
        }
        body.compositeCommand.visit(this);
        System.out.println("POP ");
    }

    @Override
    public void visitDeclaracaoDeVariavel(DeclaracaoDeVariavel variableDeclaration) {
        variableDeclaration.listOfIds.visit(this);
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
        expression.simpleExpression.visit(this);
        expression.value = expression.simpleExpression.value; //add retorno literal
        if(expression.simpleExpressionR != null){
            expression.simpleExpressionR.visit(this);
            boolean a;
                switch(expression.operator.value){
                    case "<":
                        if(expression.simpleExpression.value == null || expression.simpleExpressionR.value == null){
                            expression.value = null;
                        } else{
                            a = Float.parseFloat(expression.simpleExpression.value) < Float.parseFloat(expression.simpleExpressionR.value);
                            expression.value = Boolean.toString(a); //add retorno literal
                        }
                        System.out.println("CALL greater");
                        break;
                    case ">":
                        if(expression.simpleExpression.value == null || expression.simpleExpressionR.value == null){
                            expression.value = null;
                        } else{
                            a = Float.parseFloat(expression.simpleExpression.value) > Float.parseFloat(expression.simpleExpressionR.value);
                            expression.value = Boolean.toString(a); //add retorno literal
                        }
                        System.out.println("CALL lesser");
                        break;
                    case "<=":
                        if(expression.simpleExpression.value == null || expression.simpleExpressionR.value == null){
                            expression.value = null;
                        } else{
                            a = Float.parseFloat(expression.simpleExpression.value) <= Float.parseFloat(expression.simpleExpressionR.value);
                            expression.value = Boolean.toString(a); //add retorno literal
                        }
                        System.out.println("CALL greater_equal");
                        break;
                    case ">=":
                        if(expression.simpleExpression.value == null || expression.simpleExpressionR.value == null){
                            expression.value = null;
                        } else{
                            a = Float.parseFloat(expression.simpleExpression.value) >= Float.parseFloat(expression.simpleExpressionR.value);
                            expression.value = Boolean.toString(a); //add retorno literal
                        }
                        System.out.println("CALL lesser_equal");
                        break;
                    case "=":
                        if(expression.simpleExpression.value == null || expression.simpleExpressionR.value == null){
                            expression.value = null;
                        } else{
                            a = Float.parseFloat(expression.simpleExpression.value) == Float.parseFloat(expression.simpleExpressionR.value);
                            expression.value = Boolean.toString(a); //add retorno literal
                        }
                        System.out.println("CALL equals");
                        break;
                    case "<>":
                        if(expression.simpleExpression.value == null || expression.simpleExpressionR.value == null){
                            expression.value = null;
                        } else{
                            a = Float.parseFloat(expression.simpleExpression.value) != Float.parseFloat(expression.simpleExpressionR.value);
                            expression.value = Boolean.toString(a); //add retorno literal
                        }
                        System.out.println("CALL diff");
                    break;
                    default:
                        expression.value = null; //add retorno literal      
                }
        }
        //System.out.println(expression.value);
    }

    @Override
    public void visitExpressaoSimples(ExpressaoSimples simpleExpression) {
        ExpressaoSimples aux = simpleExpression;
        String valueAux = "start"; //add retorno literal
        while(aux != null){
            if(aux.term != null){
                aux.term.visit(this);
                if(valueAux != null){
                    if(valueAux.equals("start")){
                        valueAux = aux.term.value;//add retorno literal
                    } 
                }
                if(aux.operator != null){
                    switch(aux.operator.kind){
                        case Token.SUM:
                            if(valueAux == null || aux.term.value == null){
                                valueAux = null;
                            } else {
                                //System.out.println(valueAux+"+"+aux.term.value);
                                valueAux = Float.toString(Float.parseFloat(valueAux) + Float.parseFloat(aux.term.value));
                            }
                            System.out.println("CALL add");
                        break;
                        case Token.SUB:
                            if(valueAux == null || aux.term.value == null){
                                valueAux = null;
                            } else {
                                valueAux = Integer.toString(Integer.parseInt(valueAux) - Integer.parseInt(aux.term.value));
                            }
                            System.out.println("CALL sub");
                        break;
                        case Token.OR:
                            if(valueAux == null || aux.term.value == null){
                                valueAux = null;
                            } else {
                                valueAux = Boolean.toString(Boolean.valueOf(valueAux) || Boolean.valueOf(aux.term.value));
                            }  
                    }
                }
            }
            aux = aux.next;
        }
        simpleExpression.value = valueAux;
        //System.out.println(simpleExpression.value);
    }

    @Override
    public void visitIterativo(Iterativo iterative) {
        
        System.out.println("JUMP h");
        
        System.out.print("gI: ");
        if(iterative.command instanceof Atribuicao){
            ((Atribuicao)iterative.command).visit(this);
        } else if(iterative.command instanceof ComandoComposto){
            ((ComandoComposto)iterative.command).visit(this);
        } else if(iterative.command instanceof Iterativo){
            ((Iterativo)iterative.command).visit(this);
        } else if(iterative.command instanceof Condicional){
            ((Condicional)iterative.command).visit(this);
        }
        
        System.out.print("hI: ");
        if(iterative.expression != null){
            iterative.expression.visit(this);
        }
        
        System.out.println("JUMPIF(1) gI");
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
        literal.value = literal.name.value;
        if(literal.type.equals("integer")){
            System.out.println("LOADL "+literal.value);
        }
        //System.out.println(literal.value);
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

            selector.value = aux.expression.value; //erro aqui ??? 
            
            aux = aux.next;
        }
    }

    @Override
    public void visitTermo(Termo term) {
        Termo aux = term;
        String valueAux = null;
        //System.out.println("etou aqui");
        while(aux != null){
            if(aux.factor != null){
                if(aux.factor instanceof Variavel){
                    ((Variavel)aux.factor).visit(this);
                    aux.value = aux.factor.value;
                } else if(aux.factor instanceof Literal){
                    ((Literal)aux.factor).visit(this);
                    aux.value = aux.factor.value;
                }  else if(aux.factor instanceof Expressao){
                    ((Expressao)aux.factor).visit(this);
                    aux.value = aux.factor.value;
                }
                
                if(valueAux == null){
                    valueAux = aux.value;
                }
                
                if(aux.operator != null){
                    switch(aux.operator.kind){
                        case Token.DIV:
                            if(valueAux == null || aux.value == null){
                                valueAux = null;
                            } else {
                                valueAux = Float.toString(Float.parseFloat(valueAux) / Float.parseFloat(aux.value));
                            }
                            System.out.println("CALL div");
                        break;
                        case Token.MULT:
                            if(valueAux == null || aux.value == null){
                                valueAux = null;
                            } else {
                                valueAux = Float.toString(Float.parseFloat(valueAux) * Float.parseFloat(aux.value));
                            }
                            System.out.println("CALL mult");
                        break;
                        case Token.AND:
                            valueAux = Boolean.toString(Boolean.valueOf(valueAux) && Boolean.valueOf(aux.value)); 
                        break;
                    }
                }
                
            }
            aux = aux.next;
        }
        term.value = valueAux;
        //System.out.println(term.value);

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
    }

    @Override
    public void visitTipoSimples(TipoSimples type) {
        
    }

    @Override
    public void visitVariavel(Variavel variable) {
        if(variable.selector != null){
           variable.selector.visit(this);
       }
        System.out.println("LOAD "+variable.id.value);
    }
    
}
