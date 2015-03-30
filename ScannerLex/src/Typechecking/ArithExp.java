package Typechecking;

public class ArithExp {
	public Term t;
public	Arith a;int line;
	public String evaluate()
	{
		if(t==null)
			return "Invalid";
		if(a==null)
		{
			return t.evaluate();
		}
		else
		{
			if(a==null)
			{
				return"invalid";
			}
			if(t.evaluate().equals(a.evaluate()))
		{
			return t.evaluate();
		}
		else{
			return "Invalid";
		}
			
			
			
		}
	}
}
