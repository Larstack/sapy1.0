package it.uniroma1.sapy.exception;

/**
 * Eccezione lanciata in caso di errore durante il parsing
 */
public class ParsingException extends Exception
{
	/**
	 * Stampa l'errore che si è verificato
	 */
	@Override
	public void printStackTrace()
	{
		System.out.println("Si è verificato un errore durante il parsing");
	}
}
