package compilador;
public class Scanner {
    private char currentChar; //TO DO : pegar primeiro caractere do txt
    
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
        return 0;
        //
    }
    
    private void scanSeparator(){
        
    }
    public Token scan(){
        while(currentChar == '4'){
            this.scanSeparator();
        }
        currentValue = new StringBuffer("");
        currentKind = this.scanToken();
        return new Token(currentKind, currentValue.toString()); //TO DO : add linha e coluna
    }
}
