package it.uniroma1.sapy.exception;

/**
 * Eccezione lanciata se durante il parsing si riscontra in un'espressione un errore di parentesi.
 */
public class ParentesiParsingException extends Exception
{
	/**
	 * Stampa il messaggio di errore.
	 */
	@Override
	public void printStackTrace()
	{
		System.out.println("Errore di parentesi - Mismatched parenthesis");
	}
	
	/**
	 * Restituisce il messaggio di errore sotto forma di String.
	 */
	@Override
	public String getMessage()
	{
		return "Parentesi non valide";
	}
}
