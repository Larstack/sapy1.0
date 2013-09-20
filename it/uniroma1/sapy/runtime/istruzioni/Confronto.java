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
		return analizzaExpr();
	}
	
	public Booleano analizzaExpr() throws Exception
	{
		ArrayList<Token> espressione = new ArrayList<Token>();
		Tok tipo = peek().ritornaTipoToken();
		while(!tipo.equals(Tok.DIVERSO)&&!tipo.equals(Tok.MAGGIORE)&&!tipo.equals(Tok.MAGGIOREUGUALE)&&!tipo.equals(Tok.MINORE)&&!tipo.equals(Tok.MINOREUGUALE)&&!tipo.equals(Tok.UGUALE))
		{
			espressione.add(peek());
			consume();
			if(point==text.size()) break;
			tipo = peek().ritornaTipoToken();
		}
		Token operatore = peek();
		consume();
		Token primoTermine = risolviEspressione(espressione);
		espressione.clear();
		while(point<text.size())
		{
			espressione.add(peek());
			consume();
		}
		Token secondoTermine = risolviEspressione(espressione);
		Object o1 = primoTermine.ritornaValore();
		Object o2 = secondoTermine.ritornaValore();
		switch(operatore.ritornaTipoToken())
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
			default:
				throw new OperatoreMissingException();
		}
	}
	
	public Token risolviEspressione(ArrayList<Token> espressione) throws Exception
	{
		Token risultato;
		if(Condizione.isExprBooleana(espressione))
		{	
			Espressione espr = new ExprBooleana(espressione);
			risultato = espr.getRisultato();
		}
		else if(Condizione.isExprMatematica(espressione))
		{
			Espressione espr = new ExprMatematica(espressione);
			risultato = espr.getRisultato();
		}
		else throw new OperandoMissingException();
		return risultato;
	}
}
