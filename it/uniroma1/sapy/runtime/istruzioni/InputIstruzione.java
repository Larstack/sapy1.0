package it.uniroma1.sapy.runtime.istruzioni;

import java.util.*;
import it.uniroma1.sapy.lexer.token.*;
import it.uniroma1.sapy.runtime.VarRepository;

public class InputIstruzione implements Istruzione
{
	private Intero etichetta;
	private String varStringa;
	private String valoreVariabile;
	
	public InputIstruzione(String varStringa, Intero etichetta)
	{
		this.varStringa = varStringa;
		this.etichetta = etichetta;
	}

	@Override
	public Intero getLabel() { return etichetta; }
	
	@Override
	public Object esegui()
	{
		Scanner s = new Scanner(System.in);
		String input = s.next();
		valoreVariabile = input;
		VarRepository variabili = VarRepository.getInstance();
		variabili.setVariabile(varStringa, new Stringa(valoreVariabile));
		s.close();
		return null;
	}
	
	public String getStringaInput(){ return valoreVariabile; }
	public String getNomeVariabile(){ return varStringa; }
}