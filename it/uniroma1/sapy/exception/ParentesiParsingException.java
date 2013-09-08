package it.uniroma1.sapy.exception;

/**
 * Eccezione lanciata se durante il parsing si riscontra un errore di parentesi in un'espressione
 */
public class ParentesiParsingException extends Exception
{
	/**
	 * Stampa il messaggio di errore
	 */
	@Override
	public void printStackTrace()
	{
		System.out.println("Errore di parentesi - Mismatched parenthesis");
	}
	
	@Override
	public String getMessage()
	{
		return "Parentesi non valide";
	}
}
