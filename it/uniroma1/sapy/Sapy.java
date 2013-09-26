package it.uniroma1.sapy;

import java.io.*;
import java.util.*;
import it.uniroma1.sapy.runtime.Interprete;

/**
 * @author Leonardo Andres Ricciotti
 * Punto di partenza per l'interpretazione di un file sorgente Sapy
 */
public class Sapy
{
	/**
	 * Converte in String il file sorgente passato in input ed esegue l'interpretazione. 
	 * @param args - percorso del file sorgente Sapy.
	 * @throws Exception - se il file sorgente non viene trovato o se si verifica un errore durante l'interpretazione.
	 */
	public static void main(String[] args) throws Exception
	{	
		File f = new File(args[0]);
		Scanner s = new Scanner(f);
		String sorg = "";		
		while(s.hasNextLine()){sorg=sorg+s.nextLine()+"\n";}
		sorg = sorg.substring(0, sorg.length());
		new Interprete(sorg);
	}
}
