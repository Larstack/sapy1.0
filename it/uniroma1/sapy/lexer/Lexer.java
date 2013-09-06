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
	 * @param String - Percorso del file sorgente
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
	 * @param Scanner - Scanner passato dal costruttore dopo aver caricato il sorgente
	 */
	public void esaminaSorgente(Scanner s)
	{
		while(s.hasNextLine())
		{
			String linea = s.nextLine();
			if(linea.equals("")) continue;
			String[] lineaSplit = linea.split(" ");
			for(int i=0;i<lineaSplit.length;i++)
			{
				String l = lineaSplit[i];
				if(l.equals("REM"))
				{
					listaToken.add(new Rem());
				}
				else if(l.charAt(0)=='"')
				{
					String stringaToken = "";
					String l2 = l.substring(1);
					int indiceVirg = l2.indexOf('"');
					if(indiceVirg != -1)
					{
						stringaToken = l2.substring(0, l2.length());
						listaToken.add(new Stringa(stringaToken));
					}
					else
					{
						stringaToken = l2;
						for(int j=i+1;j<lineaSplit.length;j++)
						{
							l2 = lineaSplit[j];
							i++;
							indiceVirg = l2.indexOf('"');
							if(indiceVirg == -1)
							{
								stringaToken = stringaToken + " " + l2;
							}
							else
							{
								stringaToken = stringaToken + " " + l2.substring(0, indiceVirg);
								String stringaRim = l2.substring(indiceVirg+1);
								listaToken.add(new Stringa(stringaToken));
								if(!stringaRim.equals(""))
									esaminaBlocco(stringaRim);
								break;
							}
						}
					}
				}
				else esaminaBlocco(l);
			}
			listaToken.add(new Eol());
		}
	}
	
	/**
	 * Individua il tipo di Token, partendo dalla stringa data in input 
	 * @param String - Stringa da analizzare
	 */
	public void individuaToken(String t)
	{
		if(t.charAt(0) == '$')
		{
			listaToken.add(new Variabile(t));
		}
		else if(t.equals("TRUE"))
			listaToken.add(new Booleano(true));
		else if(t.equals("FALSE"))
			listaToken.add(new Booleano(false));
		else
		{
			try
			{
				Tok examTok = Tok.valueOf(t);
				Class<?> c = Class.forName("it.uniroma1.sapy.lexer.token."+examTok.getValoreToken());
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
	
	/**
	 * Esamina un blocco di una lina di codice, riconoscendone i token all'interno
	 * @param String - Parte di linea di codice da esaminare
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
				if(token!="")
				{
					individuaToken(token);
					token = "";
				}
				listaToken.add(new DuePunti());
				break;
			case '(':
				if(token!="")
				{
					individuaToken(token);
					token = "";
				}
				listaToken.add(new LeftPar());
				break;
			case ')':
				if(token!="")
				{
					individuaToken(token);
					token = "";
				}
				listaToken.add(new RightPar());
				break;
			case '=':
				if(token!="")
				{
					individuaToken(token);
					token = "";
				}
				listaToken.add(new Uguale());
				break;				
			case '<':
				char car = blocco.charAt(i+1);
				if(token!="")
				{
					individuaToken(token);
					token = "";
				}
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
				break;
			case '>':
				char car2 = blocco.charAt(i+1);
				if(token!="")
				{
					individuaToken(token);
					token = "";
				}
				if(car2 == '=')
				{
					listaToken.add(new MaggioreUguale());
					i++;
				}
				else
					listaToken.add(new Maggiore());
				break;
			case '+':
				if(token!="")
				{
					individuaToken(token);
					token = "";
				}
				listaToken.add(new Piu());
				break;
			case '-':
				if(token!="")
				{
					individuaToken(token);
					token = "";
				}
				listaToken.add(new Meno());
				break;	
			case '*':
				if(token!="")
				{
					individuaToken(token);
					token = "";
				}
				listaToken.add(new Per());
				break;
			case '/':
				if(token!="")
				{
					individuaToken(token);
					token = "";
				}
				listaToken.add(new Diviso());
				break;
			case '%':
				if(token!="")
				{
					individuaToken(token);
					token = "";
				}
				listaToken.add(new Modulo());
				break;
			default:
				token += Character.toString(c);
			}
		}
		if(token!="") individuaToken(token);
	}
	
	/**
	 * Restituisce la lista di Token del sorgente sapy
	 * @return ArrayList<Token> - ArrayList dei Token del sorgente
	 */
	public ArrayList<Token> getListaToken()
	{
		return listaToken;
	}
	
	/**
	 * Ritorna l'elenco dei token come stringa
	 * String - Elenco dei token del sorgente 
	 */
	@Override
	public String toString()
	{
		String s = "";
		for (int i=0;i<listaToken.size();i++)
		{
			Token t = listaToken.get(i);
			s = i == listaToken.size()-1 ? s + t.ritornaTipoToken() : s + t.ritornaTipoToken() + ",";
		}
		return s;
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) 
	{
		Lexer lex = new Lexer("/home/leonardo/Development/Programmazione(Navigli)/prg/prg3.sapy");
		System.out.print(lex.toString());
	}

}
