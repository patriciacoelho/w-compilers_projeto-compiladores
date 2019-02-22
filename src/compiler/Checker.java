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
        System.out.println ("---> Iniciando identificacao de nomes e tipos");
        program.visit(this);

    }

    @Override
    public void visitAtribuicao(Atribuicao becomes) {
        becomes.variable.visit(this);
        becomes.expression.visit(this);


        //Verificacao de tipos
        if(becomes.variable.type != null){
            if(becomes.variable.type.equals(becomes.expression.type)){
                becomes.type = becomes.variable.type;
            } else if(becomes.variable.type.equals("real") && becomes.expression.type.equals("integer")){
                becomes.type = becomes.variable.type;
            } else {
                System.out.println("Atribuicao de valores incompatíveis linha:"+becomes.variable.id.line+" coluna:"+becomes.variable.id.col);
            }
        }


    }

    @Override
    public void visitBoolLit(BoolLit boolLit) {
        //
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
                System.out.println("Expressão booleana esperada "+conditional.expression.type);//to do Token linha e coluna
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
                System.out.println("Comparacao entre valores incompativeis linha= "+expression.operator.line);
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
                if( place == null )
                	place = aux.term.type;
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
                                		System.out.println("Operandos invalidos");
                                	}
                                break;
                                case "real":
                                    if ( aux.term.type.equals("boolean") ) {
                                    	System.out.println("Operandos invalidos");
                                    }
                                    place = "real";
                                break;
                                default:
                                    System.out.println("Operandos invalidos");
                            }
                        break;
                        case Token.OR:
                            if(!place.equals("boolean") || !aux.term.type.equals("boolean")){
                                System.out.println("Operandos invalidos linha: "+aux.operator.line);
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
            System.out.println("Expressão booleana esperada "+iterative.expression.type);
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
                System.out.println("Tipo inválido de seletor");
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
                    term.type = aux.factor.type;
                } else if(aux.factor instanceof Literal){
                    ((Literal)aux.factor).visit(this);
                    term.type = aux.factor.type;
                }  else if(aux.factor instanceof Expressao){
                    ((Expressao)aux.factor).visit(this);
                    term.type = aux.factor.type;
                }

                //System.out.println(aux.factor.toString() + "   " + term.type);

//                if(aux.operator != null && aux.operator.value.equals("/")){
//                    term.type = "real";
//                }


                if( place == null || "integer".equals(place))
                	place = term.type;
                if(aux.operator != null){
                    switch(aux.operator.kind){
                        case Token.DIV:
                        	place = "real";
                        	if(term.type.equals("boolean")){
                                System.out.println("Operandos invalidos linha: "+aux.operator.line);
                            }
                        break;
                        case Token.MULT:
                            switch (place) {
                                case "integer":
                                    place = "integer";
                                break;
                                case "real":
                                    place = "real";
                                break;
                                default:
                                    System.out.println("Operandos invalidos");
                                break;
                            }
                        break;
                        case Token.AND:
                            place = "boolean";
                            if(!place.equals(term.type)){
                                System.out.println("Operandos invalidos linha: "+aux.operator.line);
                            }
                        break;
                    }
                }

            }
            aux = aux.next;

        }

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
    }

    @Override
    public void visitTipoSimples(TipoSimples type) {
        type.type = type.typo.value;
    }

    @Override
    public void visitVariavel(Variavel variable) {
       variable.declaration = table.retrieve(variable.id);
       if(variable.declaration != null){
           variable.type = variable.declaration.type.type;
       }

       if(variable.selector != null){
           variable.selector.visit(this);
       }

    }

}
