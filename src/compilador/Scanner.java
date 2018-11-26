package compilador;

import java.io.BufferedReader;


public class Scanner {
    private BufferedReader arquivo;
    private char currentChar; 
    private int linha = 0;
    private int col;
    private byte currentKind;
    private StringBuffer currentValue;

       
    Scanner(BufferedReader arquivo)throws Exception{
        this.arquivo = arquivo;
        currentChar = (char)arquivo.read(); //TO DO : pegar primeiro caractere do txt
        System.out.println(currentChar);
        System.out.println("valor corrente = "+currentValue);
        col=0;
    }
    
    private void take(char expectedChar) throws Exception{
        if(currentChar == expectedChar){
            currentValue.append(currentChar);
            currentChar = (char)arquivo.read(); //currentChar = proximo caractere;
            System.out.println(currentChar);
        } else {
            //retorna token erro
        }
    }
    
    private void takeIt() throws Exception{
        currentValue.append(currentChar);
        currentChar = (char)arquivo.read(); //currentChar = proximo caractere;
        System.out.println(currentChar);
        col++;
    }
    
    private boolean isDigit(char c){
        return c >= 48 && c <= 57;
    }
    
    private boolean isLetter(char c){
       return c >= 97 && c <= 122;
    }
    
    private boolean isGraphic(char c){
        return c >= 32 && c<= 126;
    }
    
    private byte scanToken() throws Exception{
        if(isLetter(currentChar)){
            takeIt();
            while(isLetter(currentChar) || isDigit(currentChar)){ //<letra>(<letra> | <digito>)* 
                takeIt();
            }
            return Token.ID;
        }
        if(isDigit(currentChar)){ //<digito><digito>*
            takeIt();
            while(isDigit(currentChar)){ 
                takeIt();
            }
            return Token.INT_LIT;
        }
        if(currentChar == '+'){
            takeIt();
            return Token.SOMA;
        }
        if(currentChar == '-'){
            takeIt();
            return Token.SUB;
        }
        if(currentChar == '*'){
            takeIt();
            return Token.MULT;
        }
        if(currentChar == '/'){
            takeIt();
            return Token.DIV;
        }
        if(currentChar == '>'){
            takeIt();
            if(currentChar == '='){
                takeIt();
                return Token.MAIOR_IGUAL;
            } else{
                return Token.MAIORQ;
            }
        }
        if(currentChar == '<'){ // leitura de < <= <>
            takeIt();
            if(currentChar == '='){
                takeIt();
                return Token.MENOR_IGUAL;
            } else if(currentChar == '>'){
                    takeIt();
                    return Token.DIF;
            } else {
                return Token.MENORQ;
             
            }        
        }
        if(currentChar == '['){
            takeIt();
            return Token.LBRACKET; //pode refatorar
        }
        if(currentChar == ']'){
            takeIt();
            return Token.RBRACKET;
        }
        if(currentChar == ';'){
            takeIt();
            return Token.PONTO_VG;
        }
        if(currentChar == ':'){
            takeIt();
            if(currentChar == '='){
                takeIt();
                return Token.ATRIBUICAO;
            } else{
                return Token.DOIS_PONTOS;
            }
        }
        if(currentChar == '('){
            takeIt();
            return Token.LPAREN;
        }
        if(currentChar == ')'){
            takeIt();
            return Token.RPAREN;
        }
        if(currentChar == '.'){
            takeIt();
            if(currentChar == '.'){
                takeIt();
                return Token.PONTO_PONTO;
            } else {
                return Token.PONTO;
            }
        }
        if(currentChar == ','){
            takeIt();
            return Token.VIRG;
        }
        if(currentChar == '_'){ //SYMBOL DO EOF REAL
            takeIt();
            return Token.EOF;
        }
        //TO DO : LER O EOF e o float-lit
        
        return Token.ERRO;
       
        
        //TO DO : return erro
    }
    
    private void scanSeparator() throws Exception{ //Revisar simbolos
        switch(currentChar){
            case '!':{ //marcação de linha de comentário assim como esse //
                takeIt();
                while(isGraphic(currentChar)){
                    takeIt();
                }
                take('\n');
            }
            break;
            case ' ':
            case '\n':
            case 13:
            case 9:
                takeIt();
            break;
        }
    }
    
    public Token scan() throws Exception{
        while(currentChar == '!' || currentChar == ' ' || currentChar == '\n' || currentChar == 13 || currentChar == '\t'){
            scanSeparator();
        }
        currentValue = new StringBuffer("");
        currentKind = scanToken();
        return new Token(currentKind, currentValue.toString(), linha, col); //TO DO : add linha e coluna
    }
}
