package it.uniroma1.sapy.runtime.istruzioni;

import java.util.ArrayList;
import it.uniroma1.sapy.lexer.token.*;
import it.uniroma1.sapy.runtime.VarRepository;

public class IfIstruzione implements Istruzione
{
	private ArrayList<Istruzione> istruzioni;
	private boolean condizione;
	private Intero etichetta;
	private ArrayList<Token> lineaCodice;
	
	public IfIstruzione(ArrayList<Token> lineaCodice, ArrayList<Istruzione> istruzioni,Intero etichetta)
	{
		this.lineaCodice = lineaCodice;
		this.istruzioni = istruzioni;
		this.etichetta = etichetta;
	}

	@Override
	public Intero getLabel()
	{
		return etichetta;
	}

	@Override
	public Integer esegui() throws Exception
	{
		VarRepository variabili = VarRepository.getInstance();
		for(int j=0;j<lineaCodice.size();j++)
			if(lineaCodice.get(j).ritornaTipoToken().equals(Tok.VARIABILE))
			{
				String nome = (String)lineaCodice.get(j).ritornaValore();
				Token valoreVariabile = variabili.getVariabile(nome);
				if(!valoreVariabile.equals(null))
					lineaCodice.set(j, valoreVariabile);
			}
		Condizione cond = new Condizione(lineaCodice);
		condizione = (boolean)cond.getRisultato().ritornaValore();
		
		if(condizione)
		{
			for(int i=0;i<istruzioni.size();i++)
			{
				if(istruzioni.get(i).getClass().equals(GotoIstruzione.class))
				{
					GotoIstruzione istrGoto = (GotoIstruzione)istruzioni.get(i);
					int label = istrGoto.esegui();
					boolean labelTrovata = false;
					for(int k=0;k<istruzioni.size();k++)
					{
						if(istruzioni.get(k).getLabel().ritornaValore().equals(label))
						{
							i = k;
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
								i = k;
								labelTrovata = true;
								break;
							}
						}
						if(!labelTrovata)
							return (int)ritorno;
					}
				}
			}
			return null;
		}
		else return null;
	}
}