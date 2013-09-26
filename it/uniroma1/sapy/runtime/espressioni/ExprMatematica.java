package it.uniroma1.sapy.runtime.espressioni;

import it.uniroma1.sapy.exception.*;
import it.uniroma1.sapy.lexer.token.*;

import java.util.*;

/**
 * Espressione matematica.
 */
public class ExprMatematica extends Espressione
{
	
	/**
	 * Costruttore
	 * @param espressione - Espressione matematica da analizzare e risolvere.
	 */
	public ExprMatematica(ArrayList<Token> espressione){ super(espressione); }
	
	/**
	 * Ritorna il risultato dell'espressione matematica.
	 * @return risultato dell'espressione.
	 * @throws ExtraTokenException - se il numero di Token supera quello previsto.
	 * @throws OperazioneNonValidaException - se l'espressione da analizzare è costituito da Token non validi.
	 * @throws ParentesiParsingException - se si verifica un errore di corrispondenza tra le parentesi.
	 */
	@Override
	public Token getRisultato() throws ExtraTokenException, OperazioneNonValidaException, ParentesiParsingException
	{
		Token result = expr();
		if(point<text.size()) throw new ExtraTokenException();
		return result;
	}
	
	/**
	 * Punto di partenza nella risoluzione dell'espressione. Esegue la somma o la sottrazione in presenza di un Token di tipo PIU o MENO.
	 * @return risultato della somma o sottrazione, oppure il valore ritornato da molDivMod().
	 * @throws OperazioneNonValidaException - se l'espressione da analizzare è costituito da Token non validi.
	 * @throws ParentesiParsingException - se si verifica un errore di corrispondenza tra le parentesi.
	 */
	public Token expr() throws OperazioneNonValidaException, ParentesiParsingException
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
	 * Esegue:<br />- il prodotto tra due token, in presenza di un token di tipo PER<br />
	 * - la divisione, in presenza di un token di tipo DIVISO<br />
	 * - il modulo, in presenza di un token di tipo MODULO.
	 * @return risultato del prodotto, divisione o modulo, oppure il valore ritornato da menoUnario();
	 * @throws OperazioneNonValidaException - se l'espressione da analizzare è costituito da Token non validi.
	 * @throws ParentesiParsingException - se si verifica un errore di corrispondenza tra le parentesi.
	 */
	public Token molDivMod() throws OperazioneNonValidaException, ParentesiParsingException
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
	 * Ritorna l'opposto del valore del Token INTERO, in presenza di token di tipo MENO. 
	 * @return l'opposto del valore del Token INTERO o il valore ritornato da simpleExpr().
	 * @throws OperazioneNonValidaException - se l'espressione da analizzare è costituito da Token non validi.
	 * @throws ParentesiParsingException - se si verifica un errore di corrispondenza tra le parentesi.
	 */
	public Token menoUnario() throws OperazioneNonValidaException, ParentesiParsingException
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
	 * Analizza e risolve l'espressione all'interno delle parentesi.
	 * @return risultato dell'espressione all'interno delle parentesi, oppure il Token nella posizione dell'indice(point).
	 * @throws OperazioneNonValidaException - se l'espressione da analizzare è costituito da Token non validi.
	 * @throws ParentesiParsingException - se si verifica un errore di corrispondenza tra le parentesi.
	 */
	public Token simpleExpr() throws OperazioneNonValidaException, ParentesiParsingException
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
	 * Controlla se il token è di tipo Intero, quindi utilizzabile come operando per l'espressione.
	 * @param t - Token da analizzare.
	 * @return true se il Token è di tipo INTERO, false altrimenti.
	 */
	public boolean isTerm(Token t)
	{
		return t.ritornaTipoToken().equals(Tok.INTERO);
	}
}
