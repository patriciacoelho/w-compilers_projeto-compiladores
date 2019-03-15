package compiler;

import AST.Atribuicao;
import AST.BoolLit;
import AST.ComandoComposto;
import AST.Corpo;
import AST.Comando;
import AST.Condicional;
import AST.DeclaracaoDeVariavel;
import AST.Declaracoes;
import AST.Variavel;
import AST.Expressao;
import AST.ExpressaoSimples;
import AST.Fator;
import AST.Iterativo;
import AST.ListaDeComandos;
import AST.ListaDeIds;
import AST.Literal;
import AST.Programa;
import AST.Seletor;
import AST.Termo;
import AST.Tipo;
import AST.TipoAgregado;
import AST.TipoSimples;

public class Parser {
	private Token currentToken;
    private Token lastToken;
	private Scanner scanner;

    public Parser(){

    }

    public Programa parse(String fileName) throws Exception{
        Programa program;
        scanner = new Scanner(fileName);
        currentToken = this.scanner.scan();
        System.out.println("---> INICIANDO ANALISE SINTATICA\n");
        program = parsePrograma();
        System.out.println("> CONCLUIDA COM SUCESSO\n");
        return program;
    }

	private void accept (byte expectedKind) throws Exception{
		if (currentToken.kind == expectedKind){
            lastToken = currentToken;
			currentToken = scanner.scan();
		}
        else {
        	System.out.print("ERRO SINTATICO: ");
            System.out.println("Esperava encontrar '"+ Token.SPELLINGS[expectedKind]+"' [lin: " + lastToken.line+ "; col: "+lastToken.col+"].");
            System.exit(1);
        }
	}

	private void acceptIt () throws Exception{
		currentToken = scanner.scan();
	}


	///////////////////////////////////////////////////////////////////////////////
	//
	//PARSING METHODS
	//
	///////////////////////////////////////////////////////////////////////////////

        private Atribuicao parseAtribuicao() throws Exception{
            //System.out.println("Parse Atribuicao");
            //Atribuicao == <Variavel> := <Expressao>
            Atribuicao becomes = new Atribuicao();
            becomes.variable = parseVariavel();
            accept(Token.BECOMES);
            becomes.expression = parseExpressao();
            return becomes;
        }

        private BoolLit parseBoolLit() throws Exception{
            //System.out.println("Parse Bool Lit");
            BoolLit logic = new BoolLit();
            logic.name = currentToken;
            acceptIt();
            return logic;
        }

        private Comando parseComando() throws Exception {
                //System.out.println("Parse Comando");
                Comando command;
		switch(currentToken.kind){
            case Token.ID: //atribuicao
                command = parseAtribuicao();
            break;
            case Token.IF:  //condicional
                command = parseCondicional();
            break;
            case Token.WHILE: 	//iterativo
                command = parseIterativo();
            break;
            case Token.BEGIN:
                command = parseComandoComposto();
            break;
            default:
                command = null;
                System.out.print("ERRO SINTATICO: ");
                System.out.println("Esperava encontrar comando após a linha " + lastToken.line+ ". \nNão esperava encontrar '"+
                					currentToken.value+"' após o '"+lastToken.value+"'.");
                System.exit(1);
		}
                return command;
	}

	private ComandoComposto parseComandoComposto() throws Exception {
		//begin <lista-de-comandos> end
                //System.out.println("Parse Comando Composto ");
                ComandoComposto compositeCommand = new ComandoComposto();
                accept(Token.BEGIN);
                compositeCommand.listOfCommands = parseListaDeComandos();
                accept(Token.END);

                return compositeCommand;
	}

        private Condicional parseCondicional() throws Exception{
            //System.out.println("Parse Condicional");
            Condicional conditional = new Condicional();
            accept(Token.IF);
            conditional.expression = parseExpressao();
            accept(Token.THEN);
            conditional.command = parseComando();
            if(currentToken.kind == Token.ELSE){
                acceptIt();
                conditional.commandElse = parseComando();
            } else{
                conditional.commandElse = null;
            }
            return conditional;
        }

        private Corpo parseCorpo() throws Exception {
		// <corpo> ::= <declaraes> <comando-composto>
                //System.out.println("Parse Corpo");
                Corpo body = new Corpo();
                body.declarations = parseDeclaracoes();
                body.compositeCommand = parseComandoComposto();
                return body;
	}

        private DeclaracaoDeVariavel parseDeclaracaoDeVariavel() throws Exception{
            //System.out.println("Parse declaracao de variavel ");
            DeclaracaoDeVariavel variableDeclaration = new DeclaracaoDeVariavel();
            accept(Token.VAR);
            variableDeclaration.listOfIds = parseListaDeIds();
            accept(Token.COLON);
            variableDeclaration.type = parseTipo();

            return variableDeclaration;
        }

        private Declaracoes parseDeclaracoes() throws Exception{
            //System.out.println("Parse Declaracoes");
            //DeclaracaoDeVariavel dec = NULL;
            Declaracoes declarations = null;
            while(currentToken.kind == Token.VAR){
                Declaracoes aux = new Declaracoes();
                aux.declarationOfVariable = parseDeclaracaoDeVariavel();
                aux.next = null;
                accept(Token.SEMICOLON);

                if( declarations == null){
                    declarations = aux;
                } else {
                    Declaracoes aux2 = declarations;
                    while(aux2.next != null){
                        aux2 = aux2.next;
                    }
                    aux2.next = aux;
                }

            }
            return declarations;
        }

        private Expressao parseExpressao() throws Exception {
		// <expresso> ::= <expresso-simples> | <expresso-simples> <op-rel> <expresso-simples>,
		// <op-rel> ::= < | > | <=	| >= | = | <>
                //System.out.println("Parse Expressao");
                Expressao expression = new Expressao();
		expression.simpleExpression = parseExpressaoSimples();
		if(currentToken.kind == Token.GREATER || currentToken.kind == Token.LESS || currentToken.kind == Token.LESS_EQUAL
				|| currentToken.kind == Token.GREATER_EQUAL || currentToken.kind == Token.DIFF || currentToken.kind == Token.EQUAL){
			expression.operator = currentToken;
                        acceptIt();
			expression.simpleExpressionR = parseExpressaoSimples();
		} else{
                    expression.simpleExpressionR = null;
                    expression.operator = null;
                }

                return expression;
	}

        private ExpressaoSimples parseExpressaoSimples() throws Exception {
		// <expresso-simples> ::= <expresso-simples> <op-ad> <termo> | <termo>, <op-ad> ::= + | - | or
                //System.out.println("Parse Expressao Simples");
                ExpressaoSimples simpleExpression = new ExpressaoSimples();
		simpleExpression.term = parseTermo();
                simpleExpression.operator = null;
                simpleExpression.next = null;



		while(currentToken.kind == Token.SUM || currentToken.kind == Token.SUB || currentToken.kind == Token.OR){
			ExpressaoSimples aux = new ExpressaoSimples();
                        aux.operator = currentToken;
                        acceptIt();

			aux.term = parseTermo();
                        aux.next = null;


                        if(simpleExpression.next == null){
                            simpleExpression.next = aux;
                        } else{
                            ExpressaoSimples aux2 = simpleExpression;
                            while(aux2.next != null){
                                aux2 = aux2.next;
                            }
                            aux2.next = aux;
                        }
		}

                return simpleExpression;

	}

    private Fator parseFator() throws Exception {
    	//<fator> ::= <varivel>	| <literal> | "(" <expresso> ")"
        //System.out.println("Parse Fator");
        Fator factor;
        switch(currentToken.kind){
            case Token.ID:
                factor = parseVariavel();
            break;
            case Token.TRUE:
            case Token.FALSE:
            case Token.INT_LIT:
            case Token.FLOAT_LIT:
                factor = parseLiteral();
            break;
            case Token.LPAREN:
                acceptIt();
                factor = parseExpressao();
                accept(Token.RPAREN);
            break;
            default:
                factor = null;
                System.out.print("ERRO SINTATICO: ");
                System.out.println("Esperava encontrar um operando válido [lin: " + lastToken.line+ "; col: "+lastToken.col+"]. \nO fator '"+
                					currentToken.value+"' encontrado não é um fator aceito pela linguagem.");
                System.exit(1);
        }
        return factor;
	}

        private Iterativo parseIterativo() throws Exception{
            //System.out.println("Parse Iterativos");
            Iterativo iterative = new Iterativo();
            accept(Token.WHILE);
            iterative.expression = parseExpressao();
            accept(Token.DO);
            iterative.command = parseComando();

            return iterative;
        }

        private ListaDeComandos parseListaDeComandos() throws Exception {
		// <lista-de-comandos> ::=	<comando> ; | <lista-de-comandos> <comando> ; | <vazio>
                //System.out.println("Parse Lista de comandos");
                ListaDeComandos listOfCommands = null;
		while(currentToken.kind==Token.ID || currentToken.kind==Token.IF || currentToken.kind==Token.WHILE || currentToken.kind==Token.BEGIN){
                    ListaDeComandos aux = new ListaDeComandos();
                    aux.command = parseComando();
                    aux.next = null;
                    accept(Token.SEMICOLON);

                    if(listOfCommands == null){
                        listOfCommands = aux;
                    } else {
                        ListaDeComandos aux2 = listOfCommands;
                        while(aux2.next != null){
                            aux2 = aux2.next;
                        }
                        aux2.next = aux;
                    }
                }
                return listOfCommands;
	}

        private ListaDeIds parseListaDeIds() throws Exception {
		// <lista-de-ids> ::= <id>	| <lista-de-ids> , <id>
                //System.out.println("Parse lista de ids");
                ListaDeIds listOfIds = new ListaDeIds();
                listOfIds.id = currentToken;
                listOfIds.next = null;
                accept(Token.ID);
		while(currentToken.kind == Token.COMMA){
                        acceptIt();
                        ListaDeIds aux = new ListaDeIds();
			aux.id = currentToken;
                        aux.next = null;
			accept(Token.ID);

                        ListaDeIds aux2;
                        if(listOfIds.next == null){
                            listOfIds.next = aux;
                        } else{
                            aux2 = listOfIds;
                            while(aux2.next != null){
                                aux2 = aux2.next;
                            }
                            aux2.next = aux;
                        }
		}

                return listOfIds;
	}

    private Literal parseLiteral() throws Exception {
		//<literal> ::= <bool-lit> | <int-lit> | <float-lit>
        //System.out.println("Parse literal");
        Literal literal = new Literal();
        switch(currentToken.kind){
            case Token.TRUE:
            case Token.FALSE:
                literal = parseBoolLit();
            break;
            case Token.INT_LIT:
                literal.name = currentToken;
                acceptIt();
            break;
            case Token.FLOAT_LIT:
                literal.name = currentToken;
                acceptIt();
            break;
            default:
                literal = null;
                System.out.print("ERRO SINTATICO: ");
                System.out.println("Esperava encontrar um literal como limite do array ao invés de '"+
                					currentToken.value+"' [lin: " + lastToken.line+ "; col: "+lastToken.col+"].");
                System.exit(1);
        }
        return literal;
	}

	private Programa parsePrograma() throws Exception {
		// program <id> ; <corpo> .
                //System.out.println("Parse Programa");
                Programa program = new Programa();
                accept(Token.PROGRAM);
                accept(Token.ID);
                accept(Token.SEMICOLON);
                program.body = parseCorpo();
                accept(Token.DOT);
                accept(Token.EOF);
                return program;
	}

	private Seletor parseSeletor() throws Exception {
		// <seletor> ::= <seletor> "[" <expresso> "]" | "[" <expresso> "]" | <vazio>
                //System.out.println("Parse Seletor");
                Seletor selector = null;

		while(currentToken.kind == Token.LBRACKET) {
			acceptIt();
                        Seletor aux = new Seletor();
			aux.expression = parseExpressao();
                        aux.next = null;
			accept(Token.RBRACKET);

                        if(selector == null){
                            selector = aux;
                        } else {
                            Seletor aux2 = selector;
                            while(aux2.next != null){
                                aux2 = aux2.next;
                            }
                            aux2.next = aux;
                        }
		}
                return selector;
	}

    private Termo parseTermo() throws Exception {
		// <termo> ::= <termo> <op-mul> <fator> | <fator> , <op-mul> ::= *	| / | and
        //System.out.println("Parse Termo");
        Termo term = new Termo();
		term.factor = parseFator();
        term.operator = null;
        term.next = null;
		while(currentToken.kind == Token.MULT || currentToken.kind == Token.DIV || currentToken.kind == Token.AND) {
			Termo aux = new Termo();
            aux.operator = currentToken;
            acceptIt();

			aux.factor = parseFator();
            aux.next = null;

            if(term.next == null){
                term.next = aux;
            } else{
                Termo aux2 = term;
                while(aux2.next != null){
                    aux2 = aux2.next;
                }
                aux2.next = aux;
            }
		}
                return term;
	}

        private Tipo parseTipo() throws Exception {
            //System.out.println("Parse Tipo");
                Tipo typex;
		switch(currentToken.kind){
                    case Token.ARRAY: {
			// <tipo-agregado> ::= array [ <literal> .. <literal> ] of <tipo>
                            TipoAgregado type = new TipoAgregado();
				acceptIt();
				accept(Token.LBRACKET);
				type.literal1 = parseLiteral();
				accept(Token.DOUBLE_DOT);
				type.literal2 = parseLiteral();
				accept(Token.RBRACKET);
				accept(Token.OF);
				type.typo = parseTipo();
                                typex = type;
			}
			break;
                    case Token.INTEGER:
                    case Token.REAL:
                    case Token.BOOLEAN: {
			// <tipo-simples> ::= integer | real | boolean
                                TipoSimples type = new TipoSimples();
                                type.typo = currentToken;
				acceptIt();
                                typex = type;
			}
			break;
                    default:
                        typex = null;
                        System.out.println("erro, esperava um tipo valido");
                        System.exit(1);
			// erro, esperava um tipo vlido
		}
                return typex;
	}

	private Variavel parseVariavel() throws Exception {
		// <varivel> ::=	<id> <seletor>
        //System.out.println("Parse variavel");
        Variavel variable = new Variavel();
        variable.id = currentToken;
        accept(Token.ID);
		variable.selector = parseSeletor();

		return variable;
	}

}
