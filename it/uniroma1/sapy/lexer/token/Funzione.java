package it.uniroma1.sapy.lexer.token;

/**
 * Definisce il token FUNZIONE
 */
public class Funzione extends ValToken
{
	
	/**
	 * Costruttore
	 * @param valoreToken - Valore di tipo stringa del Token 
	 */
	public Funzione(String valoreToken)
	{
		super(Tok.FUNZIONE, valoreToken);
	}
}
