package it.uniroma1.sapy.runtime;

import it.uniroma1.sapy.lexer.Lexer;
import it.uniroma1.sapy.parsing.Parser;

public class Interprete
{
	public static void main(String[] args) throws Exception
	{		
		Lexer lex = new Lexer(args[0]);
		Parser parser = new Parser(lex.getListaToken());
		ProgrammaEseguibile programma = new ProgrammaEseguibile(parser.getListaIstruzioni());
		programma.esegui();
		
	}
	
	
}
