package it.uniroma1.sapy.runtime.istruzioni;

import java.util.*;

import it.uniroma1.sapy.lexer.token.*;
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
		return analizzaExpr();
	}
	
	public Token analizzaExpr() throws Exception
	{
		ArrayList<Token> lineaCodice = new ArrayList<Token>();
		ArrayList<Token> operando = new ArrayList<Token>();
		while(point<text.size())
		{		
			Tok tipo = peek().ritornaTipoToken();
			if(tipo.equals(Tok.AND)||tipo.equals(Tok.OR))
			{
				Token operatore = peek();
				consume();
				if(operando.size()==1)
				{
					if(operando.get(0).ritornaTipoToken().equals(Tok.BOOLEANO))
					{
						lineaCodice.add(operando.get(0));
						lineaCodice.add(operatore);
						operando.clear();
					}
					else throw new BooleanParsingException();
				}
				else if(operando.size()>1)
				{
					if(isExprBooleana(operando))
					{
						for(Token t : operando)
							lineaCodice.add(t);
						lineaCodice.add(operatore);
						operando.clear();
					}
					else
					{
						try
						{
							Confronto c = new Confronto(operando);
							lineaCodice.add(c.getRisultato());
							operando.clear();
							lineaCodice.add(operatore);
						}
						catch(OperazioneNonValidaException e)
						{
							e.printStackTrace();
						}
					}
				}
				else throw new OperandoMissingException();
			}
			else if(tipo.equals(Tok.NOT))
			{
				lineaCodice.add(peek());
				consume();
				operando.clear();
			}
			else
			{
				operando.add(peek());
				consume();
			}
		}
		if(operando.size()==1)
		{
			if(operando.get(0).ritornaTipoToken().equals(Tok.BOOLEANO))
				lineaCodice.add(operando.get(0));
			else throw new BooleanParsingException();
		}
		else if(operando.size()>1)
		{
			if(isExprBooleana(operando))
				for(Token t : operando)
					lineaCodice.add(t);
			else
			{
				try
				{
					Confronto c = new Confronto(operando);
					lineaCodice.add(c.getRisultato());
				}
				catch(OperazioneNonValidaException e)
				{
					e.printStackTrace();
				}
			}
		}
		else throw new OperandoMissingException();
		ExprBooleana espressione = new ExprBooleana(lineaCodice);
		return espressione.getRisultato();
	}
	
	public static boolean isExprBooleana(ArrayList<Token> operando)
	{
		Tok[] listaBoole = new Tok[] {Tok.BOOLEANO,Tok.OR,Tok.AND,Tok.NOT,Tok.LEFT_PAR,Tok.RIGHT_PAR};
		ArrayList<Tok> exprBoole = new ArrayList<Tok>();
		for(Tok t : listaBoole)
			exprBoole.add(t);
		Iterator i = operando.iterator();
		while(i.hasNext())
		{
			Token t = (Token)i.next();
			if(!exprBoole.contains(t.ritornaTipoToken())) return false;
		}
		return true;
	}
}
