package it.uniroma1.sapy.lexer.token;

public class Intero extends Token
{
	
	/**
	 * Costruttore	
	 * @param int - Valore da assegnare al token
	 */
	public Intero(int n)
	{
		super(Tok.INTERO, n);
	}

}
