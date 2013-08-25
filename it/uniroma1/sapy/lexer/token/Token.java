package it.uniroma1.sapy.lexer.token;

/**
 * Classe astratta che definisce un Token di tipo generico
 */
abstract public class Token
{
	protected Tok tipoToken;
	
	/**
	 * @return Tok - Ritorna il tipo di token
	 */
	public Tok ritornaTipoToken()
	{
		return tipoToken;
	}
}
