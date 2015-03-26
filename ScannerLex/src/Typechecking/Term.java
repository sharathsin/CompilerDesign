package Typechecking;

public class Term {
Factor f;
TE a;
public String evaluate()
{
	if(a==null)
	{
		if(f instanceof SignFactor)
		{
			return ((SignFactor)f).evaluate();
		}
		else if(f instanceof VariableFactor)
		{ 
			return ((VariableFactor)f).evaluate();
		}
		if(f instanceof numFactor)
		{
			return ((numFactor)f).evaluate();
		}
		else if(f instanceof notFact)
		{
			return ((notFact)f).evaluate();
		}
		else 
		{
		return ((ArithFactor)f).evaluate();
			
			
		}
	}
	return null;
}
}
