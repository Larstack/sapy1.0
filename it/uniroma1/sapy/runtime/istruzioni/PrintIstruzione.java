package it.uniroma1.sapy.runtime.istruzioni;

import java.util.*;

import it.uniroma1.sapy.exception.*;
import it.uniroma1.sapy.lexer.token.*;
import it.uniroma1.sapy.parsing.Parser;
import it.uniroma1.sapy.runtime.VarRepository;


public class PrintIstruzione implements Istruzione
{
	ArrayList<Token> daStampare;
	Intero etichetta;
	
	public PrintIstruzione(ArrayList<Token> daStampare, Intero etichetta)
	{
		this.daStampare = daStampare;
		this.etichetta = etichetta;
	}
	
	@Override
	public Object esegui() throws Exception
	{
		Object stampa;
		VarRepository variabili = VarRepository.getInstance();
		for(int j=0;j<daStampare.size();j++)
			if(daStampare.get(j).ritornaTipoToken().equals(Tok.VARIABILE))
			{
				String nome = (String)daStampare.get(j).ritornaValore();
				Token valoreVariabile = variabili.getVariabile(nome);
				if(!valoreVariabile.equals(null))
					daStampare.set(j, valoreVariabile);
			}
		if(daStampare.size()==1)
		{
			if(daStampare.get(0).ritornaTipoToken().equals(Tok.STRINGA))
			{
				stampa = daStampare.get(0).ritornaValore();
				System.out.println(stampa);
				return null;
			}
		}
		if(Confronto.isExprMatematica(daStampare))
		{
			ExprMatematica em = new ExprMatematica(daStampare);
			stampa = em.getRisultato();
		}
		else if(Condizione.isExprBooleana(daStampare))
		{
			ExprBooleana eb = new ExprBooleana(daStampare);
			stampa = eb.getRisultato();
		}
		else if(Parser.isConfronto(daStampare))
		{
			Condizione cond = new Condizione(daStampare);
			stampa = cond.getRisultato();
		}
		else throw new ParsingException();
		System.out.println(stampa);
		return null;
	}

	@Override
	public Intero getLabel(){ return etichetta; }
	
}
