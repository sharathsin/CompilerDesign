package lexical;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.ArrayList;

import state.LexState;
import state.LexState.FSMState;


public class LexicalClass {

	public ArrayList<Token> main(String args) {
		// TODO Auto-generated method stub
	
	ArrayList<Token>s=new ArrayList<Token>();
		FSM finite=new FSM();
		try{
			finite.f=new LexState();
		Variables.br=new PushbackReader(new FileReader(new File(System.getProperty("user.dir")+"/TestFiles/"+args)));
		finite.f.state=FSMState.Start;
		finite.start=true;
		int i;
		Token t=finite.nextToken();
		if(t!=null)
		 	
		s.add(t);int a= (- -1-3 * 10 / 5-1);
		System.out.println(a);
		while(Variables.ch!=-1)
		{
		//	Variables.ch=(char)i;
			 t=finite.nextToken();
if(t!=null)
	 
s.add(t);
			

			
		}
		if(s.size()==0)
			System.out.println("No Tokens");
		ArrayList<Integer> I=new ArrayList<Integer>();
		int y=0;
		for(Token t1:s)
		{
			
		
			if (t1 instanceof ErrorToken) {
				System.err.println("Lexcal Error: "+((ErrorToken)t1).msg+ t1.name+" at line number"+t1.location.line);
				I.add(y);
			
			}
			y++;
		}
		
		for(int j:I)
		{
			s.remove(j);
		}
		
		}
		catch(Exception e)
		{
		System.out.println(e+"ksad");
		}
		finally{
			try {
				Variables.br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return s;
		}
	
		
		}

}
