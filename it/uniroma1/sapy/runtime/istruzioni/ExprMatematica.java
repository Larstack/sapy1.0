package it.uniroma1.sapy.runtime.istruzioni;

import it.uniroma1.sapy.exception.*;
import it.uniroma1.sapy.lexer.token.*;

import java.util.*;

public class ExprMatematica extends Espressione
{
	public ExprMatematica(ArrayList<Token> t){ super(t); }
	
	@Override
	public Token getRisultato() throws Exception
	{
		Token result = expr();
		if(point<text.size()) throw new ExtraTokenException();
		return result;
	}
	
	public void consume(){ point+=1; }
	
	public Token peek(){ return text.get(point); }
	
	public Token expr() throws Exception
	{
		Token x = molDivMod();
		if(!(point>text.size()-1))
		{	while(peek().ritornaTipoToken().equals(Tok.PIU)||peek().ritornaTipoToken().equals(Tok.MENO))
			{
				Token op = peek();
				consume();
				Token y = molDivMod();
				int n1 = (int)x.ritornaValore();
				int n2 = (int)y.ritornaValore();
				if(op.ritornaTipoToken().equals(Tok.PIU))
					x = new Intero(n1 + n2);
				else 
					x =new Intero(n1-n2);
				if(point>text.size()-1) break;
			}
		}
		return x;
	}

	public Token molDivMod() throws Exception
	{
		Token x = menoUnario();
		if(!(point>text.size()-1))
			while(peek().ritornaTipoToken().equals(Tok.MODULO)||peek().ritornaTipoToken().equals(Tok.PER)||peek().ritornaTipoToken().equals(Tok.DIVISO))
			{
				Token op = peek();
				consume();
				Token y = menoUnario();///////
				int n1 = (int)x.ritornaValore();
				int n2 = (int)y.ritornaValore();
				if(op.ritornaTipoToken().equals(Tok.PER)) x = new Intero(n1*n2);
				else if(op.ritornaTipoToken().equals(Tok.DIVISO)) x = new Intero(n1/n2);
				else x = new Intero(n1%n2);
				if(point>text.size()-1) break;
			}
		return x;
	}
	public Token menoUnario() throws Exception
	{
		if(peek().ritornaTipoToken().equals(Tok.MENO))
		{
			consume();
			Token x = simpleExpr();
//			consume();
			return new Intero(-((int)x.ritornaValore()));
		}
		else if(peek().ritornaTipoToken().equals(Tok.PIU))
		{
			consume();
			Token x = simpleExpr();
			return new Intero((int)x.ritornaValore());			
		}
		else return simpleExpr();
	}
	
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
	
	public boolean isTerm(Token t)
	{
		return t.ritornaTipoToken().equals(Tok.INTERO);
	}
}
