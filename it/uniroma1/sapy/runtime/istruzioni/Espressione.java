package it.uniroma1.sapy.runtime.istruzioni;

import it.uniroma1.sapy.lexer.token.*;
import java.util.*;
/**
 * Classe astratta per le espressioni booleane e aritmetiche
 */
abstract public class Espressione
{
	protected int point;
	protected ArrayList<Token> text;
	
	/**
	 * Costruttore
	 * @param ArrayList<Token> - Espressione booleana o aritmetica, formata da Token
	 */
	public Espressione(ArrayList<Token> t)
	{
		point = 0;
		text = t;
	}
	
	/**
	 * Ritorna il risultato dell'espressione sotto forma di Token 
	 * @return Token - Risultato dell'espressione<br />Booleano se l'espressione è booleana.<br />Intero se l'espressione è aritmetica.
	 * @throws Exception - Viene lanciata un'eccezione in caso di espressione non valida
	 */
	abstract public Token getRisultato() throws Exception;
}

