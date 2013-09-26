package it.uniroma1.sapy.runtime.istruzioni;

import java.util.ArrayList;

import it.uniroma1.sapy.exception.*;
import it.uniroma1.sapy.lexer.token.*;
import it.uniroma1.sapy.runtime.*;
import it.uniroma1.sapy.runtime.espressioni.*;

/**
 * Istruzione IF.<br />
 * Se la condizione è true, esegue il blocco istruzioni.
 */
public class IfIstruzione implements Istruzione
{
	/**
	 * Blocco di istruzioni che viene eseguito se la condizione è true.
	 */
	private ArrayList<Istruzione> istruzioni;
	
	/**
	 * Segnala se la condizione è true o false.
	 */
	private boolean condizione;
	
	/**
	 * Etichetta che identifica l'istruzione, utilizzata dall'istruzione GOTO.
	 */
	private Intero etichetta;
	
	/**
	 * Condizione da verificare.
	 */
	private ArrayList<Token> lineaCodice;
	
	/**
	 * Costruttore
	 * @param lineaCodice - condizione da verificare.
	 * @param istruzioni - blocco di istruzioni da eseguire.
	 * @param etichetta - etichetta che identifica l'istruzione.
	 */
	public IfIstruzione(ArrayList<Token> lineaCodice, ArrayList<Istruzione> istruzioni,Intero etichetta)
	{
		this.lineaCodice = lineaCodice;
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
						if(istruzioni.get(k).getLabel()!=null&&istruzioni.get(k).getLabel().ritornaValore().equals(label))
						{
							i = k-1;
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
					if(ritorno!=null)
					{
						ritorno = (int)ritorno;
						boolean labelTrovata = false;
						for(int k=0;k<istruzioni.size();k++)
						{
							if(istruzioni.get(k).getLabel()!=null&&istruzioni.get(k).getLabel().ritornaValore().equals(ritorno))
							{
								i = k-1;
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