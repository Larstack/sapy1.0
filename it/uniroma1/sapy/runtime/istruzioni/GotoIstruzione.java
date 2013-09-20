package it.uniroma1.sapy.runtime.istruzioni;

import it.uniroma1.sapy.lexer.token.Intero;

public class GotoIstruzione implements Istruzione
{
	private Intero label;
	private Intero etichetta;
	
	public GotoIstruzione(Intero label,Intero etichetta)
	{
		this.label = label;
		this.etichetta = etichetta;
	}
	
	@Override
	public Intero getLabel()
	{
		if(etichetta==null) return null;
		return etichetta;
	}
	
	public Intero getEtichetta(){ return label; }

	@Override
	public Integer esegui(){ return (int)label.ritornaValore();}

}
