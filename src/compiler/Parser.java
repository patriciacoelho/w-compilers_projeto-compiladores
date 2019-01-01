package compiler;

public class Parser {
	private Token currentToken;
	private Scanner scanner;

	private void accept (byte expectedKind) {
		if (currentToken.kind == expectedKind)
			currentToken = scanner.scan();
		// else
			// erro sintatico
	}

	private void acceptIt () {
		currentToken = scanner.scan();
	}

	// Parsing Methods
	private void parsePrograma() {
		// program <id> ; <corpo> .
		if (currentToken.kind == Token.PROGRAM) {
			acceptIt();
			parseId();
			accept(Token.SEMICOLON);
			parseCorpo();
			accept(Token.DOT);
		 }
	}

	private void parseCorpo() {
		// <corpo> ::= <declara��es> <comando-composto
		parseDeclaracoes();
		parseComandoComposto();
	}

	private void parseDeclaracoes() {
		//  <declara��es> ::= <declara��o> ; | <declara��es> <declara��o> ;	| <vazio>
		parseDeclaracao();
		accept(Token.COMMA);
	}

	private void parseDeclaracao() {
		// <declara��o> ::= <declara��o-de-vari�vel>
		parseDeclaracaoDeVariavel();
	}

	private void parseDeclaracaoDeVariavel() {
		// <declara��o-de-vari�vel> ::= var <lista-de-ids> : <tipo>
		if (currentToken.kind == Token.VAR) {
			acceptIt();
			parseListaDeIds();
			accept(Token.COLON);
			parseTipo();
		 }
	}

	private void parseListaDeIds(){
		// <lista-de-ids> ::= <id>	| <lista-de-ids> , <id>
		parseId();
		accept(Token.COMMA);
	}

	private void parseTipo(){
		if (currentToken.kind == Token.ARRAY) {
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
		if (currentToken.kind == Token.INTEGER || currentToken.kind == Token.REAL || currentToken.kind == Token.BOOLEAN) {
			// <tipo-simples> ::= integer | real | boolean
			acceptIt();
		 }
	}

	private void parseLiteral(){
		//<literal> ::= <bool-lit> | <int-lit> | <float-lit>
		if (currentToken.kind == Token.TRUE || currentToken.kind == Token.FALSE
			|| currentToken.kind == Token.INT_LIT || currentToken.kind == Token.FLOAT_LIT)
			acceptIt();

	}

	private void parseComandoComposto() {
		//begin <lista-de-comandos> end
		if (currentToken.kind == Token.BEGIN) {
			acceptIt();
			parseListaDeComandos();
			accept(Token.END);
		 }
	}

	private void parseListaDeComandos() {
		// <lista-de-comandos> ::=	<comando> ; | <lista-de-comandos> <comando> ; | <vazio>
		parseComando();
		accept(Token.SEMICOLON);
	}

	private void parseComando() {
		switch(currentToken.kind){
		case Token.ID: { //atribuicao
			// <atribui��o> ::= <vari�vel> := <express�o>
				parseVariavel();
				accept(Token.BECOMES);
				parseExpressao();
			}
			break;
		case Token.WHILE: { //iterativo
			// <iterativo> ::= while <express�o> do <comando>
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
			// <condicional> ::= if <express�o> then <comando> (else <comando> |  vazio)
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

	private void parseVariavel() {
		// <vari�vel> ::=	<id> <seletor>
		if(currentToken.kind == Token.ID){
			acceptIt();
			parseSeletor();
		}
	}


	private void parseSeletor() {
		// <seletor> ::= <seletor> "[" <express�o> "]" | "[" <express�o> "]" | <vazio>
		if(currentToken.kind == Token.LBRACKET) {
			acceptIt();
			parseExpressao();
			accept(Token.RBRACKET);
		}
	}

	private void parseExpressao() {
		// ...
	}

	public void parse () {
		currentToken = scanner.scan();
//		Program progAST = parseProgram();
//		parseProgram();
		// if (currentToken.kind != Token.EOF)
			// erro sintatico
		// return progAST;
	}
}
