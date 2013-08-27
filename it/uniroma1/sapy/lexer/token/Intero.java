package it.uniroma1.sapy.lexer.token;

public class Intero extends ValToken
{
	
	/**
	 * Costruttore	
	 * @param Valore di tipo intero da assegnare al token
	 */
	public Intero(int n)
	{
		super(Tok.INTERO, n);
	}

}
