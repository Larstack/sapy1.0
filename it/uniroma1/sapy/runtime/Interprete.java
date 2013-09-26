package it.uniroma1.sapy.runtime;

import it.uniroma1.sapy.lexer.*;
import it.uniroma1.sapy.parsing.*;

/**
 * Interpretazione di un sorgente Sapy.
 * @author Leonardo Andres Ricciotti
 */
public class Interprete
{	
	/**
	 * Interpreta il sorgente Sapy.
	 * @param sorgente - sorgente Sapy.
	 */
	public Interprete(String sorgente)
	{
		Lexer lex = new Lexer(sorgente);
		lex.esaminaSorgente();
		Parser parser = new Parser(lex.getListaToken());
		ProgrammaEseguibile programma = parser.creaProgrammaEseguibile();
		try
		{
			programma.esegui();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
