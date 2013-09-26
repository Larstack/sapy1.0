package it.uniroma1.sapy.lexer.token;

/**
 * Token di tipo Booleano (TRUE o FALSE).
 */
public class Booleano extends Token
{
	
	/**
	 * Costruttore	
	 * @param b - valore della variabile (true o false).
	 */
	public Booleano(boolean b)
	{
		super(Tok.BOOLEANO, b);
	}

}
