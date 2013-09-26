package it.uniroma1.sapy.exception;

/**
 * Eccezione lanciata se l'espressione analizzata contiene operandi non compatibili.
 */
public class OperazioneNonValidaException extends Exception
{
	/**
	 * Stampa il messaggio di errore.
	 */
	@Override
	public void printStackTrace()
	{
		System.out.println("Espressione non valida, in quanto gli operandi non sono compatibili per l'operazione richiesta"); 
	}
	
	/**
	 * Restituisce il messaggio di errore sotto forma di String.
	 */
	@Override
	public String getMessage()
	{
		return "Operandi non compatibili";
	}
}
