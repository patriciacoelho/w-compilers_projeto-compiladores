package compiler;

public class Parser {
	private Token currentToken;
	private Scanner scanner;

	private void accept (byte expectedKind) throws Exception {
		if (currentToken.kind == expectedKind)
			currentToken = scanner.scan();
		// else
			// erro sintatico, esperava 'expectedKind'
	}

	private void acceptIt () throws Exception {
		currentToken = scanner.scan();
	}


	///////////////////////////////////////////////////////////////////////////////
	//
	//PARSING METHODS
	//
	///////////////////////////////////////////////////////////////////////////////

	private void parsePrograma() throws Exception {
		// program <id> ; <corpo> .
		currentToken = scanner.scan();
		if (currentToken.kind == Token.PROGRAM) {
			acceptIt();
			accept(Token.ID);
			accept(Token.SEMICOLON);
			parseCorpo();
			accept(Token.DOT);
			if (currentToken.kind != Token.EOF) {
				//erro conteudo apos final de programa
			}
		 } else {
			 //erro não inicia com 'program'
		 }
	}

	private void parseCorpo() throws Exception {
		// <corpo> ::= <declarações> <comando-composto>
		while(currentToken.kind == Token.VAR){
			//  <declarações> ::= <declaração> ; | <declarações> <declaração> ;	| <vazio>
			parseDeclaracao();
			accept(Token.SEMICOLON);
		}
		parseComandoComposto();
	}

	private void parseDeclaracao() throws Exception {
		// <declaração> ::= <declaração-de-variável> ::= var <lista-de-ids> : <tipo>
		if (currentToken.kind == Token.VAR) {
			acceptIt();
			parseListaDeIds();
			accept(Token.COLON);
			parseTipo();
		 } else {
			 // erro, declaração não inicia com 'Var'
		 }
	}

	private void parseListaDeIds() throws Exception {
		// <lista-de-ids> ::= <id>	| <lista-de-ids> , <id>
		if (currentToken.kind == Token.ID) {
			acceptIt();
		} else {
			// erro, nenhum id identificado
		}
		while(currentToken.kind == Token.COMMA){
			acceptIt();
			if (currentToken.kind == Token.ID) {
				acceptIt();
			} else{
				//erro, esperava id após a virgula
			}
		}
	}

	private void parseTipo() throws Exception {
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
			// erro, esperava um tipo válido
		}
	}

	private void parseLiteral() throws Exception {
		//<literal> ::= <bool-lit> | <int-lit> | <float-lit>
		if (currentToken.kind == Token.TRUE || currentToken.kind == Token.FALSE
			|| currentToken.kind == Token.INT_LIT || currentToken.kind == Token.FLOAT_LIT)
			acceptIt();
		else {
			// erro, esperava um literal
		}
	}

	// Revisar a partir daqui
	private void parseComandoComposto() throws Exception {
		//begin <lista-de-comandos> end
		if (currentToken.kind == Token.BEGIN) {
			acceptIt();
			parseListaDeComandos();
			accept(Token.END);
		 }
	}

	private void parseListaDeComandos() throws Exception {
		// <lista-de-comandos> ::=	<comando> ; | <lista-de-comandos> <comando> ; | <vazio>
		parseComando();
		accept(Token.SEMICOLON);
	}

	private void parseComando() throws Exception {
		switch(currentToken.kind){
		case Token.ID: { //atribuicao
			// <atribuição> ::= <variável> := <expressão>
				parseVariavel();
				accept(Token.BECOMES);
				parseExpressao();
			}
			break;
		case Token.WHILE: { //iterativo
			// <iterativo> ::= while <expressão> do <comando>
			acceptIt();
			parseExpressao();
			accept(Token.DO);
			parseComando();
		}
		break;
		case Token.BEGIN: {	//comando-composto
				parseComandoComposto();
			}
			break;
		case Token.IF: { //condicional
			// <condicional> ::= if <expressão> then <comando> (else <comando> |  vazio)
			acceptIt();
			parseExpressao();
			accept(Token.THEN);
			parseComando();
			if(currentToken.kind == Token.ELSE){
				acceptIt();
				parseComando();
			}
		}
		break;
		default:
			//Error
		}
	}

	private void parseVariavel() throws Exception {
		// <variável> ::=	<id> <seletor>
		if(currentToken.kind == Token.ID){
			acceptIt();
			parseSeletor();
		}
	}


	private void parseSeletor() throws Exception {
		// <seletor> ::= <seletor> "[" <expressão> "]" | "[" <expressão> "]" | <vazio>
		if(currentToken.kind == Token.LBRACKET) {
			acceptIt();
			parseExpressao();
			accept(Token.RBRACKET);
		}
	}

	private void parseExpressao() throws Exception {
		// <expressão> ::= <expressão-simples> | <expressão-simples> <op-rel> <expressão-simples>,
		// <op-rel> ::= < | > | <=	| >= | = | <>
		parseExpressaoSimples();
		if(currentToken.kind == Token.GREATER || currentToken.kind == Token.LESS || currentToken.kind == Token.LESS_EQUAL
				|| currentToken.kind == Token.GREATER_EQUAL || currentToken.kind == Token.NOT_EQUAL || currentToken.kind == Token.EQUAL){
			acceptIt();
			parseExpressaoSimples();
		}



	}

	private void parseExpressaoSimples() throws Exception {
		// <expressão-simples> ::= <expressão-simples> <op-ad> <termo> | <termo>, <op-ad> ::= + | - | or
		parseTermo();
		if(currentToken.kind == Token.SUM || currentToken.kind == Token.SUB || currentToken.kind == Token.OR){
			acceptIt();
			parseTermo();
		}

	}

	private void parseTermo() throws Exception {
		// <termo> ::= <termo> <op-mul> <fator> | <fator> , <op-mul> ::= *	| / | and
		parseFator();
		if(currentToken.kind == Token.MULT || currentToken.kind == Token.DIV || currentToken.kind == Token.AND) {
			acceptIt();
			parseFator();
		}
	}

	private void parseFator() throws Exception {
		//<fator> ::= <variável>	| <literal> | "(" <expressão> ")"
		if(currentToken.kind == Token.ID)
			parseVariavel();
		if (currentToken.kind == Token.TRUE || currentToken.kind == Token.FALSE
				|| currentToken.kind == Token.INT_LIT || currentToken.kind == Token.FLOAT_LIT)
			parseLiteral();
		if(currentToken.kind == Token.LPAREN) {
			acceptIt();
			parseExpressao();
			accept(Token.RPAREN);
		}
	}

	public void parse () throws Exception {
		currentToken = scanner.scan();
//		Program progAST = parsePrograma();
		parsePrograma();
		// if (currentToken.kind != Token.EOF)
			// erro sintatico
		// return progAST;
	}
}
