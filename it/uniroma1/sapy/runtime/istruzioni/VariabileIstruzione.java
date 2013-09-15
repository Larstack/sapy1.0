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
	public Intero getLabel(){ return etichetta; }

	@Override
	public Object esegui() throws Exception
	{
		VarRepository variabili = VarRepository.getInstance(); 
		if(lineaCodice.size()==1)
			if(lineaCodice.get(0).ritornaTipoToken().equals(Tok.STRINGA))
			{
				valoreVariabile = lineaCodice.get(0);
				variabili.setVariabile(nomeVariabile, valoreVariabile);
				return null;
			}
		for(int j=0;j<lineaCodice.size();j++)
			if(lineaCodice.get(j).ritornaTipoToken().equals(Tok.VARIABILE))
			{
				String nome = (String)lineaCodice.get(j).ritornaValore();
				Token valoreVariabile = variabili.getVariabile(nome);
				if(!valoreVariabile.equals(null))
					lineaCodice.set(j, valoreVariabile);
			}
		if(Confronto.isExprMatematica(lineaCodice))
		{
			ExprMatematica em = new ExprMatematica(lineaCodice);
			valoreVariabile = em.getRisultato();
		}
		else if(Condizione.isExprBooleana(lineaCodice))
		{
			ExprBooleana eb = new ExprBooleana(lineaCodice);
			valoreVariabile = eb.getRisultato();
		}
		else if(Parser.isConfronto(lineaCodice))
		{
			Condizione cond = new Condizione(lineaCodice);
			valoreVariabile = cond.getRisultato();
		}
		else throw new ParsingException();
		variabili.setVariabile(nomeVariabile, valoreVariabile);
		return null;
	}
}
