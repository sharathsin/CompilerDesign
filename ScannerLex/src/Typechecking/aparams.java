package Typechecking;

import java.util.ArrayList;

public class aparams {
public ArrayList<Expression>e;
int line;
public boolean evaluate(ArrayList<String>s)

{if(e.size()!=s.size())return false;
for(int i =0;i<s.size();i++)

{
	if(!e.get(i).equals(s.get(i)))
	{
		return false;
	}
}
	
	return true;
}

}
