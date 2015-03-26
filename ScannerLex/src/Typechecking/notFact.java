package Typechecking;

public class notFact implements Factor {
Factor f;
public String evaluate()
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
	
}
}
