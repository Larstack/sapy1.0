package it.uniroma1.sapy.lexer.token;

/**
 * Classe astratta che definisce un Token di tipo generico
 */
abstract public class Token
{
	protected Tok tipoToken;
	protected Object valoreToken;
	/**
	 * Costruttore
	 * @param Tok - Tipo del token
	 */
	public Token(Tok t)
	{
		tipoToken = t;
	}
	/**
	 * Costruttore
	 * @param Tok - Tipo del token
	 * @param Object - Valore da assegnare al token
	 */
	public Token(Tok t, Object valoreToken)
	{
		this(t);
		this.valoreToken = valoreToken;
	}
	
	/**
	 * @return Tok - Ritorna il tipo di token
	 */
	public Tok ritornaTipoToken()
	{
		return tipoToken;
	}
	
	/**
	 * Ritorna il valore del token
	 * @return Object - Valore del token
	 */
	public Object ritornaValore()
	{
		return valoreToken;
	}
}
