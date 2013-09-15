package it.uniroma1.sapy.runtime;

import java.util.*;
import it.uniroma1.sapy.lexer.token.*;

public class VarRepository
{
	private HashMap<String,Token> variabili;
	static private VarRepository istanzaVar;
	
	private VarRepository(){ variabili = new HashMap<String,Token>(); }
	
	public static VarRepository getInstance()
	{
		if(istanzaVar == null) istanzaVar = new VarRepository();
		return istanzaVar;
	}
	
	public void setVariabile(String nome,Token valoreVar)
	{
		variabili.put(nome, valoreVar);
	}
	
	public Token getVariabile(String nome)
	{
		return variabili.get(nome);
	}
}
