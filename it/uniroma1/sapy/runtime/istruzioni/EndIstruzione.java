package it.uniroma1.sapy.runtime.istruzioni;

import it.uniroma1.sapy.lexer.token.*;

public class EndIstruzione implements Istruzione
{
	private Intero etichetta;
	
	public EndIstruzione(Intero etichetta){ this.etichetta = etichetta; }
	
	@Override
	public Intero getLabel()
	{
		if(etichetta==null) return null;
		return etichetta;
	}
	
	@Override
	public Object esegui()
	{
		System.exit(0);
		return null;
	}	
}
