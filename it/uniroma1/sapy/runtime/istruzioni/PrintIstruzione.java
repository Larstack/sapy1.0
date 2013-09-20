package it.uniroma1.sapy.runtime.istruzioni;

import java.util.*;
import it.uniroma1.sapy.exception.*;
import it.uniroma1.sapy.lexer.token.*;
import it.uniroma1.sapy.parsing.Parser;
import it.uniroma1.sapy.runtime.VarRepository;


public class PrintIstruzione implements Istruzione
{
	private ArrayList<Token> daStampare;
	private Intero etichetta;
	
	public PrintIstruzione(ArrayList<Token> daStampare, Intero etichetta)
	{
		this.daStampare = daStampare;
		this.etichetta = etichetta;
	}
	
	@Override
	public Object esegui() throws Exception
	{
		Token stampa;
		VarRepository variabili = VarRepository.getInstance();
		ArrayList<Token> daStampareProvvisoria = (ArrayList<Token>)daStampare.clone();
		for(int j=0;j<daStampareProvvisoria.size();j++)
			if(daStampareProvvisoria.get(j).ritornaTipoToken().equals(Tok.VARIABILE))
			{
				String nome = (String)daStampareProvvisoria.get(j).ritornaValore();
				Token valoreVariabile = variabili.getVariabile(nome);
				if(!valoreVariabile.equals(null))
					daStampareProvvisoria.set(j, valoreVariabile);
			}
		if(daStampareProvvisoria.size()==1)
		{
			if(daStampareProvvisoria.get(0).ritornaTipoToken().equals(Tok.STRINGA))
			{
				stampa = daStampareProvvisoria.get(0);
				System.out.println(stampa.ritornaValore());
				return null;
			}
		}
		if(Condizione.isExprMatematica(daStampareProvvisoria))
		{
			ExprMatematica em = new ExprMatematica(daStampareProvvisoria);
			stampa = em.getRisultato();
		}
		else if(Condizione.isExprBooleana(daStampareProvvisoria))
		{
			ExprBooleana eb = new ExprBooleana(daStampareProvvisoria);
			stampa = eb.getRisultato();
		}
		else if(Condizione.isConfronto(daStampareProvvisoria))
		{
			Condizione cond = new Condizione(daStampareProvvisoria);
			stampa = cond.getRisultato();
		}
		else throw new ParsingException();
		System.out.println(stampa.ritornaValore());
		return null;
	}

	@Override
	public Intero getLabel()
	{
		if(etichetta==null) return null;
		return etichetta;
	}
}
