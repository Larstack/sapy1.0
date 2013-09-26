package it.uniroma1.sapy.exception;

/**
 * Eccezione lanciata se durante il parsing di un'espressione si riscontra un problema di token in più.
 */
public class ExtraTokenException extends Exception
{
	/**
	 * Stampa il messaggio di errore.
	 */
	@Override
	public void printStackTrace()
	{
		System.out.println("Espressione non valida");
	}
}
