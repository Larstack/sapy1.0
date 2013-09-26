package it.uniroma1.sapy.lexer.token;

/**
 * Token di tipo FUNZIONE.
 */
public class Funzione extends Token
{
	
	/**
	 * Costruttore
	 * @param valoreToken - stringa non riconosciuta come Token dal Lexer.
	 */
	public Funzione(String valoreToken)
	{
		super(Tok.FUNZIONE, valoreToken);
	}
}
