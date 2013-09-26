package it.uniroma1.sapy.runtime.istruzioni;

import java.util.ArrayList;

import it.uniroma1.sapy.exception.*;
import it.uniroma1.sapy.lexer.token.*;
import it.uniroma1.sapy.runtime.*;
import it.uniroma1.sapy.runtime.espressioni.*;

/**
 * Istruzione FOR.<br />
 * Esegue un ciclo iterativo di un blocco di istruzioni.
 */
public class ForIstruzione implements Istruzione
{
	
	/**
	 * Espressione che, risolta, costituisce il valore di partenza della variabile su cui si itera.
	 */
	private ArrayList<Token> da;
	
	/**
	 * Espressione che, risolta, costituisce il valore finale della variabile contatore.
	 */
	private ArrayList<Token> finoA;
	
	/**
	 * Nome della variabile contatore.
	 */
	private String varItera;
	
	/**
	 * Blocco di istruzioni da iterare.
	 */
	private ArrayList<Istruzione> istruzioni;
	
	/**
	 * Etichetta che identifica l'istruzione, utilizzata dall'istruzione GOTO.
	 */
	private Intero etichetta;
	
	/**
	 * Booleano che segnala se la variabile contatore dev'essere incrementata o decrementata.
	 */
	private boolean isDecrescente = false;
	
	/**
	 * Costruttore
	 * @param da - valore di partenza della variabile contatore.
	 * @param finoA - valore finale della variabile contatore.
	 * @param varItera - nome della variabile contatore.
	 * @param istruzioni - blocco di istruzioni da iterare.
	 * @param etichetta - etichetta che identifica l'istruzione.
	 */
	public ForIstruzione(ArrayList<Token> da, ArrayList<Token> finoA, String varItera, ArrayList<Istruzione> istruzioni, Intero etichetta)
	{
		this.da = da;
		this.finoA = finoA;
		this.varItera = varItera;
		this.istruzioni = istruzioni;
		this.etichetta = etichetta;
	}
	
	@Override
	public Intero getLabel()
	{
		if(etichetta==null) return null;
		return etichetta;
	}

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
		variabili.setVariabile(varItera, new Intero(from));
		if(from>to) isDecrescente = true;
		int i=from;
		if(!isDecrescente)
		{
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
							if(istruzioni.get(k).getLabel()!=null&&istruzioni.get(k).getLabel().ritornaValore().equals(label))
							{
								j = k-1;
								labelTrovata = true;
								break;
							}
						}
						if(!labelTrovata)
							return label;
					}
					else
					{
						Object ritorno = istruzioni.get(j).esegui();
						if(ritorno!=null)
						{
							ritorno = (int)ritorno;
							boolean labelTrovata = false;
							for(int k=0;k<istruzioni.size();k++)
							{
								if(istruzioni.get(k).getLabel()!=null&&istruzioni.get(k).getLabel().ritornaValore().equals(ritorno))
								{
									j = k-1;
									labelTrovata = true;
									break;
								}
							}
							if(!labelTrovata)
								return (int)ritorno;
						}
					}
				}
				if((int)variabili.getVariabile(varItera).ritornaValore()>to) break;
				i = (int)variabili.getVariabile(varItera).ritornaValore();
				i++;
				variabili.setVariabile(varItera, new Intero(i));
			}
		}
		else
		{
			while(i>=to)
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
							if(istruzioni.get(k).getLabel()!=null&&istruzioni.get(k).getLabel().ritornaValore().equals(label))
							{
								j = k-1;
								labelTrovata = true;
								break;
							}
						}
						if(!labelTrovata)
							return label;
					}
					else
					{
						
						Object ritorno = istruzioni.get(j).esegui();
						if(ritorno!=null)
						{
							ritorno = (int)ritorno;
							boolean labelTrovata = false;
							for(int k=0;k<istruzioni.size();k++)
							{
								if(istruzioni.get(k).getLabel()!=null&&istruzioni.get(k).getLabel().ritornaValore().equals(ritorno))
								{
									j = k-1;
									labelTrovata = true;
									break;
								}
							}
							if(!labelTrovata)
								return (int)ritorno;
						}
					}
				}
				if((int)variabili.getVariabile(varItera).ritornaValore()<to) break;
				i = (int)variabili.getVariabile(varItera).ritornaValore();
				i--;
				variabili.setVariabile(varItera, new Intero(i));
			}
		}
		return null;
	}
}