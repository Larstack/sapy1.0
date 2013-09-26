package it.uniroma1.sapy.exception;

/**
 * Eccezione lanciata se più istruzioni hanno la stessa etichetta. Ciò implicherebbe un conflitto durante l'esecuzione di un'istruzione GOTO.
 */
public class EtichettaDuplicataException extends Exception
{
	/**
	 * int che identifica l'etichetta.
	 */
	private int etichetta;
	
	/**
	 * Costruttore
	 * @param etichetta - int che identifica l'etichetta.
	 */
	public EtichettaDuplicataException(int etichetta){ this.etichetta = etichetta; }
	
	/**
	 * Stampa il messaggio di errore.
	 */
	@Override
	public void printStackTrace()
	{
		System.out.println("Esiste già un'etichetta "+etichetta);
	}
	
	/**
	 * Restituisce il messaggio di errore sotto forma di String.
	 */
	@Override
	public String getMessage()
	{
		return "Etichetta "+etichetta+" già esistente";
	}
}
