package it.uniroma1.sapy.parsing;

import java.util.*;

import it.uniroma1.sapy.exception.*;
import it.uniroma1.sapy.lexer.token.*;
import it.uniroma1.sapy.runtime.ProgrammaEseguibile;
import it.uniroma1.sapy.runtime.VarRepository;
import it.uniroma1.sapy.runtime.istruzioni.*;

/**
 * Partendo da una lista di token, trasforma quest'ultima in una lista di istruzioni
 * organizzata in un ProgrammaEseguibile. 
 * @author Leonardo Andres Ricciotti
 */
public class Parser
{
	private ArrayList<Token> tokenLst;
	private ArrayList<Istruzione> listaIstruzioni;
	private VarRepository variabili;
	private HashSet<Integer> etichette;
	
	/**
	 * Costruttore
	 * @param ArrayList<Token> - Lista di elementi di tipo Token
	 */
	public Parser(ArrayList<Token> listaToken) throws Exception
	{
		tokenLst = listaToken;
		variabili = VarRepository.getInstance();
		etichette = new HashSet<Integer>();
		listaIstruzioni = creaListaDiIstruzioni(tokenLst);
	}
	
	public ArrayList<Istruzione> getListaIstruzioni()
	{
		return listaIstruzioni;
	}
	/**
	 * Crea la lista di istruzioni interpretate
	 */
	public ArrayList<Istruzione> creaListaDiIstruzioni(ArrayList<Token> listaToken) throws Exception
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
					else throw new ParsingException();
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
					else throw new ParsingException();
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
					else throw new ParsingException();
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
					else throw new ParsingException();
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
				else throw new ParsingException();
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
					else throw new ParsingException();
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
					else throw new ParsingException();
				}
				lineaCodice.clear();
				t = listaToken.get(i);
				if(t.ritornaTipoToken().equals(Tok.INTERO))
					label = (Intero)t;
				else throw new ParsingException();
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
					else throw new ParsingException();
				}
				lineaCodice.clear();
				t = listaToken.get(i);
				if(t.ritornaTipoToken().equals(Tok.VARIABILE))
					varStringa = (String)t.ritornaValore();
				else throw new ParsingException();
				InputIstruzione istrInput = new InputIstruzione(varStringa,etichetta);
				istruzioneLst.add(istrInput);
				i++;
				lineaCodice.clear();
			}
			else throw new ParsingException();
		}
		return istruzioneLst;
	}
	
	public ProgrammaEseguibile creaProgrammaEseguibile()
	{
		return new ProgrammaEseguibile(getListaIstruzioni());
	}	
}
