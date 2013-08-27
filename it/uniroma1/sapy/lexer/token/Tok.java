package it.uniroma1.sapy.lexer.token;

/**
 * Tipi di Token
 */
public enum Tok
{
	INTERO("Intero"), STRINGA("Stringa"), BOOLEANO("Booleano"), UGUALE("Uguale"), MINORE("Minore"), MAGGIORE("Maggiore"), DIVERSO("Diverso"), MINOREUGUALE("MinoreUguale"),
	MAGGIOREUGUALE("MaggioreUguale"), PIU("Piu"), MENO("Meno"), PER("Per"), DIVISO("Diviso"), MODULO("Modulo"), LEFT_PAR("LeftPar"), RIGHT_PAR("RightPar"),
	DUEPUNTI("DuePunti"), OR("Or"), AND("And"), NOT("Not"), REM("Rem"), IF("If"), FOR("For"), DO("Do"), THEN("Then"), ELSE("Else"), ENDIF("Endif"), END("End"), TO("To"),
	NEXT("Next"), VARIABILE("Variabile"), FUNZIONE("Funzione"), GOTO("Goto"), PRINT("Print"), INPUT("Input"), EOL("Eol");
	
	/**
	 * Costruttore
	 */
	private String s;
	Tok(String s)
	{
		this.s = s;
	}
	
	/**
	 * Ritorna il valore associato al token
	 * @return String - Valore associato al token
	 */
	public String getValoreToken()
	{
		return s;
	}
}

