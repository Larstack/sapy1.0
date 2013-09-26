package it.uniroma1.sapy.lexer.token;

/**
 * Token di tipo INTERO.
 */
public class Intero extends Token
{
	
	/**
	 * Costruttore	
	 * @param n - valore da assegnare all'INTERO.
	 */
	public Intero(int n)
	{
		super(Tok.INTERO, n);
	}

}
