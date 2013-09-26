package it.uniroma1.sapy.lexer.token;

/**
 * Classe astratta che definisce i Token.
 */
abstract public class Token
{
	/**
	 * Tipo di Token.
	 */
	protected Tok tipoToken;
	
	/**
	 * Valore associato al Token.
	 */
	protected Object valoreToken;
	
	/**
	 * Costruttore
	 * @param t - tipo di Token.
	 */
	public Token(Tok t)
	{
		tipoToken = t;
	}
	
	/**
	 * Costruttore
	 * @param t - tipo di Token.
	 * @param valoreToken - valore assegnato al token.
	 */
	public Token(Tok t, Object valoreToken)
	{
		this(t);
		this.valoreToken = valoreToken;
	}
	
	/**
	 * Ritorna il tipo del Token.
	 * @return tipo di token.
	 */
	public Tok ritornaTipoToken()
	{
		return tipoToken;
	}
	
	/**
	 * Ritorna il valore associato al token.
	 * @return valore associato al token.
	 */
	public Object ritornaValore()
	{
		return valoreToken;
	}
}
