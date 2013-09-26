package it.uniroma1.sapy.runtime.espressioni;

import java.util.*;
import it.uniroma1.sapy.lexer.token.*;
import it.uniroma1.sapy.exception.*;

/**
 * Condizione di un'istruzione IF. Gestisce confronti ed espressioni booleane.
 */
public class Condizione extends Espressione
{	
	
	/**
	 * Costruttore
	 * @param condizione - lista di Token che costituisce la condizione.
	 */
	public Condizione(ArrayList<Token> condizione){ super(condizione); }
	
	/**
	 * Ritorna il risultato della condizione.
	 */
	@Override
	public Token getRisultato() throws OperandoMissingException, OperazioneNonValidaException, OperatoreMissingException, ExtraTokenException, ParentesiParsingException
	{
		if(text.isEmpty()) throw new OperandoMissingException();
		if(text.size()==1&&peek().ritornaTipoToken().equals(Tok.BOOLEANO))
			return (Booleano)peek();
		return analizzaExpr(text);
	}
	
	/**
	 * Analizza e risolve la condizione presa in input.
	 * @param espr - condizione o espressione da analizzare.
	 * @return risultato della condizione o espressione.
	 * @throws OperazioneNonValidaException - se la condizione da analizzare è costituita da espressioni non valide.
	 * @throws OperatoreMissingException - se si riscontra l'assenza di operatori.
	 * @throws ExtraTokenException - se il numero di Token dell'espressione o condizione supera quello previsto.
	 * @throws ParentesiParsingException - se si verifica un errore di corrispondenza tra le parentesi.
	 * @throws OperandoMissingException - se manca un operando.
	 */
	public Token analizzaExpr(ArrayList<Token> espr) throws OperazioneNonValidaException, OperatoreMissingException, ExtraTokenException, ParentesiParsingException, OperandoMissingException
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
			ExprBooleana esprex = new ExprBooleana(espr);
			return esprex.getRisultato();
		}
		else if(isExprMatematica(espr))
		{
			ExprMatematica esprex = new ExprMatematica(espr);
			return esprex.getRisultato();
		}
		else if(isConfronto(espr))
		{
			Confronto esprex = new Confronto(espr);
			return esprex.getRisultato();
		}
		else throw new OperazioneNonValidaException();
	}
	
	/**
	 * Verifica se l'espressione analizzata è un'espressione matematica.
	 * @param espressioneDaAnalizzare - espressione da analizzare.
	 * @return true se l'espressione è matematica, false altrimenti.
	 */
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
	
	/**
	 * Verifica se la sequenza di Token è un confronto.
	 * @param espressioneDaAnalizzare - lista di Token da analizzare.
	 * @return true se la sequenza di Token costituisce un confronto, false altrimenti.
	 */
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
	
	/**
	 * Verifica se l'espressione è booleana.
	 * @param espressioneDaAnalizzare - espressione da analizzare.
	 * @return true se l'espressione è booleana, false altrimenti.
	 */
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
