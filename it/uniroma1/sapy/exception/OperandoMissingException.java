package it.uniroma1.sapy.exception;

/**
 * Eccezione lanciata se durante il parsing si riscontra un errore per un operando mancante
 */
public class OperandoMissingException extends Exception
{
	/**
	 * Stampa il messaggio di errore
	 */
	@Override
	public void printStackTrace()
	{
		System.out.println("Impossibile eseguire l'operazione, a causa di un operando mancante");
	}
	
	@Override
	public String getMessage()
	{
		return "Impossibile eseguire l'operazione";
	}
}
