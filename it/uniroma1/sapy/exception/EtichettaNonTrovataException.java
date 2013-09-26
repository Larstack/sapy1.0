package it.uniroma1.sapy.exception;

/**
 * Eccezione lanciata se non viene trovata l'etichetta a cui fa riferimento l'istruzione GOTO.
 */
public class EtichettaNonTrovataException extends Exception
{
	/**
	 * int che identifica l'etichetta.
	 */
	private int etichetta;
	
	/**
	 * Costruttore
	 * @param etichetta - int che identifica l'etichetta. 
	 */
	public EtichettaNonTrovataException(int etichetta){ this.etichetta = etichetta; }
	
	/**
	 * Stampa il messaggio di errore.
	 */
	@Override
	public void printStackTrace()
	{
		System.out.println("L'etichetta "+etichetta+" non è stata trovata");
	}
	
	/**
	 * Restituisce il messaggio di errore sotto forma di String.
	 */
	@Override
	public String getMessage()
	{
		return "Etichetta "+etichetta+" non trovata";
	}
}
