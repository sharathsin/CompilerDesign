package Typechecking;

public class ArithExp {
	Term t;
	Arith a;
	public String evaluate()
	{
		if(a==null)
		{
			return t.evaluate();
		}
		else
		{if(t.evaluate().equals(a.evaluate()))
		{
			return t.evaluate();
		}
		else{
			return "Invalid";
		}
			
			
			
		}
	}
}
