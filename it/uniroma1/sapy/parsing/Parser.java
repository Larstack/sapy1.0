package it.uniroma1.sapy.parsing;

import java.util.*;

import it.uniroma1.sapy.exception.*;
import it.uniroma1.sapy.lexer.token.*;
import it.uniroma1.sapy.runtime.ProgrammaEseguibile;
import it.uniroma1.sapy.runtime.VarRepository;
import it.uniroma1.sapy.runtime.istruzioni.*;

/**
 * Trasforma una lista di Token in una lista di istruzioni, organizzata in un ProgrammaEseguibile.
 * @author Leonardo Andres Ricciotti
 */
public class Parser
{
	/**
	 * Lista di Token data in input al Parser.
	 */
	private ArrayList<Token> tokenLst;
	
	/**
	 * Lista di istruzioni costruita durante il parsing.
	 */
	private ArrayList<Istruzione> listaIstruzioni;
	
	/**
	 * Istanza di VarRepository, dove salvare il valore delle variabili.
	 */
	private VarRepository variabili;
	
	/**
	 * Set per memorizzare le etichette delle istruzioni, per evitare duplicati.
	 */
	private HashSet<Integer> etichette;
	
	/**
	 * Costruttore
	 * @param listaToken - lista di elementi di tipo Token.
	 */
	public Parser(ArrayList<Token> listaToken)
	{
		tokenLst = listaToken;
		variabili = VarRepository.getInstance();
		etichette = new HashSet<Integer>();
		try
		{
			listaIstruzioni = creaListaDiIstruzioni(tokenLst);
		}
		catch (EtichettaDuplicataException | ParsingException | TokenInaspettatoException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Ritorna la lista di istruzioni costruita durante il parsing.	
	 * @return lista di istruzioni.
	 */
	public ArrayList<Istruzione> getListaIstruzioni()
	{
		return listaIstruzioni;
	}
	
	/**
	 * Crea la lista di istruzioni.
	 * @param listaToken - lista di Token da trasformare in istruzioni.
	 * @throws EtichettaDuplicataException - se si riscontra un'etichetta duplicata.
	 * @throws TokenInaspettatoException - se il Token atteso era di un altro tipo.
	 * @throws ParsingException - se il Token analizzato non viene individuato come Token valido.
	 */
	public ArrayList<Istruzione> creaListaDiIstruzioni(ArrayList<Token> listaToken) throws EtichettaDuplicataException, TokenInaspettatoException, ParsingException
	{
		ArrayList<Istruzione> istruzioneLst = new ArrayList<Istruzione>();
		ArrayList<Token> lineaCodice = new ArrayList<Token>();
		Token t;
		int i = 0;
		while(i<listaToken.size())
		{
			t = listaToken.get(i);
			if(t.ritornaTipoToken().equals(Tok.INTERO))
			{
				lineaCodice.add(t);
				i++;
				t = listaToken.get(i);
			}
			if(t.ritornaTipoToken().equals(Tok.DUEPUNTI)||t.ritornaTipoToken().equals(Tok.EOL))
			{
				i++;
				lineaCodice.clear();
				continue;
			}
			else if(t.ritornaTipoToken().equals(Tok.REM))
			{
				while(!t.ritornaTipoToken().equals(Tok.EOL))
				{
					i++;
					if(!(i<listaToken.size()))
						break;
					t = listaToken.get(i);
				}
				i++;
				lineaCodice.clear();
			}
			else if(t.ritornaTipoToken().equals(Tok.END))
			{
				i++;
				Intero etichetta = null;
				if(!lineaCodice.isEmpty())
				{
					if(lineaCodice.get(0).ritornaTipoToken().equals(Tok.INTERO))
					{
						etichetta = (Intero)lineaCodice.get(lineaCodice.size()-1);
						if(etichette.contains(etichetta.ritornaValore()))
							throw new EtichettaDuplicataException((int)etichetta.ritornaValore());
						etichette.add((int)etichetta.ritornaValore());
					}
					else throw new TokenInaspettatoException();
				}
				lineaCodice.clear();
				EndIstruzione istrEnd = new EndIstruzione(etichetta);
				istruzioneLst.add(istrEnd);
			}
			else if(t.ritornaTipoToken().equals(Tok.VARIABILE))
			{
				i++;
				String nomeVar = (String)t.ritornaValore();
				Intero etichetta = null;
				if(!lineaCodice.isEmpty())
				{
					if(lineaCodice.get(0).ritornaTipoToken().equals(Tok.INTERO))
					{
						etichetta = (Intero)lineaCodice.get(lineaCodice.size()-1);
						if(etichette.contains(etichetta.ritornaValore()))
							throw new EtichettaDuplicataException((int)etichetta.ritornaValore());
						etichette.add((int)etichetta.ritornaValore());
					}
					else throw new TokenInaspettatoException();
				}
				lineaCodice.clear();
				t = listaToken.get(i);
				if(t.ritornaTipoToken().equals(Tok.UGUALE))
				{
					i++;
					t = listaToken.get(i);
					while(!t.ritornaTipoToken().equals(Tok.EOL)&&!t.ritornaTipoToken().equals(Tok.DUEPUNTI))
					{
						lineaCodice.add(t);
						i++;
						if(!(i<listaToken.size()))
							break;
						t = listaToken.get(i);
					}
					i++;
					VariabileIstruzione istrVariabile = new VariabileIstruzione(nomeVar,(ArrayList<Token>)lineaCodice.clone(),etichetta);
					istruzioneLst.add(istrVariabile);
					lineaCodice.clear();
				}
			}
			else if(t.ritornaTipoToken().equals(Tok.IF))
			{
				Intero etichetta = null;
				IfIstruzione istrIf;
				if(!lineaCodice.isEmpty())
				{
					if(lineaCodice.get(0).ritornaTipoToken().equals(Tok.INTERO))
					{
						etichetta = (Intero)lineaCodice.get(lineaCodice.size()-1);
						if(etichette.contains(etichetta.ritornaValore()))
							throw new EtichettaDuplicataException((int)etichetta.ritornaValore());
						etichette.add((int)etichetta.ritornaValore());
					}
					else throw new TokenInaspettatoException();
				}
				lineaCodice.clear();
				i++;
				t = listaToken.get(i);
				while(!t.ritornaTipoToken().equals(Tok.THEN))
				{
					lineaCodice.add(t);
					i++;
					t = listaToken.get(i);
				}
				i++;
				ArrayList<Token> condizione = (ArrayList<Token>)lineaCodice.clone();
				lineaCodice.clear();
				t = listaToken.get(i);
				while(!t.ritornaTipoToken().equals(Tok.ENDIF))
				{
					lineaCodice.add(t);
					i++;
					if(!(i<listaToken.size()))
						break;
					t = listaToken.get(i);
				}
				i++;
				istrIf = new IfIstruzione(condizione,creaListaDiIstruzioni((ArrayList<Token>)lineaCodice.clone()),etichetta);
				lineaCodice.clear();
				istruzioneLst.add(istrIf);
			}
			else if(t.ritornaTipoToken().equals(Tok.FOR))
			{
				i++;
				ArrayList<Token> da;
				ArrayList<Token> finoA;
				String varItera;
				ForIstruzione istrFor;
				Intero etichetta = null;
				if(!lineaCodice.isEmpty())
				{
					if(lineaCodice.get(0).ritornaTipoToken().equals(Tok.INTERO))
					{
						etichetta = (Intero)lineaCodice.get(lineaCodice.size()-1);
						if(etichette.contains(etichetta.ritornaValore()))
							throw new EtichettaDuplicataException((int)etichetta.ritornaValore());
						etichette.add((int)etichetta.ritornaValore());
					}
					else throw new TokenInaspettatoException();
				}
				lineaCodice.clear();
				t = listaToken.get(i);
				if(t.ritornaTipoToken().equals(Tok.VARIABILE))
				{
					varItera = (String)t.ritornaValore();
					i = i+2;
					t = listaToken.get(i);
					while(!t.ritornaTipoToken().equals(Tok.TO))
					{
						lineaCodice.add(t);
						i++;
						t = listaToken.get(i);
					}
					i++;
					da = (ArrayList<Token>)lineaCodice.clone();
					lineaCodice.clear();
					t = listaToken.get(i);
					while(!t.ritornaTipoToken().equals(Tok.DO))
					{
						lineaCodice.add(t);
						i++;
						t = listaToken.get(i);
					}
					i++;
					finoA = (ArrayList<Token>)lineaCodice.clone();
					lineaCodice.clear();
					t = listaToken.get(i);
					while(!t.ritornaTipoToken().equals(Tok.NEXT))
					{
						lineaCodice.add(t);
						i++;
						t = listaToken.get(i);
					}
					i++;
					istrFor = new ForIstruzione(da,finoA,varItera,creaListaDiIstruzioni((ArrayList<Token>)lineaCodice.clone()),etichetta);
					lineaCodice.clear();
					istruzioneLst.add(istrFor);
				}
				else throw new TokenInaspettatoException();
			}
			else if(t.ritornaTipoToken().equals(Tok.PRINT))
			{
				ArrayList<Token> daStampare;
				Intero etichetta = null;
				if(!lineaCodice.isEmpty())
				{
					if(lineaCodice.get(0).ritornaTipoToken().equals(Tok.INTERO))
					{
						etichetta = (Intero)lineaCodice.get(lineaCodice.size()-1);
						if(etichette.contains(etichetta.ritornaValore()))
							throw new EtichettaDuplicataException((int)etichetta.ritornaValore());
						etichette.add((int)etichetta.ritornaValore());
					}
					else throw new TokenInaspettatoException();
				}
				lineaCodice.clear();
				i++;
				t = listaToken.get(i);
				while(!t.ritornaTipoToken().equals(Tok.EOL)&&!t.ritornaTipoToken().equals(Tok.DUEPUNTI))
				{
					lineaCodice.add(t);
					i++;
					if(!(i<listaToken.size()))
						break;
					t = listaToken.get(i);
				}
				i++;
				daStampare = (ArrayList<Token>)lineaCodice.clone();
				PrintIstruzione istrPrint = new PrintIstruzione(daStampare,etichetta);
				istruzioneLst.add(istrPrint);
				lineaCodice.clear();		
			}
			else if(t.ritornaTipoToken().equals(Tok.GOTO))
			{
				i++;
				Intero label;
				Intero etichetta = null;
				if(!lineaCodice.isEmpty())
				{
					if(lineaCodice.get(0).ritornaTipoToken().equals(Tok.INTERO))
					{
						etichetta = (Intero)lineaCodice.get(lineaCodice.size()-1);
						if(etichette.contains(etichetta.ritornaValore()))
							throw new EtichettaDuplicataException((int)etichetta.ritornaValore());
						etichette.add((int)etichetta.ritornaValore());
					}
					else throw new TokenInaspettatoException();
				}
				lineaCodice.clear();
				t = listaToken.get(i);
				if(t.ritornaTipoToken().equals(Tok.INTERO))
					label = (Intero)t;
				else throw new TokenInaspettatoException();
				GotoIstruzione istrGoto = new GotoIstruzione(label,etichetta);
				istruzioneLst.add(istrGoto);
				i++;
				lineaCodice.clear();
			}
			else if(t.ritornaTipoToken().equals(Tok.INPUT))
			{
				i++;
				String varStringa;
				Intero etichetta = null;
				if(!lineaCodice.isEmpty())
				{
					if(lineaCodice.get(0).ritornaTipoToken().equals(Tok.INTERO))
					{
						etichetta = (Intero)lineaCodice.get(lineaCodice.size()-1);
						if(etichette.contains(etichetta.ritornaValore()))
							throw new EtichettaDuplicataException((int)etichetta.ritornaValore());
						etichette.add((int)etichetta.ritornaValore());
					}
					else throw new TokenInaspettatoException();
				}
				lineaCodice.clear();
				t = listaToken.get(i);
				if(t.ritornaTipoToken().equals(Tok.VARIABILE))
					varStringa = (String)t.ritornaValore();
				else throw new TokenInaspettatoException();
				InputIstruzione istrInput = new InputIstruzione(varStringa,etichetta);
				istruzioneLst.add(istrInput);
				i++;
				lineaCodice.clear();
			}
			else throw new ParsingException();
		}
		return istruzioneLst;
	}
	
	/**
	 * Trasforma la lista di istruzioni costruita durante il parsing in un ProgrammaEseguibile.
	 * @return programma eseguibile.
	 */
	public ProgrammaEseguibile creaProgrammaEseguibile()
	{
		return new ProgrammaEseguibile(getListaIstruzioni());
	}	
}
