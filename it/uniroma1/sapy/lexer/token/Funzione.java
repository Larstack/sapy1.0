package it.uniroma1.sapy.lexer.token;

/**
 * Definisce il token FUNZIONE
 */
public class Funzione extends Token
{
	
	/**
	 * Costruttore
	 * @param String - Valore del Token 
	 */
	public Funzione(String valoreToken)
	{
		super(Tok.FUNZIONE, valoreToken);
	}
}
