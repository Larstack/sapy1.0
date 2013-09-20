package it.uniroma1.sapy.runtime;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import it.uniroma1.sapy.lexer.Lexer;
import it.uniroma1.sapy.parsing.Parser;

public class Interprete
{
	private String sorgente;
	
	public Interprete(String sorgente) throws Exception
	{
		this.sorgente = sorgente;
		Lexer lex = new Lexer(sorgente);
		lex.esaminaSorgente();
		Parser parser = new Parser(lex.getListaToken());
		ProgrammaEseguibile programma = parser.creaProgrammaEseguibile();
		programma.esegui();
	}
	public static void main(String[] args) throws Exception {
		File f = new File("/home/leonardo/Development/Programmazione(Navigli)/prg/prg6.sapy");
		Scanner s = new Scanner(f);
		String sorg = "";		
		while(s.hasNextLine()){sorg=sorg+s.nextLine()+"\n";}
		sorg = sorg.substring(0, sorg.length());
		Interprete interprete = new Interprete(sorg);
	}
}
