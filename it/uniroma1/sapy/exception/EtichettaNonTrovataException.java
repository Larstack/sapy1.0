package it.uniroma1.sapy.exception;

public class EtichettaNonTrovataException extends Exception
{
	private int etichetta;
	
	/**
	 * Costruttore
	 * @param int - Intero che identifica l'etichetta 
	 */
	public EtichettaNonTrovataException(int etichetta){ this.etichetta = etichetta; }
	
	/**
	 * Stampa il messaggio di errore
	 */
	@Override
	public void printStackTrace()
	{
		System.out.println("L'etichetta "+etichetta+" non è stata trovata");
	}
		
	@Override
	public String getMessage()
	{
		return "Etichetta "+etichetta+" non trovata";
	}
}
