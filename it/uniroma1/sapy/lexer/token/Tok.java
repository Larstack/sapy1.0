package it.uniroma1.sapy.lexer.token;

/**
 * Tipi di Token validi.
 */
public enum Tok
{
	INTERO("Intero"), STRINGA("Stringa"), BOOLEANO("Booleano"), UGUALE("Uguale"), MINORE("Minore"), MAGGIORE("Maggiore"), DIVERSO("Diverso"), MINOREUGUALE("MinoreUguale"),
	MAGGIOREUGUALE("MaggioreUguale"), PIU("Piu"), MENO("Meno"), PER("Per"), DIVISO("Diviso"), MODULO("Modulo"), LEFT_PAR("LeftPar"), RIGHT_PAR("RightPar"),
	DUEPUNTI("DuePunti"), OR("Or"), AND("And"), NOT("Not"), REM("Rem"), IF("If"), FOR("For"), DO("Do"), THEN("Then"), ENDIF("EndIf"), END("End"), TO("To"),
	NEXT("Next"), VARIABILE("Variabile"), FUNZIONE("Funzione"), GOTO("Goto"), PRINT("Print"), INPUT("Input"), EOL("Eol");
	
	/**
	 * Nome della classe di riferimento del Token.
	 */
	private String token;
	
	/**
	 * Costruttore
	 * @param token - nome della classe di riferimento del Token.
	 */
	Tok(String token)
	{
		this.token = token;
	}
	
	/**
	 * Ritorna il nome della classe di riferimento del Token.
	 * @return nome della classe di riferimento del Token.
	 */
	public String getValoreToken()
	{
		return token;
	}
}

