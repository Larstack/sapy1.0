package it.uniroma1.sapy.runtime.istruzioni;

import it.uniroma1.sapy.lexer.token.Intero;

/**
 * Istruzione GOTO.<br />
 * Effettua un salto all'istruzione con etichetta esplicitata dall'istruzione GOTO, poi il programma prosegue l'esecuzione da lì. 
 */
public class GotoIstruzione implements Istruzione
{
	/**
	 * Etichetta dell'istruzione su cui effetture il salto.
	 */
	private Intero label;
	
	/**
	 * Etichetta che identifica l'istruzione, utilizzata dall'istruzione GOTO.
	 */
	private Intero etichetta;
	
	/**
	 * Costruttore
	 * @param label - etichetta dell'istruzione su cui effettuare il salto.
	 * @param etichetta - Etichetta che identifica questa istruzione GOTO.
	 */
	public GotoIstruzione(Intero label,Intero etichetta)
	{
		this.label = label;
		this.etichetta = etichetta;
	}
	
	@Override
	public Intero getLabel()
	{
		if(etichetta==null) return null;
		return etichetta;
	}
	
	public Intero getEtichetta(){ return label; }

	@Override
	public Integer esegui(){ return (int)label.ritornaValore();}

}
