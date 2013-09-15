package it.uniroma1.sapy.runtime.istruzioni;

import java.util.ArrayList;

import it.uniroma1.sapy.lexer.token.*;
import it.uniroma1.sapy.runtime.VarRepository;

public class ForIstruzione implements Istruzione
{
	
	private ArrayList<Token> da;
	private ArrayList<Token> finoA;
	private String varItera;
	private ArrayList<Istruzione> istruzioni;
	private Intero etichetta;
	
	public ForIstruzione(ArrayList<Token> da, ArrayList<Token> finoA, String varItera, ArrayList<Istruzione> istruzioni, Intero etichetta)
	{
		this.da = da;
		this.finoA = finoA;
		this.varItera = varItera;
		this.istruzioni = istruzioni;
		this.etichetta = etichetta;
	}
	
	@Override
	public Intero getLabel(){ return etichetta; }

	@Override
	public Integer esegui() throws Exception
	{
		VarRepository variabili = VarRepository.getInstance();
		for(int j=0;j<da.size();j++)
			if(da.get(j).ritornaTipoToken().equals(Tok.VARIABILE))
			{
				String nome = (String)da.get(j).ritornaValore();
				Token valoreVariabile = variabili.getVariabile(nome);
				if(!valoreVariabile.equals(null))
					da.set(j, valoreVariabile);
			}
		ExprMatematica esprMat = new ExprMatematica(da);
		int from = (int)esprMat.getRisultato().ritornaValore();
		variabili.setVariabile(varItera, new Intero(from));
		
		for(int j=0;j<finoA.size();j++)
			if(finoA.get(j).ritornaTipoToken().equals(Tok.VARIABILE))
			{
				String nome = (String)finoA.get(j).ritornaValore();
				Token valoreVariabile = variabili.getVariabile(nome);
				if(!valoreVariabile.equals(null))
					finoA.set(j, valoreVariabile);
			}
		esprMat = new ExprMatematica(finoA);
		int to = (int)esprMat.getRisultato().ritornaValore();		

		int i = from;
		variabili.setVariabile(varItera, new Intero(i));
		while(i<=to)
		{
			for(int j=0;j<istruzioni.size();j++)
			{
				if(istruzioni.get(j).getClass().equals(GotoIstruzione.class))
				{
					GotoIstruzione istrGoto = (GotoIstruzione)istruzioni.get(j);
					int label = istrGoto.esegui();
					boolean labelTrovata = false;
					for(int k=0;k<istruzioni.size();k++)
					{
						if(istruzioni.get(k).getLabel().ritornaValore().equals(label))
						{
							j = k;
							labelTrovata = true;
							break;
						}
					}
					if(!labelTrovata)
						return label;
				}
				else
				{
					Object ritorno = istruzioni.get(i).esegui();
					if(!(ritorno==null))
					{
						ritorno = (int)ritorno;
						boolean labelTrovata = false;
						for(int k=0;k<istruzioni.size();k++)
						{
							if(istruzioni.get(k).getLabel().ritornaValore().equals(ritorno))
							{
								j = k;
								labelTrovata = true;
								break;
							}
						}
						if(!labelTrovata)
							return (int)ritorno;
					}
				}
			}
			i++;
			variabili.setVariabile(varItera, new Intero(i));
		}
		return null;
	}
}