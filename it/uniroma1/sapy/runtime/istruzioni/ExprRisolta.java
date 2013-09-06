package it.uniroma1.sapy.runtime.istruzioni;

import java.util.*;
import it.uniroma1.sapy.exception.*;
import it.uniroma1.sapy.lexer.token.*;

public class ExprRisolta
{
	private ArrayList<Token> text;
	private int point = 0;
	
	public ExprRisolta(ArrayList<Token> espressione)
	{
		this.text = espressione;
	}
	
	public Token getRisultato() throws Exception
	{	
		Token result = expr();
		if(point<text.size()) throw new ExtraTokenException();
		return result;
	}
	
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
	
	public void consume()
	{
		point+=1;
	}
	
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
	
	public Token peek()
	{
		return text.get(point);
	}
	
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
	
	public Token simpleExpr() throws Exception
	{
		Token t = peek();
		if(t.ritornaTipoToken().equals(Tok.LEFT_PAR))
		{
			consume();
			Token result = expr(); 
			consume();
			return result;
		}
		else if(isTerm(t))
		{
			consume();
			return t;
		}
		else throw new ParsingException();
	}
	
	public boolean isTerm(Token t)
	{
		Tok[] operatori = new Tok[] {Tok.AND,Tok.OR,Tok.NOT,Tok.PIU,Tok.MENO,Tok.PER,Tok.DIVISO,Tok.MODULO};
		for(Tok tokk : operatori)
			if(t.ritornaTipoToken().equals(tokk)) return false;
		return true;
	}
}
