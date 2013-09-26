package it.uniroma1.sapy.exception;

import it.uniroma1.sapy.lexer.token.Tok;

/**
 * Eccezione lanciata in caso di errore di sintassi.
 */
public class SintassiException extends Exception
{
	/**
	 * Tok che definisce il tipo di Token previsto.
	 */
	private Tok tipo;
	/**
	 * Costruttore
	 * @param tipo - tipo di token previsto.
	 */
	public SintassiException(Tok tipo)
	{
		this.tipo = tipo;
	}
	
	/**
	 * Stampa l'errore che si è verificato.
	 */
	@Override
	public void printStackTrace()
	{
		System.out.println("Errore di sintassi - Previsto il token "+tipo);
	}
}
