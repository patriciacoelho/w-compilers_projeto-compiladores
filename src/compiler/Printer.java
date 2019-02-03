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
public class Printer implements Visitor{

    int i=0;
    int lvl = 0;
    public void print(Programa program){
        System.out.println("---> Iniciando impressão da Árvore");
        program.visit(this);
    }

    private void indent() {
    	//System.out.print(i);
        for(int x=0; x<=i/2;x++){
        	System.out.print("|  ");
            System.out.print("  ");
        }
    }

    private void subIndent() {
        for(int x=0; x<=lvl - i;x++){
        	System.out.print("|  ");
        }
    }

    @Override
    public void visitAtribuicao(Atribuicao becomes) {
        indent();
        System.out.println("Atribuição");
        indent();
        if(becomes.variable != null){
            i++; if (i > lvl) lvl = i;
            becomes.variable.visit(this);
            i--;
        }
        if(becomes.expression != null ){
            i++; if (i > lvl) lvl = i;
            becomes.expression.visit(this);
            i--;
        }

    }

    @Override
    public void visitBoolLit(BoolLit boolLit) {
        indent();
        System.out.println("Bool Lit");
        indent();
        System.out.println(boolLit.name.value);
    }

    @Override
    public void visitComandoComposto(ComandoComposto compositeCommands) {
        indent();
        System.out.println("Comando Composto");
        indent();
        if(compositeCommands.listOfCommands != null){
            i++; if (i > lvl) lvl = i;
            compositeCommands.listOfCommands.visit(this);
            i--;
        }

    }

    @Override
    public void visitCondicional(Condicional conditional) {
        indent();
        System.out.println("Condicional");
        indent();
        if(conditional.expression != null){
            i++; if (i > lvl) lvl = i;
            conditional.expression.visit(this);
            i--;
        }


        if(conditional.command instanceof Atribuicao){
            i++; if (i > lvl) lvl = i;
            ((Atribuicao)conditional.command).visit(this);
            i--;
        } else if(conditional.command instanceof ComandoComposto){
            i++; if (i > lvl) lvl = i;
            ((ComandoComposto)conditional.command).visit(this);
            i--;
        } else if(conditional.command instanceof Iterativo){
            i++; if (i > lvl) lvl = i;
            ((Iterativo)conditional.command).visit(this);
            i--;
        } else if(conditional.command instanceof Condicional){
            i++; if (i > lvl) lvl = i;
            ((Condicional)conditional.command).visit(this);
            i--;
        }

        if(conditional.commandElse instanceof Atribuicao){
            i++; if (i > lvl) lvl = i;
            ((Atribuicao)conditional.commandElse).visit(this);
            i--;
        } else if(conditional.commandElse instanceof ComandoComposto){
            i++; if (i > lvl) lvl = i;
            ((ComandoComposto)conditional.commandElse).visit(this);
            i--;
        } else if(conditional.commandElse instanceof Iterativo){
            i++; if (i > lvl) lvl = i;
            ((Iterativo)conditional.commandElse).visit(this);
            i--;
        } else if(conditional.commandElse instanceof Condicional){
            i++; if (i > lvl) lvl = i;
            ((Condicional)conditional.commandElse).visit(this);
            i--;
        }
    }

    @Override
    public void visitCorpo(Corpo body) {
        indent();
        System.out.println("Corpo");
        indent();
        if(body != null){
            i++; if (i > lvl) lvl = i;
            body.declarations.visit(this);
            body.compositeCommand.visit(this);
            i--;
        }


    }

    @Override
    public void visitDeclaracaoDeVariavel(DeclaracaoDeVariavel variableDeclaration) {
        indent();
        System.out.println("Declaracao de Variavel");
        indent();
        if(variableDeclaration != null){
            i++; if (i > lvl) lvl = i;
            variableDeclaration.listOfIds.visit(this);
            i--;
            if(variableDeclaration.type instanceof TipoAgregado){
                i++; if (i > lvl) lvl = i;
                ((TipoAgregado)variableDeclaration.type).visit(this);
                i--;
            } else {
                if(variableDeclaration.type instanceof TipoSimples){
                    i++; if (i > lvl) lvl = i;
                    ((TipoSimples)variableDeclaration.type).visit(this);
                    i--;
                }
            }
        }

        //variableDeclaration.type.visit(this);
    }

    @Override
    public void visitDeclaracoes(Declaracoes declarations) {
        indent();
        System.out.println("Declaracoes");
        indent();
        Declaracoes aux = declarations;
        while(aux != null){
            i++; if (i > lvl) lvl = i;
            aux.declarationOfVariable.visit(this);
            i--;
            aux = aux.next;
        }
    }

    @Override
    public void visitExpressao(Expressao expression) {

        indent();
        System.out.println("Expressao");
        indent();
        if(expression.simpleExpression != null){
            i++; if (i > lvl) lvl = i;
            expression.simpleExpression.visit(this);
            i--;
        }
        if(expression.operator != null){
            indent();
            System.out.println(expression.operator.value);
            indent();
        }
        if(expression.simpleExpressionR != null){
            i++; if (i > lvl) lvl = i;
            expression.simpleExpressionR.visit(this);
            i--;
        }

    }

    @Override
    public void visitExpressaoSimples(ExpressaoSimples simpleExpression) {
        indent();
        System.out.println("Expressao Simples");
        indent();
        ExpressaoSimples aux = simpleExpression;
        while(aux != null){
            if(aux.word != null){
                i++; if (i > lvl) lvl = i;
                aux.word.visit(this);
                i--;
            }
            if(aux.operator != null){
                indent();
                System.out.println(aux.operator.value);
            }
            aux = aux.next;
        }
    }

    @Override
    public void visitIterativo(Iterativo iterative) {
        indent();
        System.out.println("Iterativo");
        indent();
        i++; if (i > lvl) lvl = i;
        iterative.expression.visit(this);
        i--;
        if(iterative.command instanceof Atribuicao){
            i++; if (i > lvl) lvl = i;
            ((Atribuicao)iterative.command).visit(this);
            i--;
        } else if(iterative.command instanceof ComandoComposto){
            i++; if (i > lvl) lvl = i;
            ((ComandoComposto)iterative.command).visit(this);
            i--;
        } else if(iterative.command instanceof Iterativo){
            i++; if (i > lvl) lvl = i;
            ((Iterativo)iterative.command).visit(this);
            i--;
        } else if(iterative.command instanceof Condicional){
            i++; if (i > lvl) lvl = i;
            ((Condicional)iterative.command).visit(this);
            i--;
        }

    }

    @Override
    public void visitListaDeComandos(ListaDeComandos listOfCommands) {
        indent();
        System.out.println("Lista de Comandos");
        indent();
        ListaDeComandos aux = listOfCommands;

        while(aux != null){
            if(aux.command != null){
                if(aux.command instanceof Atribuicao){
                    i++; if (i > lvl) lvl = i;
                    ((Atribuicao)aux.command).visit(this);
                    i--;
                } else if(aux.command instanceof ComandoComposto){
                    i++; if (i > lvl) lvl = i;
                    ((ComandoComposto)aux.command).visit(this);
                    i--;
                } else if(aux.command instanceof Iterativo){
                    i++; if (i > lvl) lvl = i;
                    ((Iterativo)aux.command).visit(this);
                    i--;
                } else if(aux.command instanceof Condicional){
                    i++; if (i > lvl) lvl = i;
                    ((Condicional)aux.command).visit(this);
                    i--;
                }
            }
            aux = aux.next;
        }
    }

    @Override
    public void visitListaDeIds(ListaDeIds listOfIds) {
        indent();
        System.out.println("Lista De Ids");
        if(listOfIds != null){
            ListaDeIds aux = listOfIds;
            while(aux != null){
                indent();
                System.out.println(aux.id.value);
                aux = aux.next;
            }
        }
    }

    @Override
    public void visitLiteral(Literal literal) {
        indent();
        System.out.println("Literal");
        if(literal.name != null){
            indent();
            System.out.println(literal.name.value);
        }

    }

    @Override
    public void visitPrograma(Programa program) {
        System.out.println("Programa");
        if(program != null){
            if(program.body != null){
                i++; if (i > lvl) lvl = i;
                program.body.visit(this);
                i--;
            }
        }
    }

    @Override
    public void visitSeletor(Seletor selector) {
        indent();
        System.out.println("Seletor");
        indent();
        Seletor aux = selector;
        while(aux!= null){
            i++; if (i > lvl) lvl = i;
            aux.expression.visit(this);
            i--;
            aux = aux.next;
        }
    }

    @Override
    public void visitTermo(Termo term) {
        indent();
        System.out.println("Termo");
        indent();
        Termo aux = term;
        while(aux != null){
            if(aux.factor instanceof Variavel){
                i++;
                if (i > lvl)
                	lvl = i;
                ((Variavel)aux.factor).visit(this);
                i--;
            } else if(aux.factor instanceof Literal){
                i++;
                if (i > lvl)
                	lvl = i;
                ((Literal)aux.factor).visit(this);
                i--;
            }  else if(aux.factor instanceof Expressao){
                i++;
                if (i > lvl)
                	lvl = i;
                ((Expressao)aux.factor).visit(this);
                i--;
            }
            if(aux.operator != null){
                indent();
                System.out.println(aux.operator);
                indent();
            }
            aux = aux.next;
        }


    }

    @Override
    public void visitTipoAgregado(TipoAgregado type) {
        indent();
        System.out.println("Tipo Agregado");
        indent();
        System.out.println(type.literal1.name.value);
        indent();
        System.out.println(type.literal2.name.value);
        indent();
        if(type.typo instanceof TipoAgregado){
                i++;
                if (i > lvl)
                	lvl = i;
                ((TipoAgregado)type.typo).visit(this);
                i--;
        } else {
            if(type.typo instanceof TipoSimples){
                i++;
                if (i > lvl)
                	lvl = i;
                ((TipoSimples)type.typo).visit(this);
                i--;
            }
        }
    }

    @Override
    public void visitTipoSimples(TipoSimples type) {
        indent();
        System.out.println("Tipo Simples");
        if(type.typo != null){
            indent();
            System.out.println(type.typo.value);
        }

    }

    @Override
    public void visitVariavel(Variavel variable) {
        indent();
        System.out.println("Variavel");
        indent();
        System.out.println(variable.id.value);
        indent();
        if(variable.selector != null){
            i++;
            if (i > lvl)
            	lvl = i;
            variable.selector.visit(this);
            i--;
        }

    }



}
