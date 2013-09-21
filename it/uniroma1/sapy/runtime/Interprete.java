package it.uniroma1.sapy.runtime;

import it.uniroma1.sapy.lexer.*;
import it.uniroma1.sapy.parsing.*;

public class Interprete
{	
	public Interprete(String sorgente) throws Exception
	{
		Lexer lex = new Lexer(sorgente);
		lex.esaminaSorgente();
		Parser parser = new Parser(lex.getListaToken());
		ProgrammaEseguibile programma = parser.creaProgrammaEseguibile();
		programma.esegui();
	}
}
