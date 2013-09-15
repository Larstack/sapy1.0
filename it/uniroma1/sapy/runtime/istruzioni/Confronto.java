package it.uniroma1.sapy.runtime.istruzioni;

import java.util.*;

import it.uniroma1.sapy.exception.*;
import it.uniroma1.sapy.lexer.token.*;

public class Confronto extends Espressione
{
	private ArrayList<Token> lineaCodice = new ArrayList<Token>();
	private Token t;
	
	public Confronto(ArrayList<Token> text)
	{
		super(text);
	}
	
	/**
	 * Ritorna il risultato del confronto
	 * @return Booleano - Ritorna un token di tipo Booleano 
	 */
	@Override
	public Token getRisultato() throws Exception
	{	
		return analizzaExpr(); //////////////// Classe finita, ma provarla!!
	}
	
	public Booleano analizzaExpr() throws Exception
	{
		Token op = null;
		Token primoTermine = null;
		Token secondoTermine = null;		
		ArrayList<Token> termineDiConfronto = new ArrayList<Token>();
		Tok[] operatori = new Tok[] {Tok.DIVERSO,Tok.MAGGIORE,Tok.MAGGIOREUGUALE,Tok.MINORE,Tok.MINOREUGUALE,Tok.UGUALE};
		while(point<text.size())
		{
			boolean ctrl = false;
			for(Tok t : operatori)
				if(peek().ritornaTipoToken().equals(t))
				{
					ctrl = true;
					break;
				}
			if(!ctrl)
			{
				termineDiConfronto.add(peek());
				consume();
			}
			else if(op == null)
			{
				op = peek();
				consume();
				if(termineDiConfronto.size()==1)
				{
					primoTermine = termineDiConfronto.get(0);
					termineDiConfronto.clear();
					continue;
				}
				else
				{
					if(isExprMatematica(termineDiConfronto))
					{
						ExprMatematica espr = new ExprMatematica(termineDiConfronto);
						primoTermine = espr.getRisultato();
						termineDiConfronto.clear();
					}
					else throw new OperazioneNonValidaException();
					
				}
					
			}
			else throw new ExtraTokenException();
		}
		if(termineDiConfronto.size()==1)
		{
			secondoTermine = termineDiConfronto.get(0);
			termineDiConfronto.clear();
		}
		else
		{
			if(isExprMatematica(termineDiConfronto))
			{
				ExprMatematica espr = new ExprMatematica(termineDiConfronto);
				secondoTermine = espr.getRisultato();
			}
			else throw new OperazioneNonValidaException();
		}
		return eseguiConfronto(primoTermine,secondoTermine,op);
	}
	
	public static boolean isExprMatematica(ArrayList<Token> termineDiConfronto)
	{
		Tok[] listaMat = new Tok[] {Tok.INTERO,Tok.PIU,Tok.MENO,Tok.PER,Tok.DIVISO,Tok.MODULO,Tok.LEFT_PAR,Tok.RIGHT_PAR};
		ArrayList<Tok> exprMat = new ArrayList<Tok>();
		for(Tok t : listaMat)
			exprMat.add(t);
		Iterator i = termineDiConfronto.iterator();
		while(i.hasNext())
		{
			Token t = (Token)i.next();
			if(!exprMat.contains(t.ritornaTipoToken())) return false;
		}
		return true;
	}

	/**
	 * 
	 * @param Token x,Token y,Token operatore - 
	 * @return Booleano - Ritorna un token di tipo booleano
	 * @throws Exception - Lancia un'eccezione se si verifica un errore durante il confronto
	 */
	public Booleano eseguiConfronto(Token x, Token y, Token op) throws Exception
	{
		Tok tipo = op.ritornaTipoToken();
		if(tipo.equals(Tok.DIVERSO)||tipo.equals(Tok.MAGGIORE)||tipo.equals(Tok.MAGGIOREUGUALE)||tipo.equals(Tok.MINORE)||tipo.equals(Tok.MINOREUGUALE)||tipo.equals(Tok.UGUALE))
		{
			Object o1 = x.ritornaValore();
			Object o2 = y.ritornaValore();
			tipo = x.ritornaTipoToken();
			Tok tipo2 = y.ritornaTipoToken();
			if(!tipo.equals(tipo2))
				throw new OperazioneNonValidaException();
			else
			{
				switch(op.ritornaTipoToken())
				{
					case DIVERSO:
						return new Booleano(!o1.equals(o2));
					case MAGGIORE:
						if(tipo.equals(Tok.INTERO))
							return new Booleano((int)o1>(int)o2);
						throw new OperazioneNonValidaException();
					case MAGGIOREUGUALE:
						if(tipo.equals(Tok.INTERO))
							return new Booleano((int)o1>=(int)o2);
						throw new OperazioneNonValidaException();
					case MINORE:
						if(tipo.equals(Tok.INTERO))
							return new Booleano((int)o1<(int)o2);
						throw new OperazioneNonValidaException();
					case MINOREUGUALE:
						if(tipo.equals(Tok.INTERO))
							return new Booleano((int)o1<=(int)o2);
						throw new OperazioneNonValidaException();
					case UGUALE:
						return new Booleano(o1.equals(o2));
				}
			
			}
		}
		else throw new OperatoreMissingException();
		return null;
	}
	
	/**
	 * Controlla se il token è di tipo Stringa, Booleano o Intero
	 * @param Token - Token da verificarne il tipo
	 * @return boolean - Ritorna true in caso di token di tipo Booleano, Stringa o Intero, false altrimenti
	 */
	public boolean isTerm(Token t)
	{
		Tok tipo = t.ritornaTipoToken();
		return tipo.equals(Tok.STRINGA)||tipo.equals(Tok.BOOLEANO)||tipo.equals(Tok.INTERO);
	}
}
