package it.uniroma1.sapy.runtime;

import java.util.*;

import it.uniroma1.sapy.exception.*;
import it.uniroma1.sapy.lexer.token.Token;
import it.uniroma1.sapy.runtime.istruzioni.*;

/**
 * Programma eseguibile, visto come lista di istruzioni.
 * @author Leonardo Andres Ricciotti
 */
public class ProgrammaEseguibile
{
	/**
	 * Lista di istruzioni che costituisce il programma eseguibile.
	 */
	private ArrayList<Istruzione> programma;
	
	/**
	 * Costruttore
	 * @param programma - lista di istruzioni.
	 */
	public ProgrammaEseguibile(ArrayList<Istruzione> programma)
	{
		this.programma = programma;
	}
	
	/**
	 * Ritorna la lista di istruzioni.
	 * @return lista di istruzioni del programma.
	 */
	public ArrayList<Istruzione> getListaIstruzioni()
	{
		return programma;
	}
	
	/**
	 * Esegue le istruzioni una per una.
	 * @throws Exception - se si riscontra un errore durante l'esecuzione di un'istruzione.
	 */
	public void esegui() throws Exception
	{		
		for(int i=0;i<programma.size();i++)
		{
			Istruzione istr = programma.get(i);
			if(istr.getClass().equals(GotoIstruzione.class))
			{
				int label = (int)istr.esegui();
				boolean labeltrovata = false;
				for(int j=0;j<programma.size();j++)
				{
					if(programma.get(j).getLabel()!=null&&programma.get(j).getLabel().ritornaValore().equals(label))
					{
						i = j-1;
						labeltrovata = true;
						break;
					}
				}
				if(!labeltrovata) throw new EtichettaNonTrovataException(label);
				continue;
			}
			Object valRitorno = istr.esegui();
			if(valRitorno!=null)
			{
				boolean labeltrovata = false;
				for(int j=0;j<programma.size();j++)
				{
					if(programma.get(j).getLabel()!=null&&programma.get(j).getLabel().ritornaValore().equals(valRitorno))
					{
						i = j-1;
						labeltrovata = true;
						break;
					}
				}
				if(!labeltrovata) throw new EtichettaNonTrovataException((int)valRitorno);
			}
			
		}
	}
}
