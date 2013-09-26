package it.uniroma1.sapy.runtime;

import java.util.*;
import it.uniroma1.sapy.lexer.token.*;

/**
 * Memoria che mantiene il valore delle variabili durante l'esecuzione del programma. 
 * @author Leonardo Andres Ricciotti
 */
public class VarRepository
{
	/**
	 * Map che memorizza il nome della variabile e il valore ad essa associato.
	 */
	private HashMap<String,Token> variabili;
	
	/**
	 * Istanza unica della classe.
	 */
	static private VarRepository istanzaVar;
	
	/**
	 * Costruttore privato
	 */
	private VarRepository(){ variabili = new HashMap<String,Token>(); }
	
	/**
	 * Crea un'istanza unica della classe e ritorna quest'ultima.  
	 * @return istanza della classe.
	 */
	public static VarRepository getInstance()
	{
		if(istanzaVar == null) istanzaVar = new VarRepository();
		return istanzaVar;
	}
	
	/**
	 * Inserisce o modifica il valore di una variabile.
	 * @param nome - nome della variabile.
	 * @param valoreVar - valore associato alla variabile.
	 */
	public void setVariabile(String nome,Token valoreVar)
	{
		variabili.put(nome, valoreVar);
	}
	
	/**
	 * Ritorna il valore di una variabile sotto forma di Token.
	 * @param nome - nome della variabile.
	 * @return valore associato alla variabile data in input.
	 */
	public Token getVariabile(String nome)
	{
		return variabili.get(nome);
	}
}
