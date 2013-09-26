package it.uniroma1.sapy.lexer;

import java.io.*;
import java.lang.reflect.Constructor;
import java.util.*;
import it.uniroma1.sapy.lexer.token.*;
/**
 * Costruisce una lista di token a partire da un sorgente Sapy.
 * @author Leonardo Andres Ricciotti
 */
public class Lexer 
{
	/**
	 * lista di Token costruita dal Lexer.
	 */
	private ArrayList<Token> listaToken;
	
	/**
	 * sorgente Sapy passato in input. 
	 */
	private String sorgente;
	
	/**
	 * Costruttore
	 * @param sorgente - sorgente Sapy da analizzare.  
	 */
	public Lexer(String sorgente)
	{
		listaToken = new ArrayList<Token>();
		this.sorgente = sorgente;
	}
	
	/**
	 * Esegue l'analisi del sorgente linea per linea e costruisce la lista di Token.
	 */
	public void esaminaSorgente()
	{
		String[] sorgenteSplit = sorgente.split("\n");
		int nLinea = 0;
		while(nLinea<sorgenteSplit.length)
		{
			String linea = sorgenteSplit[nLinea];
			if(linea.equals(""))
			{
				nLinea++;
				continue;
			}
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
						stringaToken = l2.substring(0, indiceVirg);
						listaToken.add(new Stringa(stringaToken));
						if(l2.length()-stringaToken.length()>1)
							esaminaBlocco(l2.substring(indiceVirg+1, l2.length()));
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
			nLinea++;
		}
	}
	
	/**
	 * Individua il tipo di Token, partendo dalla stringa data in input. 
	 * @param t - String da analizzare.
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
	 * Esamina un blocco(senza spazi) di una linea di codice, riconoscendone i token all'interno.
	 * @param blocco - parte di linea di codice senza spazi da esaminare.
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
	 * Ritorna la lista di Token costruita dal Lexer.
	 * @return lista di Token.
	 */
	public ArrayList<Token> getListaToken()
	{
		return listaToken;
	}
	
	/**
	 * Ritorna l'elenco dei Token sotto forma di String.
	 * @return elenco dei Token. 
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
	 * Stampa a video la lista dei Token costruita dal Lexer.
	 * @param args - percorso del file sorgente Sapy.
	 * @throws FileNotFoundException - se il percorso del file dato in input non viene trovato.
	 */
	public static void main(String[] args) throws FileNotFoundException
	{
		File f = new File(args[0]);
		Scanner s = new Scanner(f);
		String sorg = "";		
		while(s.hasNextLine()){sorg=sorg+s.nextLine()+"\n";}
		sorg = sorg.substring(0, sorg.length());
		Lexer lex = new Lexer(sorg);
		lex.esaminaSorgente();
		System.out.print(lex.toString());
	}

}
