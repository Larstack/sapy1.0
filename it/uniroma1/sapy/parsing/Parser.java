package it.uniroma1.sapy.parsing;

import java.util.*;

import it.uniroma1.sapy.exception.SintassiException;
import it.uniroma1.sapy.lexer.token.*;
import it.uniroma1.sapy.runtime.istruzioni.Istruzione;

/**
 * Partendo da una lista di token, trasforma quest'ultima in una lista di istruzioni
 * organizzata in un ProgrammaEseguibile. 
 * @author Leonardo Andres Ricciotti
 */
public class Parser
{
	private ArrayList<Token> listaToken;
	private ArrayList<Istruzione> listaIstruzioni;
	
	/**
	 * Costruttore
	 * @param ArrayList<Token> - Lista di elementi di tipo Token
	 */
	public Parser(ArrayList<Token> listaToken)
	{
		this.listaToken = listaToken;
		creaListaDiIstruzioni();
	}
	
	/**
	 * Crea la lista di istruzioni interpretate
	 */
	public void creaListaDiIstruzioni()
	{
		ArrayList<Token> lineaCodice = new ArrayList<Token>();
		boolean forif = false;
		boolean ctrl = false;
		for(int i=0;i<listaToken.size();i++)
		{
			if(lineaCodice.size()>1 && ctrl == false)
			{
				ctrl = true;
				if(lineaCodice.get(0).ritornaTipoToken().equals(Tok.FOR)||lineaCodice.get(0).ritornaTipoToken().equals(Tok.IF))
					forif = true;
				else if(lineaCodice.get(0).ritornaTipoToken().equals(Tok.INTERO))
					if(lineaCodice.get(1).ritornaTipoToken().equals(Tok.FOR)||lineaCodice.get(1).ritornaTipoToken().equals(Tok.IF))
						forif = true;
			}				
			Token t = listaToken.get(i);
			if(forif || (!t.ritornaTipoToken().equals(Tok.EOL) && !t.ritornaTipoToken().equals(Tok.DUEPUNTI)))
			{
				lineaCodice.add(t);
				if(t.ritornaTipoToken().equals(Tok.NEXT)||t.ritornaTipoToken().equals(Tok.ENDIF))
				{
					forif = false;
					ctrl = false;
					try
					{
						analizzaLinea(lineaCodice);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					lineaCodice.clear();
				}
			}
			else
			{
				if(!lineaCodice.get(0).ritornaTipoToken().equals(Tok.REM)&&lineaCodice.get(1).ritornaTipoToken().equals(Tok.REM))
					try
					{
						analizzaLinea(lineaCodice);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				lineaCodice.clear();	
			}
		}
	}
	
	public void analizzaLinea(ArrayList<Token> lineaCodice)throws Exception
	{
		Token primoToken;
		if(lineaCodice.get(0).ritornaTipoToken().equals(Tok.INTERO))
			primoToken = lineaCodice.get(1);
		else primoToken = lineaCodice.get(0);
		switch(primoToken.ritornaTipoToken())
		{
			case FOR:
				String var;
				int volte;
				int daNum;
				if(!lineaCodice.get(1).ritornaTipoToken().equals(Tok.VARIABILE))
					throw new SintassiException(Tok.VARIABILE);
				else
				{
					ValToken v =(ValToken)lineaCodice.get(1);
					var = (String)v.ritornaValore();
					if(!lineaCodice.get(2).ritornaTipoToken().equals(Tok.UGUALE))
						throw new SintassiException(Tok.UGUALE);
					if(!lineaCodice.get(3).ritornaTipoToken().equals(Tok.INTERO))
						throw new SintassiException(Tok.INTERO);
					else
					{
						v = (ValToken)lineaCodice.get(3);
						volte = (int)v.ritornaValore();
						if(!lineaCodice.get(4).ritornaTipoToken().equals(Tok.DO))
							throw new SintassiException(Tok.DO);
						if(!lineaCodice.get(lineaCodice.size()-1).equals(Tok.NEXT))
							throw new SintassiException(Tok.NEXT);
						else
						{
							ArrayList<Token> expr = new ArrayList<Token>();
							for(int j=5;j<lineaCodice.size()-1;j++)
								expr.add(lineaCodice.get(j));
							
						}
					}
				}
		}
	}
}
