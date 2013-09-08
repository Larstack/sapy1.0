package it.uniroma1.sapy.runtime.istruzioni;

import java.util.*;
import it.uniroma1.sapy.exception.*;
import it.uniroma1.sapy.lexer.token.*;

public class ExprBooleana extends Espressione
{	
	public ExprBooleana(ArrayList<Token> espressione){ super(espressione); }
	
	@Override
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
	
	public void consume(){ point+=1; }
	
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
	
	public Token peek(){ return text.get(point); }
	
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
		return t.ritornaTipoToken().equals(Tok.BOOLEANO);
	}
	public static void main(String[] args) throws Exception {
		/*array("false", "and", "true", "or", "true", "and",
		 *  "(", "false", "or", "false", "or", "not", "true", ")");
		 */
		ArrayList<Token> t = new ArrayList<Token>();
		t.add(new Booleano(false));
		t.add(new And());
		t.add(new Booleano(true));
		t.add(new Or());
		t.add(new Booleano(true));
		t.add(new And());
		t.add(new LeftPar());
		t.add(new Booleano(false));
		t.add(new Or());
		t.add(new Booleano(false));
		t.add(new Or());
		t.add(new Not());
		t.add(new Booleano(true));
		t.add(new RightPar());
		t.add(new Or());
		t.add(new Booleano(true));
		t.add(new And());
		t.add(new LeftPar());
		t.add(new Booleano(true));
		t.add(new Or());
		t.add(new Not());
		t.add(new Booleano(true));
		t.add(new RightPar());		
		ExprBooleana a = new ExprBooleana(t);
		System.out.println(a.getRisultato().ritornaValore());
	}
}
