package compiler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class Scanner {
    final private BufferedReader file;
    private char currentChar;
    private int line;
    private int col,aux;
    private byte currentKind;
    private StringBuffer currentValue;


    public Scanner(String fileName){
    	this.file = sourceFile(fileName);
    }

    private BufferedReader sourceFile(String fileName) {
    	BufferedReader file;
		try {
			file = new BufferedReader(new FileReader(fileName));
		    currentChar = (char) file.read();
		    col=0;
		    line=1;
		    return file;
		} catch (FileNotFoundException e) {
			System.out.println("ERRO: Programa fonte não encontrado\n[" + e + "].");
			System.exit(1);
		} catch (IOException e) {
			System.out.println("ERRO: Programa fonte não encontrado\n[" + e + "].");
			System.exit(1);
		}
		return null;
    }
    private void takeIt() throws Exception{
        currentValue.append(currentChar);
        if( file != null) {
        	currentChar = (char) file.read();
            col++;
        }
    }

    private boolean isDigit(char c){
        return c >= 48 && c <= 57;
    }

    private boolean isLetter(char c){
       return c >= 97 && c <= 122;
    }

    private boolean isGraphic(char c){
        boolean x = c==9 || (c >= 32 && c<= 126);
        boolean y = false;
        if(c == '�' || c == '~'){
                y = true;
        }
        return x || y;
    }

    private byte scanToken() throws Exception{
        if(isLetter(currentChar)){
            takeIt();
            aux = col;
            while(isLetter(currentChar) || isDigit(currentChar)){ //<letra>(<letra> | <digito>)*
                takeIt();
            }
            return Token.ID;
        }
        if(isDigit(currentChar)){ //<digito><digito>*
            takeIt();
            aux = col;
            while(isDigit(currentChar)){
                takeIt();
            }
            if(currentChar == '.'){
                takeIt();
                if(isDigit(currentChar)){
                    takeIt();
                    while(isDigit(currentChar)){
                        takeIt();
                    }
                    return Token.FLOAT_LIT;
                } else{
                    return Token.FLOAT_LIT;
                }
            }
            return Token.INT_LIT;
        }
        if(currentChar == '+'){
            takeIt();
            aux = col;
            return Token.SUM;
        }
        if(currentChar == '-'){
            takeIt();
            aux = col;
            return Token.SUB;
        }
        if(currentChar == '*'){
            takeIt();
            aux = col;
            return Token.MULT;
        }
        if(currentChar == '/'){
            takeIt();
            aux = col;
            return Token.DIV;
        }
        if(currentChar == '>'){
            takeIt();
            aux = col;
            if(currentChar == '='){
                takeIt();
                return Token.GREATER_EQUAL;
            } else{
                return Token.GREATER;
            }
        }
        if(currentChar == '<'){ // leitura de < <= <>
            takeIt();
            aux = col;
            if(currentChar == '='){
                takeIt();
                return Token.LESS_EQUAL;
            } else if(currentChar == '>'){
                    takeIt();
                    return Token.DIFF;
            } else {
                return Token.LESS;

            }
        }
        if(currentChar == '['){
            takeIt();
            aux = col;
            return Token.LBRACKET; //pode refatorar
        }
        if(currentChar == ']'){
            takeIt();
            aux = col;
            return Token.RBRACKET;
        }
        if(currentChar == ';'){
            takeIt();
            aux = col;
            return Token.SEMICOLON;
        }
        if(currentChar == ':'){
            takeIt();
            aux = col;
            if(currentChar == '='){
                takeIt();
                return Token.BECOMES;
            } else{
                return Token.COLON;
            }
        }
        if(currentChar == '('){
            takeIt();
            aux = col;
            return Token.LPAREN;
        }
        if(currentChar == ')'){
            takeIt();
            aux = col;
            return Token.RPAREN;
        }
        if(currentChar == '.'){
            takeIt();
            aux = col;
            if(currentChar == '.'){
                takeIt();
                return Token.DOUBLE_DOT;
            } else {
                if(isDigit(currentChar)){
                    takeIt();
                    while(isDigit(currentChar)){
                        takeIt();
                    }
                    return Token.FLOAT_LIT;
                } else{
                    return Token.DOT;
                }

            }
        }
        if(currentChar == ','){
            takeIt();
            aux = col;
            return Token.COMMA;
        }
        if(currentChar == 65535){ //SYMBOL DO EOF REAL
            takeIt();
            aux = col;
            return Token.EOF;
        }
        if(currentChar == '='){
            takeIt();
            aux = col;
            return Token.EQUAL;
        }
        takeIt();
        return Token.ERROR;
    }

    private void scanSeparator() throws Exception{ //Revisar simbolos
        switch(currentChar){
            case '#':{ //marcação de linha de comentário assim como esse //
                takeIt();
                aux = col;
                while(isGraphic(currentChar)){
                    takeIt();
                }
                if (currentChar == '\n')
                    takeIt();
                col=-1;
            }
            break;
            case '\n':
                line++;
                col=-1;
            case ' ':
            case 13:
            case 9:
                takeIt();
            break;
        }
    }

    public Token scan() throws Exception{
        currentValue = new StringBuffer("");
        while(currentChar == '#' || currentChar == ' ' || currentChar == '\n' || currentChar == 13 || currentChar == '\t' || currentChar == 10){
            scanSeparator();
        }
        currentValue.delete(0,currentValue.length());
        currentKind = scanToken();
        return new Token(currentKind, currentValue.toString(), line, aux); //TO DO : add linha e coluna
    }

    public ArrayList<Token> ler() throws Exception{
        ArrayList<Token> lista = new ArrayList<>();
        Token tk;
        do{
            tk = scan();
            lista.add(tk);
        }while(tk.kind != Token.EOF);
        return lista;
    }
}
