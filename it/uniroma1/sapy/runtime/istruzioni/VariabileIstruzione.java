package it.uniroma1.sapy.runtime.istruzioni;

import java.util.*;

import it.uniroma1.sapy.exception.*;
import it.uniroma1.sapy.lexer.token.*;
import it.uniroma1.sapy.parsing.Parser;
import it.uniroma1.sapy.runtime.VarRepository;

public class VariabileIstruzione implements Istruzione
{
	private String nomeVariabile;
	private Token valoreVariabile;
	private Intero etichetta;
	private ArrayList<Token> lineaCodice;
	
	public VariabileIstruzione(String nomeVariabile,ArrayList<Token> lineaCodice,Intero etichetta)
	{
		this.nomeVariabile = nomeVariabile;
		this.lineaCodice = lineaCodice;
		this.etichetta = etichetta;
	}
	
	public Token getValoreVariabile(){ return valoreVariabile; }
	
	public String getNomeVariabile(){ return nomeVariabile; }
	
	@Override
	public Intero getLabel()
	{
		if(etichetta==null) return null;
		return etichetta;
	}

	@Override
	public Object esegui() throws Exception
	{
		VarRepository variabili = VarRepository.getInstance();
		ArrayList<Token> lineaCodiceProvvisoria = (ArrayList<Token>)lineaCodice.clone();
		for(int j=0;j<lineaCodiceProvvisoria.size();j++)
			if(lineaCodiceProvvisoria.get(j).ritornaTipoToken().equals(Tok.VARIABILE))
			{
				String nome = (String)lineaCodiceProvvisoria.get(j).ritornaValore();
				Token valoreVariabile = variabili.getVariabile(nome);
				if(!valoreVariabile.equals(null))
					lineaCodiceProvvisoria.set(j, valoreVariabile);
			}
		if(lineaCodiceProvvisoria.size()==1)
			if(lineaCodiceProvvisoria.get(0).ritornaTipoToken().equals(Tok.STRINGA))
			{
				valoreVariabile = lineaCodiceProvvisoria.get(0);
				variabili.setVariabile(nomeVariabile, valoreVariabile);
				return null;
			}
		if(Condizione.isExprMatematica(lineaCodiceProvvisoria))
		{
			ExprMatematica em = new ExprMatematica(lineaCodiceProvvisoria);
			valoreVariabile = em.getRisultato();
		}
		else if(Condizione.isExprBooleana(lineaCodiceProvvisoria))
		{
			ExprBooleana eb = new ExprBooleana(lineaCodiceProvvisoria);
			valoreVariabile = eb.getRisultato();
		}
		else if(Condizione.isConfronto(lineaCodiceProvvisoria))
		{
			Condizione cond = new Condizione(lineaCodiceProvvisoria);
			valoreVariabile = cond.getRisultato();
		}
		else throw new ParsingException();
		variabili.setVariabile(nomeVariabile, valoreVariabile);
		return null;
	}
}
