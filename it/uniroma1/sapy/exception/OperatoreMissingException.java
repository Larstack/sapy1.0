package it.uniroma1.sapy.exception;

/**
 * Eccezione lanciata se durante il parsing si riscontra un errore per un operatore mancante
 */
public class OperatoreMissingException extends Exception
{
	/**
	 * Stampa il messaggio di errore
	 */
	@Override
	public void printStackTrace()
	{
		System.out.println("Impossibile eseguire l'operazione, a causa di un operatore mancante");
	}
	
	@Override
	public String getMessage()
	{
		return "Impossibile eseguire l'operazione";
	}

}
