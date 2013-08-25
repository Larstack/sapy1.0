package it.uniroma1.sapy.lexer.token;

/**
 * Classe astratta che estende la classe Token;<br />
 * Fornisce un metodo che restituisce il valore del token 
 */
abstract public class ValToken extends Token
{
	protected Object valoreToken;
	
/**
 * Ritorna il valore del token
 * @return Object - Valore del token
 */
	public Object ritornaValore()
	{
		return valoreToken;
	}
}
