package Typechecking;

import java.util.ArrayList;

public class StatBlock {
	public ArrayList<Statment> s;int line;
	public boolean  evaluate()
	{
		boolean b=true;
	for(Statment s1:s)
	{
	if(s1 instanceof ForStatement)
	{
		if(! ((ForStatement) s1).isValid())
		{
			b=false;
		}
	}
	else{
		if(! ((returnStatement) s1).evaulate())b=false;
	}


	}
	return b;
	}
}
