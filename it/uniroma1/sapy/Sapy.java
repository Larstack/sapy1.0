package it.uniroma1.sapy;

import java.io.*;
import java.util.*;
import it.uniroma1.sapy.runtime.Interprete;

public class Sapy
{
	public static void main(String[] args) throws Exception
	{	
		File f = new File(args[0]);
		Scanner s = new Scanner(f);
		String sorg = "";		
		while(s.hasNextLine()){sorg=sorg+s.nextLine()+"\n";}
		sorg = sorg.substring(0, sorg.length());
		s.close();
		new Interprete(sorg);
	}
}
