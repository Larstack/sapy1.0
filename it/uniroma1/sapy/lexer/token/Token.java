package it.uniroma1.sapy.lexer.token;

/**
 * Classe astratta che definisce un Token di tipo generico
 */
abstract public class Token
{
	protected Tok tipoToken;
	
	public Token(Tok t)
	{
		tipoToken = t;
	}
	/**
	 * @return Tok - Ritorna il tipo di token
	 */
	public Tok ritornaTipoToken()
	{
		return tipoToken;
	}
}
