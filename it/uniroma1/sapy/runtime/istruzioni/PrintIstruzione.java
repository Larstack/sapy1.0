package it.uniroma1.sapy.runtime.istruzioni;

import java.util.*;
import it.uniroma1.sapy.lexer.token.*;

/**
 * Istruzione PRINT
 */
public class PrintIstruzione implements Istruzione
{
	private ArrayList<Token> expr;
	/**
	 * Costruttore
	 * @param ArrayList<Token> - Espressione da stampare
	 */
	public PrintIstruzione(ArrayList<Token> expr)
	{
		this.expr = expr;
		analizzaEspressione();
	}
	
	/**
	 * Risolve l'espressione da stampare
	 */
	public void analizzaEspressione()
	{
		Iterator iteraExpr = expr.iterator();
		int espressioneNumerica = 0;
		while(iteraExpr.hasNext())
		{
			Token t = (Token)iteraExpr.next();
			if(t.ritornaTipoToken().equals(Tok.INTERO))
			{
				
			}
				
		}
	}
	
	/**
	 * Stampa a video l'espressione
	 */
	@Override
	public void esegui()
	{
		
	}
	
}
