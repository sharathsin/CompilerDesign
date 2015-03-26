package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import lexical.Token;

public class FollowandFirst {
BufferedReader bf;
HashMap<String, ArrayList<Token>>s;
HashMap<String, ArrayList<Token>>f;
public void parse()
{
	s=new HashMap<String, ArrayList<Token>>();
	f=new HashMap<String, ArrayList<Token>>();
	try {
		bf=new BufferedReader(new FileReader(new File(System.getProperty("user.dir")+"\\ff.txt")));
    String nterm ;
    while((nterm=bf.readLine())!=null)
    {
    	String first=bf.readLine();
    	s.put(nterm, read(first));
    	String follow=bf.readLine();
    	f.put(nterm, read(follow));
    	
    }
	
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	
}
public ArrayList<Token> read(String s)
{
	ArrayList<Token> s1=new ArrayList<Token>();
	String s2[]=s.split("\\|");
	for(String g :s2)
	{
		Token t=new Token();
		String p= g.trim();
		t.value=new StringBuffer(p);
		s1.add(t);
	}
	return s1;
	
}



}
