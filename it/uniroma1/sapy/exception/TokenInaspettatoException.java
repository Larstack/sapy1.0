package it.uniroma1.sapy.exception;

/**
 * Eccezione lanciata se il Token in esame non è quello atteso.
 */
public class TokenInaspettatoException extends Exception
{
	/**
	 * Stampa il messaggio di errore.
	 */
	@Override
	public void printStackTrace()
	{
		System.out.println("Token inaspettato");
	}	
	
	/**
	 * Restituisce il messaggio di errore sotto forma di String.
	 */
	@Override
	public String getMessage()
	{
		return "Token inaspettato";
	}
}