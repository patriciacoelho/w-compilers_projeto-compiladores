package compiler;

public class Parser {
	private Token currentToken;
        private Token lastToken;
	private final Scanner scanner;

        Parser(Scanner scanner) throws Exception{
            this.scanner = scanner;
            currentToken = this.scanner.scan();
        }
        
        public void parse() throws Exception{
           parsePrograma();
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
        
        private void parseAtribuicao() throws Exception{
            parseVariavel();
            accept(Token.BECOMES);
            parseExpressao();
        }
        
        private void parseBoolLit() throws Exception{
            switch(currentToken.kind){
                case Token.TRUE:
                case Token.FALSE:
                    acceptIt();
            }
        }
        
        private void parseComando() throws Exception {
		switch(currentToken.kind){
                    case Token.ID: //atribuicao
                        parseAtribuicao();
                    break;
                    case Token.IF:  //condicional
                        parseCondicional();
                    break;
                    case Token.WHILE: 	//iterativo
                        parseIterativo();
                    break;
                    case Token.BEGIN: 
                        parseComandoComposto();
                    break;
                    default:
			//Error
                        System.out.println("Unexpected Symbol = "+currentToken.value);
                        System.exit(1);
		}
	}
        
	private void parseComandoComposto() throws Exception {
		//begin <lista-de-comandos> end
                accept(Token.BEGIN);
                parseListaDeComandos();
                accept(Token.END);
	}
        
        private void parseCondicional() throws Exception{
            accept(Token.IF);
            parseExpressao();
            accept(Token.THEN);
            parseComando();
            if(currentToken.kind == Token.ELSE){
                acceptIt();
                parseComando();
            }
        }
        
        private void parseCorpo() throws Exception {
		// <corpo> ::= <declaraes> <comando-composto>
                parseDeclaracoes();
                parseComandoComposto();
	}
        
        private void parseDeclaracao() throws Exception {
		// <declarao> ::= <declarao-de-varivel> ::= var <lista-de-ids> : <tipo>
		parseDeclaracaoDeVariavel();
	}
        
        private void parseDeclaracaoDeVariavel() throws Exception{
            accept(Token.VAR);
            parseListaDeIds();
            accept(Token.COLON);
            parseTipo();
        }
        
        private void parseDeclaracoes() throws Exception{
            while(currentToken.kind == Token.VAR){
                parseDeclaracao();
                accept(Token.SEMICOLON);
            }
        }
        
        private void parseExpressao() throws Exception {
		// <expresso> ::= <expresso-simples> | <expresso-simples> <op-rel> <expresso-simples>,
		// <op-rel> ::= < | > | <=	| >= | = | <>
		parseExpressaoSimples();
		if(currentToken.kind == Token.GREATER || currentToken.kind == Token.LESS || currentToken.kind == Token.LESS_EQUAL
				|| currentToken.kind == Token.GREATER_EQUAL || currentToken.kind == Token.DIFF || currentToken.kind == Token.EQUAL){
			acceptIt();
			parseExpressaoSimples();
		}
	}
        
        private void parseExpressaoSimples() throws Exception {
		// <expresso-simples> ::= <expresso-simples> <op-ad> <termo> | <termo>, <op-ad> ::= + | - | or
		parseTermo();
		while(currentToken.kind == Token.SUM || currentToken.kind == Token.SUB || currentToken.kind == Token.OR){
			acceptIt();
			parseTermo();
		}

	}
        
        private void parseFator() throws Exception {
		//<fator> ::= <varivel>	| <literal> | "(" <expresso> ")"
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
                        
                }
	}
        
        private void parseIterativo() throws Exception{
            accept(Token.WHILE);
            parseExpressao();
            accept(Token.DO);
            parseComando();
        }
        
        private void parseListaDeComandos() throws Exception {
		// <lista-de-comandos> ::=	<comando> ; | <lista-de-comandos> <comando> ; | <vazio>
		while(currentToken.kind==Token.ID || currentToken.kind==Token.IF || currentToken.kind==Token.WHILE || currentToken.kind==Token.BEGIN){
                    parseComando();
                    accept(Token.SEMICOLON);
                }
	}
        
        private void parseListaDeIds() throws Exception {
		// <lista-de-ids> ::= <id>	| <lista-de-ids> , <id>
                accept(Token.ID);
		while(currentToken.kind == Token.COMMA){
			acceptIt();
			accept(Token.ID);
		}
	}
        
        private void parseLiteral() throws Exception {
		//<literal> ::= <bool-lit> | <int-lit> | <float-lit>
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
                }
	}
        
	private void parsePrograma() throws Exception {
		// program <id> ; <corpo> .
                accept(Token.PROGRAM);
                accept(Token.ID);
                accept(Token.SEMICOLON);
                parseCorpo();
                accept(Token.DOT);
                accept(Token.EOF);
	}

	private void parseSeletor() throws Exception {
		// <seletor> ::= <seletor> "[" <expresso> "]" | "[" <expresso> "]" | <vazio>
		while(currentToken.kind == Token.LBRACKET) {
			acceptIt();
			parseExpressao();
			accept(Token.RBRACKET);
		}
	}
        
        private void parseTermo() throws Exception {
		// <termo> ::= <termo> <op-mul> <fator> | <fator> , <op-mul> ::= *	| / | and
		parseFator();
		while(currentToken.kind == Token.MULT || currentToken.kind == Token.DIV || currentToken.kind == Token.AND) {
			acceptIt();
			parseFator();
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
			// erro, esperava um tipo vlido
		}
	}
	
	private void parseVariavel() throws Exception {
		// <varivel> ::=	<id> <seletor>
		if(currentToken.kind == Token.ID){
			acceptIt();
			parseSeletor();
		}
	}

}
