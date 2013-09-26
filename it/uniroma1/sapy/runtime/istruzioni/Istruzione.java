package it.uniroma1.sapy.runtime.istruzioni;

import it.uniroma1.sapy.lexer.token.*;

/**
 * Interfaccia che definisce un'istruzione del programma. 
 * @author Leonardo Andres Ricciotti
 */
public interface Istruzione
{
	/**
	 * Ritorna l'etichetta dell'istruzione, utilizzata dall'istruzione GOTO.
	 * @return etichetta dell'istruzione.
	 */
	public Intero getLabel();
	
	/**
	 * Esegue l'istruzione.
	 * @return un valore int che fa riferimento al valore di un'etichetta, se durante l'esecuzione dell'istruzione viene rilevata un'istruzione GOTO; null altrimenti.
	 * @throws Exception - Se si verifica un errore durante l'esecuzione dell'istruzione.
	 */
	public Object esegui() throws Exception;
}
