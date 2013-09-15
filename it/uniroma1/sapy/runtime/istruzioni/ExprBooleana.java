package it.uniroma1.sapy.runtime.istruzioni;

import java.util.*;
import it.uniroma1.sapy.exception.*;
import it.uniroma1.sapy.lexer.token.*;

/**
 * Espressione booleana - Estende la classe Espressione
 */
public class ExprBooleana extends Espressione
{	
	
	/**
	 * Costruttore
	 * @param ArrayList<Token> - Espressione booleana da risolvere
	 */
	public ExprBooleana(ArrayList<Token> espressione){ super(espressione); }
	
	/**
	 * Ritorna il risultato dell'espressione booleana
	 * @return Booleano - Ritorna un token di tipo Booleano 
	 */
	@Override
	public Token getRisultato() throws Exception
	{	
		Token result = expr();
		if(point<text.size()) throw new ExtraTokenException();
		return result;
	}
	
	/**
	 * Punto di partenza nella risoluzione dell'espressione. Esegue la somma logica in presenza di un token di tipo Or
	 * @return Booleano - Ritorna un token di tipo booleano
	 * @throws Exception - Lancia un'eccezione se si verifica un errore durante la risoluzione dell'espressione
	 */
	public Token expr() throws Exception
	{
		Token x = andExpr();
		if(!(point>text.size()-1))
		{	while(peek().ritornaTipoToken().equals(Tok.OR))
			{
				consume();
				Token y = andExpr();
				boolean b1 = (boolean)x.ritornaValore();
				boolean b2 = (boolean)y.ritornaValore();
				x = new Booleano(b1 || b2);
				if(point>text.size()-1) break;
			}
		}
		return x;
	}
	
	/**
	 * Esegue il prodotto logico tra due token di tipo Booleano, in presenza di un token di tipo And
	 * @return Booleano - Ritorna un token di tipo booleano
	 * @throws Exception - Lancia un'eccezione se si verifica un errore durante la risoluzione dell'espressione
	 */
	public Token andExpr() throws Exception
	{
		Token x = notExpr();
		if(!(point>text.size()-1))
		{
			while(peek().ritornaTipoToken().equals(Tok.AND))
			{
				consume();
				Token y = notExpr();
				boolean b1 = (boolean)x.ritornaValore();
				boolean b2 = (boolean)y.ritornaValore();
				x = new Booleano(b1 && b2);
				if(point>text.size()-1) break;
			}	
		}
		return x;
	}
	
	/**
	 * Complementa il valore del token in presenza di un token di tipo Not 
	 * @return Booleano - Ritorna un token di tipo Booleano
	 */
	public Token notExpr() throws Exception
	{
		if(peek().ritornaTipoToken().equals(Tok.NOT))
		{
			consume();
			Token x = notExpr();
			if(x.ritornaValore().equals(true)) return new Booleano(false);
			return new Booleano(true);
		}		
		else return simpleExpr();
	}
	
	/**
	 * In presenza di token di tipo LeftPar inizia l'analisi del contenuto tra parentesi, altrimenti ritorna il Booleano se il token è di questo tipo.
	 * @return Booleano - Ritorna un token di tipo Booleano
	 * @throws ParentesiParsingException - Viene lanciata nel caso di errore nelle parentesi tonde.
	 * @throws OperazioneNonValidaException - Viene lanciata se gli operandi non sono compatibili.
	 */
	public Token simpleExpr() throws Exception
	{
		Token t = peek();
		if(t.ritornaTipoToken().equals(Tok.LEFT_PAR))
		{
			consume();
			Token result = expr();
			try
			{
			if(point>text.size()-1||!peek().ritornaTipoToken().equals(Tok.RIGHT_PAR))
				throw new ParentesiParsingException();
			}
			catch(ParentesiParsingException e)
			{
				e.printStackTrace();
				throw new ParentesiParsingException();
			}
			consume();
			return result;
		}
		else if(isTerm(t))
		{
			consume();
			return t;
		}
		else
		{
			try
			{
				throw new OperazioneNonValidaException();	
			}
			catch(OperazioneNonValidaException e)
			{
				e.printStackTrace();
				throw new OperazioneNonValidaException();
			}
		}
	}
	
	/**
	 * Controlla se il token è di tipo Booleano, quindi compatibile con un'operazione logica
	 * @param Token - Token da verificarne il tipo
	 * @return boolean - Ritorna true in caso di token di tipo Booleano, false altrimenti
	 */
	public boolean isTerm(Token t)
	{
		return t.ritornaTipoToken().equals(Tok.BOOLEANO);
	}
}
