package it.uniroma1.sapy.runtime.istruzioni;

import it.uniroma1.sapy.lexer.token.*;
/**
 * Istruzione che viene eseguita dall'interprete 
 * @author Leonardo Andres Ricciotti
 */
public interface Istruzione
{
	
	public Intero getLabel();
	
	public Object esegui() throws Exception;
}
