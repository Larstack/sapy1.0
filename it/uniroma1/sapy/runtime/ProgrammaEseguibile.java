package it.uniroma1.sapy.runtime;

import java.util.*;

import it.uniroma1.sapy.exception.*;
import it.uniroma1.sapy.runtime.istruzioni.*;

public class ProgrammaEseguibile
{
	private ArrayList<Istruzione> programma;
	
	public ProgrammaEseguibile(ArrayList<Istruzione> programma)
	{
		this.programma = programma;
	}
	
	public ArrayList<Istruzione> getListaIstruzioni()
	{
		return programma;
	}
	
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
					if(programma.get(j).getLabel().ritornaValore().equals(label))
					{
						i = j;
						labeltrovata = true;
						break;
					}
				}
				if(!labeltrovata) throw new EtichettaNonTrovataException(label);
			}
			Object valRitorno = istr.esegui();
			if(valRitorno!=null)
			{
				boolean labeltrovata = false;
				for(int j=0;j<programma.size();j++)
				{
					if(programma.get(j).getLabel().ritornaValore().equals(valRitorno))
					{
						i = j;
						labeltrovata = true;
						break;
					}
				}
				if(!labeltrovata) throw new EtichettaNonTrovataException((int)valRitorno);
			}
			
		}
	}
}
