package Typechecking;

public class Arith {
public Term t;
public Arith a;
public String evaluate()
{
	if(a==null)
	{
	return	t.evaluate();
		
	}
	else
	{
		if(t.evaluate().equals(a.evaluate()))
		{
			return t.evaluate();
		}
		else{
			return "invalid";
		}
	}
	
}
}
