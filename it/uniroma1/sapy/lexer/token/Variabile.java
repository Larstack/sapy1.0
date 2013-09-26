package it.uniroma1.sapy.lexer.token;

/**
 * Token di tipo VARIABILE ($nome_var).
 */
public class Variabile extends Token
{
	
	/**
	 * Costruttore
	 * @param valoreToken - nome dato alla VARIABILE.
	 */
	public Variabile(String valoreToken)
	{
		super(Tok.VARIABILE, valoreToken);
	}
	
}
