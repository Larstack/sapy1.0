package it.uniroma1.sapy.lexer;

import java.io.*;
import java.util.*;
import it.uniroma1.sapy.lexer.token.*;

/**
 * Prendendo in input un sorgente Sapy, costruisce una lista di token
 * @author Leonardo Andres Ricciotti
 */
public class Lexer 
{
	private ArrayList<Token> listaToken;
	
	/**
	 * Costruttore<br />
	 * Inizializza lo scanner e chiama il metodo esaminaSorgente
	 * @param nomeFile - Percorso del file sorgente
	 * @exception FileNotFoundException - Viene lanciata se il file sorgente non viene trovato  
	 */
	public Lexer(String nomeFile)
	{
		listaToken = new ArrayList<Token>();
		if(nomeFile != null)	
		{
			File f = new File(nomeFile);
			
			try
			{
				Scanner s = new Scanner(f);
				esaminaSorgente(s);
			}
			catch(FileNotFoundException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Esegue l'analisi del sorgente linea per linea e costruisce la lista di Token
	 * @param s - Scanner passato dal costruttore dopo aver caricato il sorgente
	 */
	public void esaminaSorgente(Scanner s)
	{
		while(s.hasNextLine())
		{
			String linea = s.nextLine();
			String[] lineaSplit = linea.split(" ");
			for(int i=0;i<lineaSplit.length;i++)
			{
				String l = lineaSplit[i];
				if(l.equals("REM"))
				{
					listaToken.add(new Rem());
					for(int k=i+1;k<lineaSplit.length;k++)
						listaToken.add(new Funzione(lineaSplit[k]));
					i = lineaSplit.length;
				}
				else if(l.equals("\""))
				{
					String stringaToken = "";
					for(int j=i+1;j<lineaSplit.length;j++)
					{
						String l2 = lineaSplit[j];
						int indiceVirg = l2.indexOf("\"");
						if(indiceVirg == -1)
						{
							stringaToken = " "+l2;
							i++;
						}
						else
						{
							String finoVirgolette = l2.substring(0, indiceVirg);
							stringaToken = " " + finoVirgolette;
							String bloccoDaVirg = l2.substring(indiceVirg+1, l2.length());
							if(!bloccoDaVirg.equals(""))
								esaminaBlocco(bloccoDaVirg);
						}
					}
				}
				else esaminaBlocco(l);
			}
			listaToken.add(new Eol());
		}
	}
	

	
	/**
	 * 
	 * @param blocco - Parte di linea di codice da esaminare
	 */
	public void esaminaBlocco(String blocco)
	{
		String token = "";
		char c;
		boolean ctrl;
		for(int i=0;i<blocco.length();i++)
		{
			c = blocco.charAt(i);		
			switch(c)	
			{
				
		}
		if(token!="") individuaToken(token);
	}
	
	/**
	 * Individua il tipo di Token, partendo dalla stringa data in input 
	 * @param t - Stringa da analizzare
	 */
	public void individuaToken(String t)
	{
		//
		
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) 
	{
		Lexer lex = new Lexer("/home/leonardo/Scrivania/prova.sapy");
		
	}

}
