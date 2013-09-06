package it.uniroma1.sapy.lexer.token;

/**
 * Definisce il token STRINGA
 */
public class Stringa extends ValToken
{

	/**
	 * Costruttore
	 * @param String - Valore del Token 
	 */
	public Stringa(String valoreToken)
	{
		super(Tok.STRINGA, valoreToken);
	}

}
