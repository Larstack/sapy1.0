package it.uniroma1.sapy.runtime.espressioni;

import it.uniroma1.sapy.lexer.token.*;
import java.util.*;

/**
 * Classe astratta che definisce le espressioni.
 */
abstract public class Espressione
{
	/**
	 * Indice che indica la posizione del Token che si sta analizzando. 
	 */
	protected int point;
	
	/**
	 * Espressione da analizzare.
	 */
	protected ArrayList<Token> text;
	
	/**
	 * Costruttore
	 * @param espressioneDaAnalizzare - espressione da analizzare.
	 */
	public Espressione(ArrayList<Token> espressioneDaAnalizzare)
	{
		point = 0;
		text = espressioneDaAnalizzare;
	}
	
	/**
	 * Ritorna il risultato dell'espressione sotto forma di Token.
	 * @return risultato dell'espressione.
	 * @throws Exception - Viene lanciata un'eccezione in caso di errore nell'espressione.
	 */
	abstract public Token getRisultato() throws Exception;
	
	/**
	 * Incrementa l'indice dell'espressione, puntando al Token successivo.
	 */
	public void consume(){ point+=1; }
	

	/**
	 * Ritorna il token nella posizione indicata dall'indice(point).
	 * @return Token nella posizione indicata dal point.
	 */
	public Token peek(){ return text.get(point); }
}

