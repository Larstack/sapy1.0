package it.uniroma1.sapy.runtime.espressioni;

import java.util.*;
import it.uniroma1.sapy.exception.*;
import it.uniroma1.sapy.lexer.token.*;

/**
 * Espressione booleana.
 */
public class ExprBooleana extends Espressione
{	
	
	/**
	 * Costruttore
	 * @param espressione - espressione booleana da risolvere.
	 */
	public ExprBooleana(ArrayList<Token> espressione){ super(espressione); }
	
	/**
	 * Ritorna il risultato dell'espressione booleana.
	 * @return token di tipo Booleano, che costituisce il risultato dell'espressione. 
	 * @throws ExtraTokenException - se il numero di Token supera quello previsto. 
	 * @throws OperazioneNonValidaException - se l'espressione da analizzare è costituito da Token non validi.
	 * @throws ParentesiParsingException - se si verifica un errore di corrispondenza tra le parentesi.
	 */
	@Override
	public Token getRisultato() throws ExtraTokenException, ParentesiParsingException, OperazioneNonValidaException
	{	
		Token result = expr();
		if(point<text.size()) throw new ExtraTokenException();
		return result;
	}
	
	/**
	 * Punto di partenza nella risoluzione dell'espressione. Esegue la somma logica in presenza di un Token di tipo OR.
	 * @return risultato della somma logica o dell'intera espressione booleana in analisi.
	 * @throws OperazioneNonValidaException - se l'espressione da analizzare è costituito da Token non validi.
	 * @throws ParentesiParsingException - se si verifica un errore di corrispondenza tra le parentesi.
	 */
	public Token expr() throws ParentesiParsingException, OperazioneNonValidaException
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
	 * Esegue il prodotto logico tra due token di tipo BOOLEANO, in presenza di un token di tipo AND.
	 * @return risultato del prodotto logico o del valore ritornato da notExpr().
	 * @throws OperazioneNonValidaException - se l'espressione da analizzare è costituito da Token non validi.
	 * @throws ParentesiParsingException - se si verifica un errore di corrispondenza tra le parentesi.
	 */
	public Token andExpr() throws ParentesiParsingException, OperazioneNonValidaException
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
	 * Complementa il valore del token in presenza di un token di tipo NOT. 
	 * @return valore negato del Token di tipo BOOLEANO o valore restituito da simpleExpr().
	 * @throws OperazioneNonValidaException - se l'espressione da analizzare è costituito da Token non validi.
	 * @throws ParentesiParsingException - se si verifica un errore di corrispondenza tra le parentesi.
	 */
	public Token notExpr() throws ParentesiParsingException, OperazioneNonValidaException
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
	 * In presenza di Token di tipo LEFT_PAR inizia l'analisi del contenuto tra parentesi, altrimenti ritorna il BOOLEANO se il token è di questo tipo.
	 * @return Token nella posizione indicata dall'indice(point) o risultato dell'espressione tra parentesi.
	 * @throws OperazioneNonValidaException - se l'espressione da analizzare è costituito da Token non validi.
	 * @throws ParentesiParsingException - se si verifica un errore di corrispondenza tra le parentesi.
	 */
	public Token simpleExpr() throws ParentesiParsingException, OperazioneNonValidaException
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
	 * Controlla se il token è di tipo BOOLEANO, quindi compatibile con un'operazione logica.
	 * @param t - Token da verificarne il tipo.
	 * @return true se il Token è di tipo BOOLEANO, false altrimenti.
	 */
	public boolean isTerm(Token t)
	{
		return t.ritornaTipoToken().equals(Tok.BOOLEANO);
	}
}
