package lexical;
/*
 * 
 * @Author:Sharath Jillela
 * 
 * 
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

import state.LexState;
import state.LexState.FSMState;

public class FSM {
	HashSet<LexState> scannerState;
	HashSet<String> aLpCharacters, key;
	boolean start;
	LexState f;

	FSM() {
		String line;
		try {
			BufferedReader bf = new BufferedReader(new FileReader(new File(
					System.getProperty("user.dir") + "\\Reserved")));
			aLpCharacters = new HashSet<String>();
			key = new HashSet<String>();
			while ((line = bf.readLine()) != null) {
				aLpCharacters.add(line.trim());
			}
			bf = new BufferedReader(new FileReader(new File(
					System.getProperty("user.dir") + "\\keywords")));
			while ((line = bf.readLine()) != null) {
				key.add(line.trim());
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}

	public Token processOperator() {
		if ((char) Variables.ch == '*') {
			Token t = new Token();
			t.type = "muloperator";
			t.name = new StringBuffer("*");
			t.value=new StringBuffer(t.name);
			return t;
		}
		else if ((char) Variables.ch == ';') {
			Token t = new Token();
			t.type = "semiop";
			t.name = new StringBuffer(";");
			t.value=new StringBuffer(t.name);
			return t;
		} 
		else if ((char) Variables.ch == '.') {
			Token t = new Token();
			t.type = "dotop";
			t.name = new StringBuffer(".");	t.value=new StringBuffer(t.name);
			return t;
		}
		else if ((char) Variables.ch == ',') {
			Token t = new Token();
			t.type = "commaop";
			t.name = new StringBuffer(",");	t.value=new StringBuffer(t.name);
			return t;
		} 
		else if ((char) Variables.ch == '+') {
			Token t = new Token();
			t.type = "addoperator";
			t.name = new StringBuffer("+");	t.value=new StringBuffer(t.name);
			return t;
		} else if ((char) Variables.ch == '/') {
			Token t = new Token();
			if ((Variables.ch = nextChar()) == '*') {

				return processComment();

			} else if (Variables.ch == '/') {

				return processLineComment();

			} else if(!Character.isWhitespace(Variables.ch)) {
				backup();
				t.type = "division";
				t.name = new StringBuffer("/");	t.value=new StringBuffer(t.name);
			}
			else
			{
				t.type = "division";
				t.name = new StringBuffer("/");	t.value=new StringBuffer(t.name);

				
			}

			return t;
		} else if ((char) Variables.ch == '-') {
			Token t = new Token();
			t.type = "suboperator";
			t.name = new StringBuffer("-");	t.value=new StringBuffer(t.name);
			return t;
		} else if ((char) Variables.ch == '=') {
			Token t = new Token();
			Variables.ch = nextChar();	
			if (Variables.ch == '=') {
				t.type = "isequaltoop";
				t.name = new StringBuffer("==");t.value=new StringBuffer(t.name);

			} else if (!Character.isWhitespace(Variables.ch)) {
				backup();
				t.type = "equalop";
				t.name = new StringBuffer("=");t.value=new StringBuffer(t.name);
			} else {
				t.type = "equalop";
				t.name = new StringBuffer("=");t.value=new StringBuffer(t.name);

			}

			return t;
		} else if ((char) Variables.ch == '>') {
			Token t = new Token();
			if ((Variables.ch = nextChar()) == '=') {
				t.type = "GTE";
				t.name = new StringBuffer(">=");t.value=new StringBuffer(t.name);

			} else if (!Character.isWhitespace(Variables.ch)) {
				backup();
				t.type = "GT";
				t.name = new StringBuffer(">");t.value=new StringBuffer(t.name);
			} else {
				t.type = "GT";
				t.name = new StringBuffer(">");t.value=new StringBuffer(t.name);
			}

			return t;
		} else if ((char) Variables.ch == '<') {
			Token t = new Token();
			if ((Variables.ch = nextChar()) == '=') {
				t.type = "LTE";
				t.name = new StringBuffer("<=");t.value=new StringBuffer(t.name);

			} else if (Variables.ch == '>') {
				t.type = "NotEqual";
				t.name = new StringBuffer("<>");t.value=new StringBuffer(t.name);

			} else if (!Character.isWhitespace(Variables.ch)) {
				backup();
				t.type = "LT";
				t.name = new StringBuffer("<");t.value=new StringBuffer(t.name);
			} else {
				t.type = "LT";
				t.name = new StringBuffer("<");t.value=new StringBuffer(t.name);

			}

			return t;
		} else if ((char) Variables.ch == '{') {
			Token t = new Token();
			t.type = "opencurlpar";
			t.name = new StringBuffer("{");t.value=new StringBuffer(t.name);
			return t;
		}
		 else if ((char) Variables.ch == '}') {
				Token t = new Token();
				t.type = "closecurlpar";
				t.name = new StringBuffer("}");t.value=new StringBuffer(t.name);
				return t;
			}else if ((char) Variables.ch == '[') {
			Token t = new Token();
			t.type = "arrayop";
			t.name = new StringBuffer("[");t.value=new StringBuffer(t.name);
			return t;
		} else if ((char) Variables.ch == ']') {
			Token t = new Token();
			t.type = "closearrayop";
			t.name = new StringBuffer("]");t.value=new StringBuffer(t.name);
			return t;
		}

		else if ((char) Variables.ch == '(') {
			Token t = new Token();
			t.type = "oppar";
			t.name = new StringBuffer("(");t.value=new StringBuffer(t.name);
			return t;
		} else if ((char) Variables.ch == ')') {
			Token t = new Token();
			t.type = "cloasepar";
			t.name = new StringBuffer(")");t.value=new StringBuffer(t.name);
			return t;
		}

		return null;

	}

	public void backup() {
		try {
			Variables.br.unread((char) Variables.ch);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
 public boolean isNonzeroDigit(char ch)
 {
if(Character.isDigit(ch))
{
	if(ch=='0')
		return false;
	return true;
	
}
else
{
	return false;
}
	 
	 
	 
	 
	 
	 
 }
	public Token nextToken() {
		Token T = null;
		try {
			try {

				Variables.ch = Variables.br.read();
				if(Variables.ch=='\n')
				{
					Variables.line_number++;
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (isAlphabet((char) Variables.ch) && f.state == FSMState.Start) {
				T = new Token();
				T.type = "id";
				T.value=new StringBuffer(T.type);
				T.location = new Location();
				T.name = processId();
				f.state = FSMState.Start;
				if (key.contains(T.name.toString())) {
					T.type =T.name.toString()+ "keyword";
					T.value=T.name;
				}

			} 
			else if(isNonzeroDigit((char)Variables.ch)&& f.state==FSMState.Start)
			{
				T= processNumber();
			}
			else if(Variables.ch=='0'){
				T=new Token();
				T.type="numint";
				T.value=new StringBuffer(T.type);
				T.name=new StringBuffer("0");
				
				
			}
			else if (isNotAlphabet((char) Variables.ch)
					&& f.state == FSMState.Start) {
				T= processOperator();

			}
			else{
				if(Variables.ch>32&& Variables.ch<256){
				T=new ErrorToken("Invalid symbol ");
				T.name=new StringBuffer();
				T.value=new StringBuffer("error");
				T.name.append((char)Variables.ch);
				T.location=new Location();
				}
				
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e + "here");
			;
		}
	
		if(T!=null)
		{
			T.location=new Location();
			T.location.line=Variables.line_number;
		}
		return T;
	}


	public boolean isWhitespace(int ch)
	{
		return ch>33;
	}
	public boolean isAlphabet(char ch) {
		return Character.isLetter(ch);
	}

	public boolean isNumber(char ch) {
		return Character.isDigit(ch);
	}

	public StringBuffer processId() {
		StringBuffer s = new StringBuffer();
		if (isAlphabet((char) Variables.ch) && f.state == FSMState.Start) {
			s.append((char) Variables.ch);

			f.state = FSMState.id1;

		}
		do {
			Variables.ch = nextChar();
			if (!isAlphabet((char) Variables.ch)
					&& !isNumber((char) Variables.ch) && Variables.ch != '_') {
				if (!isNotAlphabet((char) Variables.ch)) {
					f.state = FSMState.error;
					backup();
				} else {
					f.state = FSMState.Finish;
					backup();
				}
				break;
			} else
				s.append((char) Variables.ch);
		} while (f.state != FSMState.Finish || f.state != FSMState.error);
		return s;
	}

	private boolean isNotAlphabet(char ch) {
		// TODO Auto-generated method stub

		return aLpCharacters.contains(String.valueOf(ch));
	}

	public Token processComment() {
		Variables.ch=nextChar();
		char ch1=nextChar();
		while(true)
		{
			if(Variables.ch>255||Variables.ch<0)
			{
				Token t=new ErrorToken("comment not ended");
				t.type="comment";
				t.value=new StringBuffer("error");
				t.name=new StringBuffer("comment");
				return t;
			}
		if(Variables.ch=='*'&& ch1=='/')
		{
		
			
			return null;
		}
		else
		{
		Variables.ch=(int)ch1;
		ch1=nextChar();
			
		}
			
			
		}
		
	
		
		
			
	}

	

	public Token processLineComment() {
		while((Variables.ch=nextChar())!='\n'&& Variables.ch!=-1&&Variables.ch<255&&Variables.ch>0);

		return null;
	}

	public Token processNumber() {
		FSMState f1;
		f1=FSMState.Start;
		Token t=new Token();
		t.type="numint";
		t.value=new StringBuffer(t.type);
		t.name=new StringBuffer();
		t.name.append((char)Variables.ch);
		String error = null;
		do{
			Variables.ch=nextChar();
			if(Variables.ch=='.'&& (f1== FSMState.Start||f1==FSMState.num||f1==FSMState.Floatnum))
			{
				t.name.append((char)Variables.ch);
				f1=FSMState.Float;
				
			}
			else if(Character.isDigit(Variables.ch)&& f1==FSMState.Start)
			{
				t.name.append((char)Variables.ch);
				f1=FSMState.num;
			}
			else if(Character.isDigit(Variables.ch)&&f1==FSMState.Float)
			{
				f1=FSMState.Floatnum;
				t.name.append((char)Variables.ch);
			}
				
			else if(Character.isDigit(Variables.ch)&&f1==FSMState.num)
			{
				t.name.append((char)Variables.ch);
			}
			else if(Character.isDigit(Variables.ch)&&f1==FSMState.Floatnum)
			{
				t.name.append((char)Variables.ch);
			}
			else if(Variables.ch=='.'&&f1==FSMState.num)
			{
				f1=FSMState.Float;
				t.name.append('0');
			}
			else if(Variables.ch=='.'&&(f1==FSMState.Float||f1==FSMState.float1))
			{
				f1=FSMState.error;
				error="Invalid decimal";
				backup();
			}
			else if(!(Character.isDigit(Variables.ch)))
			{
				if(f1== FSMState.float1||f1==FSMState.Float)
				{
					f1=FSMState.error;
					error="Invalid Float";
					backup();
				}
				else if(f1==FSMState.num||f1==FSMState.Floatnum)
				{
					f1=FSMState.Finish;
					backup();
				}
				else{
					f1=FSMState.Finish;
					backup();
				}
				
			}
			
		}
		while(f1!=FSMState.error&&f1!=FSMState.Finish);
		if(t.name.toString().contains(".")&&t.name.toString().charAt(t.name.length()-1)=='0'&&t.name.toString().charAt(t.name.length()-2)!='.')
		{
			f1=FSMState.error;
			error="Invalid decimal";
		}
		Token h;
		if(f1==FSMState.error)
		{
		 h=new ErrorToken(error);
		 h.name=t.name;
		 h.value=new StringBuffer("error");
		 h.type=t.type;
		 t=h;
		
		}
		else if(f1==FSMState.Finish)
		{
			
		if(t.name.toString().contains("."))
		{
			t.type="numfloat";	t.value=new StringBuffer(t.type);
		}
			
		}
		return t;
	}

	public char nextChar() {
		try {
			char ch=(char) Variables.br.read();
			if(ch=='\n')
				Variables.line_number++;
			return ch;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;

	}

}
