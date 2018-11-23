package compilador;
public class Scanner {
    private char currentChar; //TO DO : pegar primeiro caractere do txt
    
    private int linha, col;
    private byte currentKind;
    private StringBuffer currentValue;
    
    private void take(char expectedChar){
        if(currentChar == expectedChar){
            currentValue.append(currentChar);
            //currentChar = proximo caractere;
        } else {
            //retorna token erro
        }
    }
    
    private void takeIt(){
        currentValue.append(currentChar);
        //currentChar = proximo caractere;
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
    
    private byte scanToken(){
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
        //TO DO : LER O EOF e o float-lit
            
       
        
        //TO DO : return erro
    }
    
    private void scanSeparator(){ //Revisar simbolos
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
                takeIt();
            break;
        }
    }
    
    public Token scan(){
        while(currentChar == '!' || currentChar == ' ' || currentChar == '\n'){
            scanSeparator();
        }
        currentValue = new StringBuffer("");
        currentKind = scanToken();
        return new Token(currentKind, currentValue.toString(), linha, col); //TO DO : add linha e coluna
    }
}
