package it.uniroma1.sapy.runtime.istruzioni;

import it.uniroma1.sapy.lexer.token.*;

/**
 * Istruzione END.<br />
 * Termina l'esecuzione del programma.
 */
public class EndIstruzione implements Istruzione
{
	/**
	 * Etichetta che identifica l'istruzione, utilizzata dall'istruzione GOTO.
	 */
	private Intero etichetta;
	
	/**
	 * Costruttore
	 * @param etichetta - etichetta dell'istruzione.
	 */
	public EndIstruzione(Intero etichetta){ this.etichetta = etichetta; }
	
	@Override
	public Intero getLabel()
	{
		if(etichetta==null) return null;
		return etichetta;
	}
	
	@Override
	public Object esegui()
	{
		System.exit(0);
		return null;
	}	
}
