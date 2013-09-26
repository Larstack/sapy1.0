package it.uniroma1.sapy.runtime.istruzioni;

import java.util.*;
import it.uniroma1.sapy.lexer.token.*;
import it.uniroma1.sapy.runtime.VarRepository;

/**
 * Istruzione INPUT.<br />
 * Salva in una variabile la stringa presa in input da console.
 */
public class InputIstruzione implements Istruzione
{
	/**
	 * Etichetta che identifica l'istruzione, utilizzata dall'istruzione GOTO.
	 */
	private Intero etichetta;
	
	/**
	 * Nome della variabile in cui viene salvata la stringa presa in input.
	 */
	private String varStringa;
	
	/**
	 * Stringa presa in input da console.
	 */
	private String valoreVariabile;
	
	/**
	 * Costruttore
	 * @param varStringa - nome della variabile in cui salvare la stringa presa in input.
	 * @param etichetta - etichetta che identifica l'istruzione.
	 */
	public InputIstruzione(String varStringa, Intero etichetta)
	{
		this.varStringa = varStringa;
		this.etichetta = etichetta;
	}

	@Override
	public Intero getLabel()
	{
		if(etichetta==null) return null;
		return etichetta;
	}
	
	@Override
	public Object esegui()
	{
		Scanner s = new Scanner(System.in);
		String input = s.nextLine();
		valoreVariabile = input;
		VarRepository variabili = VarRepository.getInstance();
		variabili.setVariabile(varStringa, new Stringa(valoreVariabile));
		return null;
	}
	
	/**
	 * Ritorna la stringa presa in input.
	 * @return stringa presa in input da console.
	 */
	public String getStringaInput(){ return valoreVariabile; }
	
	/**
	 * Ritorna il nome della variabile in cui viene salvata la stringa.
	 * @return nome della variabile in cui viene salvata la stringa.
	 */
	public String getNomeVariabile(){ return varStringa; }
}