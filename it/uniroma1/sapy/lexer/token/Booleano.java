package it.uniroma1.sapy.lexer.token;

public class Booleano extends ValToken
{
	
	/**
	 * Costruttore	
	 * @param TRUE o FALSE
	 */
	public Booleano(boolean b)
	{
		super(Tok.BOOLEANO, b);
	}

}
