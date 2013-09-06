package it.uniroma1.sapy.lexer.token;

public class Booleano extends Token
{
	
	/**
	 * Costruttore	
	 * @param boolean - Valore della variabile
	 */
	public Booleano(boolean b)
	{
		super(Tok.BOOLEANO, b);
	}

}
