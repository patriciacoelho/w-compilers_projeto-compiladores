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
import AST.Iterativo;
import AST.Programa;

public class Parser {
	private Token currentToken;
        private Token lastToken;
	private final Scanner scanner;

        Parser(Scanner scanner) throws Exception{
            this.scanner = scanner;
            currentToken = this.scanner.scan();
        }
        
        public Programa parse() throws Exception{
            Programa program;
            System.out.println("---> Iniciando análise Sintática");
            program = parsePrograma();
            return program;
        }
        
	private void accept (byte expectedKind) throws Exception{
		if (currentToken.kind == expectedKind){
                        lastToken = currentToken;
			currentToken = scanner.scan();
                }
                else {
			// erro sintatico, esperava 'expectedKind'
                        System.out.println("erro sintatico");
                        System.out.println("Esperava encontrar "+ Token.SPELLINGS[expectedKind]+"\n linha:" + lastToken.line+ "\n col:"+lastToken.col);
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
            switch(currentToken.kind){
                case Token.TRUE:
                case Token.FALSE:
                    logic.name = currentToken;
                    acceptIt();
                break;
                default:
                    System.out.println("Unexpected Character");
                
            }
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
			//Error
                        command = null;
                        System.out.println("Unexpected Symbol = "+currentToken.value);
                        System.exit(1);
		}
                return command;
	}
        
	private ComandoComposto parseComandoComposto() throws Exception {
		//begin <lista-de-comandos> end
                //System.out.println("Parse Comando Composto ");
                ComandoComposto compositeCommand = new ComandoComposto();
                accept(Token.BEGIN);
                compositeCommand.lc = parseListaDeComandos();
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
                body.d = parseDeclaracoes();
                body.compositeCommand = parseComandoComposto();
                return body;
	}
        
        private DeclaracaoDeVariavel parseDeclaracaoDeVariavel() throws Exception{
            //System.out.println("Parse declaracao de variavel ");
            DeclaracaoDeVariavel variableDeclaration = new DeclaracaoDeVariavel();
            accept(Token.VAR);
            variableDeclaration.li = parseListaDeIds();
            accept(Token.COLON);
            variableDeclaration.t = parseTipo();
            
            return variableDeclaration;
        }
        
        private Declaracoes parseDeclaracoes() throws Exception{
            //System.out.println("Parse Declaracoes");
            //DeclaracaoDeVariavel dec = NULL;
            Declaracoes first,last, d;
            first = null;
            last = null;
            DeclaracaoDeVariavel dv;
            while(currentToken.kind == Token.VAR){
                d = new Declaracoes();
                d.declarationOfVariable = parseDeclaracaoDeVariavel();
                d.next = null;
                accept(Token.SEMICOLON);
                
                if(first==null){
                    first = d;
                } else { 
                    last.next = d;
                }
                last = d;    
            }
            return first;
        }
        
        private Expressao parseExpressao() throws Exception {
		// <expresso> ::= <expresso-simples> | <expresso-simples> <op-rel> <expresso-simples>,
		// <op-rel> ::= < | > | <=	| >= | = | <>
                //System.out.println("Parse Expressao");
                Expressao expression = new Expressao();
		expression.simpleExpression = parseExpressaoSimples();
		if(currentToken.kind == Token.GREATER || currentToken.kind == Token.LESS || currentToken.kind == Token.LESS_EQUAL
				|| currentToken.kind == Token.GREATER_EQUAL || currentToken.kind == Token.DIFF || currentToken.kind == Token.EQUAL){
			acceptIt();
			expression.simpleExpressionR = parseExpressaoSimples();
		} else{
                    expression.simpleExpressionR = parseExpressaoSimples();
                }
                
                return expression;
	}
        
        private ExpressaoSimples parseExpressaoSimples() throws Exception {
		// <expresso-simples> ::= <expresso-simples> <op-ad> <termo> | <termo>, <op-ad> ::= + | - | or
                //System.out.println("Parse Expressao Simples");
                
		parseTermo();
                
                
                
		while(currentToken.kind == Token.SUM || currentToken.kind == Token.SUB || currentToken.kind == Token.OR){
			acceptIt();
			parseTermo();
		}

	}
        
        private void parseFator() throws Exception {
		//<fator> ::= <varivel>	| <literal> | "(" <expresso> ")"
                //System.out.println("Parse Fator");
                switch(currentToken.kind){
                    case Token.ID:
                        parseVariavel();
                    break;
                    case Token.TRUE:
                    case Token.FALSE:
                    case Token.INT_LIT:
                    case Token.FLOAT_LIT:
                        parseLiteral();
                    break;
                    case Token.LPAREN:
                        acceptIt();
                        parseExpressao();
                        accept(Token.RPAREN);
                    break;
                    default:
                        System.out.println("Erro");
                        System.exit(1);
                        
                }
	}
        
        private Iterativo parseIterativo() throws Exception{
            //System.out.println("Parse Iterativos");
            Iterativo iterative = new Iterativo();
            accept(Token.WHILE);
            iterative.expression = parseExpressao();
            accept(Token.DO);
            iterative.Command = parseComando();
            
            return iterative;
        }
        
        private void parseListaDeComandos() throws Exception {
		// <lista-de-comandos> ::=	<comando> ; | <lista-de-comandos> <comando> ; | <vazio>
                //System.out.println("Parse Lista de comandos");
		while(currentToken.kind==Token.ID || currentToken.kind==Token.IF || currentToken.kind==Token.WHILE || currentToken.kind==Token.BEGIN){
                    parseComando();
                    accept(Token.SEMICOLON);
                }
	}
        
        private void parseListaDeIds() throws Exception {
		// <lista-de-ids> ::= <id>	| <lista-de-ids> , <id>
                //System.out.println("Parse lista de ids");
                accept(Token.ID);
		while(currentToken.kind == Token.COMMA){
			acceptIt();
			accept(Token.ID);
		}
	}
        
        private void parseLiteral() throws Exception {
		//<literal> ::= <bool-lit> | <int-lit> | <float-lit>
                //System.out.println("Parse literal");
                switch(currentToken.kind){
                    case Token.TRUE:
                    case Token.FALSE:
                        parseBoolLit();
                    break;
                    case Token.INT_LIT:
                        acceptIt();
                    break;
                    case Token.FLOAT_LIT:
                        acceptIt();
                    break;
                    default:
                        System.out.println("Erro");
                        System.exit(1);
                }
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

	private void parseSeletor() throws Exception {
		// <seletor> ::= <seletor> "[" <expresso> "]" | "[" <expresso> "]" | <vazio>
                //System.out.println("Parse Seletor");
		while(currentToken.kind == Token.LBRACKET) {
			acceptIt();
			parseExpressao();
			accept(Token.RBRACKET);
		}
	}
        
        private void parseTermo() throws Exception {
		// <termo> ::= <termo> <op-mul> <fator> | <fator> , <op-mul> ::= *	| / | and
                //System.out.println("Parse Termo");
		parseFator();
		while(currentToken.kind == Token.MULT || currentToken.kind == Token.DIV || currentToken.kind == Token.AND) {
			acceptIt();
			parseFator();
		}
	}
	
        private void parseTipo() throws Exception {
            //System.out.println("Parse Tipo");
		switch(currentToken.kind){
                    case Token.ARRAY: {
			// <tipo-agregado> ::= array [ <literal> .. <literal> ] of <tipo>
				acceptIt();
				accept(Token.LBRACKET);
				parseLiteral();
				accept(Token.DOUBLE_DOT);
				parseLiteral();
				accept(Token.RBRACKET);
				accept(Token.OF);
				parseTipo();
			}
			break;
                    case Token.INTEGER:
                    case Token.REAL:
                    case Token.BOOLEAN: {
			// <tipo-simples> ::= integer | real | boolean
				acceptIt();
			}
			break;
                    default:
                        System.out.println("erro, esperava um tipo vlido");
                        System.exit(1);
			// erro, esperava um tipo vlido
		}
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
