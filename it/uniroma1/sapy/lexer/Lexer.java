package it.uniroma1.sapy.lexer;

import java.io.*;
import java.lang.reflect.Constructor;
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
							listaToken.add(new Stringa(stringaToken));
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
	 * Esamina un blocco di una lina di codice, riconoscendone i token all'interno
	 * @param blocco - Parte di linea di codice da esaminare
	 */
	public void esaminaBlocco(String blocco)
	{
		String token = "";
		char c;
		boolean ctrl = false;
		for(int i=0;i<blocco.length();i++)
		{
			c = blocco.charAt(i);
			if(c != '"' && ctrl)
			{
				token+=Character.toString(c);
				continue;
			}
			switch(c)
			{
			case '"':
				if(ctrl == true)
				{
					ctrl = false;
					listaToken.add(new Stringa(token));
					token = "";
				}
				else
					ctrl = true;
				break;			
			case ':':
				listaToken.add(new DuePunti());
				if(token!="")
				{
					individuaToken(token);
					token = "";
				}
				break;
			case '(':
				listaToken.add(new LeftPar());
				if(token!="")
				{
					individuaToken(token);
					token = "";
				}
				break;
			case ')':
				listaToken.add(new RightPar());
				if(token!="")
				{
					individuaToken(token);
					token = "";
				}
				break;
			case '=':
				listaToken.add(new Uguale());
				if(token!="")
				{
					individuaToken(token);
					token = "";
				}
				break;				
			case '<':
				char car = blocco.charAt(i+1);
				if(car == '>')
				{
					listaToken.add(new Diverso());
					i++;
				}
				else if(car == '=')
				{
					listaToken.add(new MinoreUguale());
					i++;
				}
				else
					listaToken.add(new Minore());
				if(token!="")
				{
					individuaToken(token);
					token = "";
				}
				break;
			case '>':
				char car2 = blocco.charAt(i+1);
				if(car2 == '=')
				{
					listaToken.add(new MaggioreUguale());
					i++;
				}
				else
					listaToken.add(new Maggiore());
				if(token!="")
				{
					individuaToken(token);
					token = "";
				}
				break;
			case '+':
				listaToken.add(new Piu());
				if(token!="")
				{
					individuaToken(token);
					token = "";
				}
				break;
			case '-':
				listaToken.add(new Meno());
				if(token!="")
				{
					individuaToken(token);
					token = "";
				}
				break;	
			case '*':
				listaToken.add(new Per());
				if(token!="")
				{
					individuaToken(token);
					token = "";
				}
				break;
			case '/':
				listaToken.add(new Diviso());
				if(token!="")
				{
					individuaToken(token);
					token = "";
				}
				break;
			case '%':
				listaToken.add(new Modulo());
				if(token!="")
				{
					individuaToken(token);
					token = "";
				}
				break;
			default:
				token += Character.toString(c);
			}
		}
		if(token!="") individuaToken(token);
	}
	
	/**
	 * Individua il tipo di Token, partendo dalla stringa data in input 
	 * @param t - Stringa da analizzare
	 */
	public void individuaToken(String t)
	{
		if(t.equals("TRUE"))
			listaToken.add(new Booleano(true));
		else if(t.equals("FALSE"))
			listaToken.add(new Booleano(false));
		else
		{
			try	
			{
				Tok examTok = Tok.valueOf(t);	
				Class<?> c = Class.forName(examTok.getValoreToken());
				Class<? extends Token> TipoToken = c.asSubclass(Token.class);
				Constructor<? extends Token> constr1 = TipoToken.getConstructor();
				Token NuovoToken = constr1.newInstance();
				listaToken.add(NuovoToken);
			}
			catch(IllegalArgumentException e)
			{
				boolean isIntero = false;
				for(int i=0;i<t.length();i++)
				{
					char c = t.charAt(i);
					if(c >= 48 && c<= 57)
						isIntero = true;
					else
					{
						isIntero= false;
						listaToken.add(new Funzione(t));
						break;
					}
				}
				if(isIntero)
					listaToken.add(new Intero(Integer.parseInt(t)));
			}
			catch(ClassNotFoundException e)
			{
				e.printStackTrace();
			}
			catch(ClassCastException e)
			{
				e.printStackTrace();
			}
			catch(NoSuchMethodException e)
			{
				e.printStackTrace();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
// Gestire Variabili

	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) 
	{
		Lexer lex = new Lexer("/home/leonardo/Development/Programmazione(Navigli)/prg/prg1.sapy");
		
	}

}
