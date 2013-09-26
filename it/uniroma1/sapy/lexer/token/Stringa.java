package it.uniroma1.sapy.lexer.token;

/**
 * Token di tipo STRINGA.
 */
public class Stringa extends Token
{

	/**
	 * Costruttore
	 * @param valoreToken - stringa da assegnare come valore al Token.
	 */
	public Stringa(String valoreToken)
	{
		super(Tok.STRINGA, valoreToken);
	}

}
