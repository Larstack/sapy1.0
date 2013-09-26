package it.uniroma1.sapy.exception;

/**
 * Eccezione lanciata in caso di errore nelle espressioni booleane.
 */
public class BooleanParsingException extends Exception
{
	/**
	 * Stampa il messaggio di errore.
	 */
	@Override
	public void printStackTrace()
	{
		System.out.println("Si è verificato un errore durante il parsing di un'espressione booleana");
	}
}
