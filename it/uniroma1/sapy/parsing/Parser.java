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
		listaIstruzioni = creaListaDiIstruzioni(tokenLst);
		etichette = new HashSet<Integer>();
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
			}
			if(t.ritornaTipoToken().equals(Tok.DUEPUNTI)||t.ritornaTipoToken().equals(Tok.EOL))
			{
				i++;
				lineaCodice.clear();
				continue;
			}
			else if(t.ritornaTipoToken().equals(Tok.REM))
			{
				while(!t.ritornaTipoToken().equals(Tok.EOL)||!t.ritornaTipoToken().equals(Tok.DUEPUNTI))
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
					while(!t.ritornaTipoToken().equals(Tok.EOL)||!t.ritornaTipoToken().equals(Tok.DUEPUNTI))
					{
						lineaCodice.add(t);
						i++;
						if(!(i<listaToken.size()))
							break;
						t = listaToken.get(i);
					}
					i++;
					/*if(lineaCodice.size()==1)
					{
						if(lineaCodice.get(0).ritornaTipoToken().equals(Tok.STRINGA))
						{
							valVariabile = lineaCodice.get(0);
							VariabileIstruzione istrVariabile = new VariabileIstruzione(nomeVar,valVariabile,etichetta);
							istruzioneLst.add(istrVariabile);
							istrVariabile.esegui();
							lineaCodice.clear();
							continue;
						}
					}*/
					/////////////////
					VariabileIstruzione istrVariabile = new VariabileIstruzione(nomeVar,lineaCodice,etichetta);
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
				/*for(int j=0;j<lineaCodice.size();j++)
					if(lineaCodice.get(j).ritornaTipoToken().equals(Tok.VARIABILE))
					{
						String nome = (String)lineaCodice.get(j).ritornaValore();
						Token valoreVariabile = variabili.getVariabile(nome);
						if(!valoreVariabile.equals(null))
							lineaCodice.set(j, valoreVariabile);
					}
				Condizione cond = new Condizione(lineaCodice);
				condizione = (Booleano)cond.getRisultato();*/
				ArrayList<Token> condizione = lineaCodice;
				lineaCodice.clear();
				t = listaToken.get(i);
				while(!t.ritornaTipoToken().equals(Tok.ENDIF))
				{
					lineaCodice.add(t);
					i++;
					t = listaToken.get(i);
				}
				i++;
				/*for(int j=0;j<lineaCodice.size();j++)
					if(lineaCodice.get(j).ritornaTipoToken().equals(Tok.VARIABILE))
					{
						String nome = (String)lineaCodice.get(j).ritornaValore();
						Token valoreVariabile = variabili.getVariabile(nome);
						if(!valoreVariabile.equals(null))
							lineaCodice.set(j, valoreVariabile);
					}*/
				istrIf = new IfIstruzione(condizione,creaListaDiIstruzioni(lineaCodice),etichetta);
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
					/*for(int j=0;j<lineaCodice.size();j++)
						if(lineaCodice.get(j).ritornaTipoToken().equals(Tok.VARIABILE))
						{
							String nome = (String)lineaCodice.get(j).ritornaValore();
							Token valoreVariabile = variabili.getVariabile(nome);
							if(!valoreVariabile.equals(null))
								lineaCodice.set(j, valoreVariabile);
						}
					ExprMatematica esprMat = new ExprMatematica(lineaCodice);
					da = (Intero)esprMat.getRisultato();
					variabili.setVariabile(varItera, da);*/
					da = lineaCodice;
					lineaCodice.clear();
					t = listaToken.get(i);
					while(!t.ritornaTipoToken().equals(Tok.DO))
					{
						lineaCodice.add(t);
						i++;
						t = listaToken.get(i);
					}
					i++;
					/*for(int j=0;j<lineaCodice.size();j++)
						if(lineaCodice.get(j).ritornaTipoToken().equals(Tok.VARIABILE))
						{
							String nome = (String)lineaCodice.get(j).ritornaValore();
							Token valoreVariabile = variabili.getVariabile(nome);
							if(!valoreVariabile.equals(null))
								lineaCodice.set(j, valoreVariabile);
						}
					esprMat = new ExprMatematica(lineaCodice);
					finoA = (Intero)esprMat.getRisultato();*/
					finoA = lineaCodice;
					lineaCodice.clear();
					t = listaToken.get(i);
					while(!t.ritornaTipoToken().equals(Tok.NEXT))
					{
						lineaCodice.add(t);
						i++;
						t = listaToken.get(i);
					}
					i++;
					/*for(int j=0;j<lineaCodice.size();j++)
						if(lineaCodice.get(j).ritornaTipoToken().equals(Tok.VARIABILE))
						{
							String nome = (String)lineaCodice.get(j).ritornaValore();
							Token valoreVariabile = variabili.getVariabile(nome);
							if(!valoreVariabile.equals(null))
								lineaCodice.set(j, valoreVariabile);
						}*/
					istrFor = new ForIstruzione(da,finoA,varItera,creaListaDiIstruzioni(lineaCodice),etichetta);
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
				while(!t.ritornaTipoToken().equals(Tok.EOL)||!t.ritornaTipoToken().equals(Tok.DUEPUNTI))
				{
					lineaCodice.add(t);
					i++;
					if(!(i<listaToken.size()))
						break;
					t = listaToken.get(i);
				}
				i++;
				/*
				for(int j=0;j<lineaCodice.size();j++)
					if(lineaCodice.get(j).ritornaTipoToken().equals(Tok.VARIABILE))
					{
						String nome = (String)lineaCodice.get(j).ritornaValore();
						Token valoreVariabile = variabili.getVariabile(nome);
						if(!valoreVariabile.equals(null))
							lineaCodice.set(j, valoreVariabile);
					}
				if(lineaCodice.size()==1)
				{
					if(lineaCodice.get(0).ritornaTipoToken().equals(Tok.STRINGA))
					{
						daStampare = lineaCodice.get(0);
						PrintIstruzione istrPrint = new PrintIstruzione(daStampare,etichetta);
						istruzioneLst.add(istrPrint);
						lineaCodice.clear();
						continue;
					}
				}
				if(Confronto.isExprMatematica(lineaCodice))
				{
					ExprMatematica em = new ExprMatematica(lineaCodice);
					daStampare = em.getRisultato();
				}
				else if(Condizione.isExprBooleana(lineaCodice))
				{
					ExprBooleana eb = new ExprBooleana(lineaCodice);
					daStampare = eb.getRisultato();
				}
				else if(isConfronto(lineaCodice))
				{
					Condizione cond = new Condizione(lineaCodice);
					daStampare = cond.getRisultato();
				}
				else throw new ParsingException();*/
				daStampare = lineaCodice;
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
	
	public static boolean isConfronto(ArrayList<Token> termineDiConfronto)
	{
		Tok[] listaConfr = new Tok[] {Tok.STRINGA,Tok.DIVERSO,Tok.MAGGIORE,Tok.MAGGIOREUGUALE,Tok.MINORE,Tok.MINOREUGUALE,Tok.UGUALE,Tok.BOOLEANO,Tok.OR,Tok.AND,Tok.NOT,Tok.INTERO,Tok.PIU,Tok.MENO,Tok.PER,Tok.DIVISO,Tok.MODULO,Tok.LEFT_PAR,Tok.RIGHT_PAR};
		ArrayList<Tok> exprConfr = new ArrayList<Tok>();
		for(Tok t : listaConfr)
			exprConfr.add(t);
		Iterator i = termineDiConfronto.iterator();
		while(i.hasNext())
		{
			Token t = (Token)i.next();
			if(!exprConfr.contains(t.ritornaTipoToken())) return false;
		}
		return true;
	}	
}
