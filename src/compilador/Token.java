package compilador;
class Token {
    
    public byte kind;
    public String value;
    public int line;
    public int col;
    
    public Token(byte kind, String value, int line, int col){
        this.kind = kind;
        this.value = value;
        this.line = line;
        this.col = col;
        
        if(kind == ID){
            for(byte k = BEGIN;k <= BOOLEAN; k++){
                if(value.equals(spellings[k])){
                    this.kind = k;
                    break;
                }
            }
        }
        
    }
    
    public void imprimir(){
        System.out.println(value+" "+kind+" "+line+" "+col+" ");
    }
    
    
    public final static byte ID = 0, INT_LIT = 1, SOMA = 2, SUB = 3, MULT = 4,
            DIV = 5, MAIORQ = 6, MAIOR_IGUAL = 7, MENOR_IGUAL = 8, DIF = 9,
            MENORQ = 10, LBRACKET = 11, RBRACKET = 12, PONTO_VG = 13,
            ATRIBUICAO = 14, DOIS_PONTOS = 15, LPAREN = 16, RPAREN = 17, 
            PONTO_PONTO = 18, PONTO = 19, VIRG = 20, BEGIN  = 21, END = 22, 
            IF = 23, THEN = 24, ELSE = 25, VAR = 26, WHILE = 27, DO = 28,
            OR = 29, AND = 30, PROGRAM = 31, ARRAY = 32, OF = 33, INTEGER = 34,
            REAL = 35,TRUE= 36, FALSE = 37, BOOLEAN = 36, EOF = 39, ERRO = 40;
    
    private final static String[] spellings = {
        "<id>", "<int_lit>", "+", "-", "*", "/", ">", ">=", "<=", "<>", ">",
        "[", "]", ";", ":=", ":", "(", ")", "..",".", ",", "begin", "end", "if",
        "then", "else", "var","while", "do", "or", "and", "program", "array",
        "of", "integer", "real","true","false", "boolean", "<EOF>"
    };
}
