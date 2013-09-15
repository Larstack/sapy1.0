package it.uniroma1.sapy.runtime.istruzioni;

import it.uniroma1.sapy.exception.*;
import it.uniroma1.sapy.lexer.token.*;

import java.util.*;

/**
 * Espressione matematica - Estende la classe Espressione
 */
public class ExprMatematica extends Espressione
{
	
	/**
	 * Costruttore
	 * @param ArrayList<Token> - Espressione matematica da risolvere
	 */
	public ExprMatematica(ArrayList<Token> t){ super(t); }
	
	/**
	 * Ritorna il risultato dell'espressione matematica
	 * @return Intero - Ritorna un token di tipo Intero 
	 */
	@Override
	public Token getRisultato() throws Exception
	{
		Token result = expr();
		if(point<text.size()) throw new ExtraTokenException();
		return result;
	}
	
	/**
	 * Punto di partenza nella risoluzione dell'espressione. Esegue la somma o la sottrazione in presenza di un token di tipo Piu o Meno
	 * @return Intero - Ritorna un token di tipo Intero
	 * @throws Exception - Lancia un'eccezione se si verifica un errore durante la risoluzione dell'espressione
	 */
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

	/**
	 * Esegue:<br />- il prodotto tra due token, in presenza di un token di tipo Per<br />
	 * - la divisione, in presenza di un token di tipo Diviso<br />
	 * - il modulo, in presenza di un token di tipo Modulo.
	 * @return Intero - Ritorna un token di tipo Intero
	 * @throws Exception - Lancia un'eccezione se si verifica un errore durante la risoluzione dell'espressione
	 */
	public Token molDivMod() throws Exception
	{
		Token x = menoUnario();
		if(!(point>text.size()-1))
			while(peek().ritornaTipoToken().equals(Tok.MODULO)||peek().ritornaTipoToken().equals(Tok.PER)||peek().ritornaTipoToken().equals(Tok.DIVISO))
			{
				Token op = peek();
				consume();
				Token y = menoUnario();
				int n1 = (int)x.ritornaValore();
				int n2 = (int)y.ritornaValore();
				if(op.ritornaTipoToken().equals(Tok.PER)) x = new Intero(n1*n2);
				else if(op.ritornaTipoToken().equals(Tok.DIVISO)) x = new Intero(n1/n2);
				else x = new Intero(n1%n2);
				if(point>text.size()-1) break;
			}
		return x;
	}
	
	/**
	 * Ritorna il token Intero con valore opposto, in presenza di token di tipo Meno 
	 * @return Intero - Ritorna un token di tipo Intero
	 */
	public Token menoUnario() throws Exception
	{
		if(peek().ritornaTipoToken().equals(Tok.MENO))
		{
			consume();
			Token x = simpleExpr();
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

	/**
	 * In presenza di token di tipo LeftPar inizia l'analisi del contenuto tra parentesi, altrimenti ritorna l'Intero se il token è di questo tipo.
	 * @return Intero - Ritorna un token di tipo Intero
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
	 * Controlla se il token è di tipo Intero, quindi utilizzabile come operando
	 * @param Token - Token da verificarne il tipo
	 * @return boolean - Ritorna true in caso di token di tipo Intero, false altrimenti
	 */
	public boolean isTerm(Token t)
	{
		return t.ritornaTipoToken().equals(Tok.INTERO);
	}
}
