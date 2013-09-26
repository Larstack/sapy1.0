package it.uniroma1.sapy.runtime.espressioni;

import java.util.*;

import it.uniroma1.sapy.exception.*;
import it.uniroma1.sapy.lexer.token.*;

/**
 * Confronto tra uno o più Token. 
 */
public class Confronto extends Espressione
{
	/**
	 * Costruttore
	 * @param text - lista di Token che costituisce il confronto da risolvere.
	 */
	public Confronto(ArrayList<Token> text)
	{
		super(text);
	}
	
	/**
	 * Ritorna il risultato del confronto.
	 * @return risultato del confronto sotto forma di token di tipo Booleano. 
	 * @throws OperatoreMissingException - se si riscontra l'assenza di operatori.
	 * @throws OperazioneNonValidaException - se il confronto da analizzare è costituito da Token non validi.
	 * @throws OperandoMissingException - se manca un operando. 
	 * @throws ParentesiParsingException - se si verifica un errore di corrispondenza tra le parentesi.
	 * @throws ExtraTokenException - se il numero di Token supera quello previsto.
	 */
	@Override
	public Token getRisultato() throws OperazioneNonValidaException, OperatoreMissingException, ExtraTokenException, ParentesiParsingException, OperandoMissingException
	{	
		return analizzaExpr();
	}
	
	/**
	 * Analizza e risolve un confronto.
	 * @return risultato del confronto.
	 * @throws OperatoreMissingException - se si riscontra l'assenza di operatori.
	 * @throws OperazioneNonValidaException - se il confronto da analizzare è costituito da Token non validi.
	 * @throws OperandoMissingException - se manca un operando. 
	 * @throws ParentesiParsingException - se si verifica un errore di corrispondenza tra le parentesi.
	 * @throws ExtraTokenException - se il numero di Token supera quello previsto.
	 */
	public Booleano analizzaExpr() throws OperazioneNonValidaException, OperatoreMissingException, ExtraTokenException, ParentesiParsingException, OperandoMissingException
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
	
	/**
	 * Risolve l'espressione passata in input.
	 * @param espressione - espressione da risolvere.
	 * @return risultato dell'espressione.
	 * @throws OperazioneNonValidaException - se il confronto da analizzare è costituito da Token non validi.
	 * @throws OperandoMissingException - se manca un operando. 
	 * @throws ParentesiParsingException - se si verifica un errore di corrispondenza tra le parentesi.
	 * @throws ExtraTokenException - se il numero di Token supera quello previsto.
	 */
	public Token risolviEspressione(ArrayList<Token> espressione) throws ExtraTokenException, ParentesiParsingException, OperazioneNonValidaException, OperandoMissingException
	{
		Token risultato;
		if(Condizione.isExprBooleana(espressione))
		{	
			ExprBooleana espr = new ExprBooleana(espressione);
			risultato = espr.getRisultato();
		}
		else if(Condizione.isExprMatematica(espressione))
		{
			ExprMatematica espr = new ExprMatematica(espressione);
			risultato = espr.getRisultato();
		}
		else throw new OperandoMissingException();
		return risultato;
	}
}
