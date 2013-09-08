package it.uniroma1.sapy.exception;

import it.uniroma1.sapy.lexer.token.Tok;

/**
 * Eccezione lanciata in caso di errore di sintassi 
 */
public class SintassiException extends Exception
{
	private Tok t;
	/**
	 * Costruttore
	 * @param Tok - Il tipo di token previsto
	 */
	public SintassiException(Tok t)
	{
		this.t = t;
	}
	
	/**
	 * Stampa l'errore che si è verificato
	 */
	@Override
	public void printStackTrace()
	{
		System.out.println("Errore di sintassi - Previsto il token "+t);
	}
}
