package it.uniroma1.sapy.runtime.istruzioni;

import java.lang.reflect.Array;
import java.util.*;

import it.uniroma1.sapy.lexer.token.*;
import it.uniroma1.sapy.parsing.Parser;
import it.uniroma1.sapy.exception.*;

public class Condizione extends Espressione
{	
	public Condizione(ArrayList<Token> t){ super(t); }
	
	@Override
	public Token getRisultato() throws Exception
	{
		if(text.isEmpty()) throw new OperandoMissingException();
		if(text.size()==1&&peek().ritornaTipoToken().equals(Tok.BOOLEANO))
			return (Booleano)peek();
		return analizzaExpr(text);
	}
	
	public Token analizzaExpr(ArrayList<Token> espr) throws Exception
	{
		ArrayList<Token> espressione = new ArrayList<Token>();
		Token t = peek();
		if(t.ritornaTipoToken().equals(Tok.LEFT_PAR))
		{
			consume();
			while(!peek().ritornaTipoToken().equals(Tok.RIGHT_PAR))
			{
				espressione.add(peek());
				consume();
				if(point==text.size()) break;
			}
			return analizzaExpr(espressione);
		}
		else if(isExprBooleana(espr))
		{
			Espressione esprex = new ExprBooleana(espr);
			return esprex.getRisultato();
		}
		else if(isExprMatematica(espr))
		{
			Espressione esprex = new ExprMatematica(espr);
			return esprex.getRisultato();
		}
		else if(isConfronto(espr))
		{
			Espressione esprex = new Confronto(espr);
			return esprex.getRisultato();
		}
		else throw new OperazioneNonValidaException();
	}
	
	public static boolean isExprMatematica(ArrayList<Token> espressioneDaAnalizzare)
	{
		Tok[] listaMat = new Tok[] {Tok.INTERO,Tok.PIU,Tok.MENO,Tok.PER,Tok.DIVISO,Tok.MODULO,Tok.LEFT_PAR,Tok.RIGHT_PAR};
		ArrayList<Tok> exprMat = new ArrayList<Tok>();
		for(Tok t : listaMat)
			exprMat.add(t);
		Iterator i = espressioneDaAnalizzare.iterator();
		while(i.hasNext())
		{
			Token t = (Token)i.next();
			if(!exprMat.contains(t.ritornaTipoToken())) return false;
		}
		return true;
	}
	
	public static boolean isConfronto(ArrayList<Token> espressioneDaAnalizzare)
	{
		Tok[] listaConfr = new Tok[] {Tok.STRINGA,Tok.DIVERSO,Tok.MAGGIORE,Tok.MAGGIOREUGUALE,Tok.MINORE,Tok.MINOREUGUALE,Tok.UGUALE,Tok.BOOLEANO,Tok.OR,Tok.AND,Tok.NOT,Tok.INTERO,Tok.PIU,Tok.MENO,Tok.PER,Tok.DIVISO,Tok.MODULO,Tok.LEFT_PAR,Tok.RIGHT_PAR};
		ArrayList<Tok> exprConfr = new ArrayList<Tok>();
		for(Tok t : listaConfr)
			exprConfr.add(t);
		Iterator i = espressioneDaAnalizzare.iterator();
		while(i.hasNext())
		{
			Token t = (Token)i.next();
			if(!exprConfr.contains(t.ritornaTipoToken())) return false;
		}
		return true;
	}
	
	public static boolean isExprBooleana(ArrayList<Token> espressioneDaAnalizzare)
	{
		Tok[] listaBoole = new Tok[] {Tok.BOOLEANO,Tok.OR,Tok.AND,Tok.NOT,Tok.LEFT_PAR,Tok.RIGHT_PAR};
		ArrayList<Tok> exprBoole = new ArrayList<Tok>();
		for(Tok t : listaBoole)
			exprBoole.add(t);
		Iterator i = espressioneDaAnalizzare.iterator();
		while(i.hasNext())
		{
			Token t = (Token)i.next();
			if(!exprBoole.contains(t.ritornaTipoToken())) return false;
		}
		return true;
	}
	
}
