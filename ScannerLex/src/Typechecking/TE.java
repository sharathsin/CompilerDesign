package Typechecking;

public class TE {
public	Factor f;
public	TE a;
	public String evaluate()
	{
	String s;
		
		if(f instanceof SignFactor)
		{
		s=	((SignFactor)f).evaluate();
		}
		else if(f instanceof VariableFactor)
		{ 
	s= ((VariableFactor)f).evaluate();
		}
		if(f instanceof numFactor)
		{
	s= ((numFactor)f).evaluate();
		}
		else if(f instanceof notFact)
		{
s= ((notFact)f).evaluate();
		}
		else 
		{
	s= ((ArithFactor)f).evaluate();
			
			
		}
		if(a==null)
		{
		return s;
		}
		else
		{
			if(a.evaluate().equals(s))
			{
				
			return s;
			
			}
			else{
				
			return "invalid";
			
			}
			
		}
	//	return s;
	}
}
