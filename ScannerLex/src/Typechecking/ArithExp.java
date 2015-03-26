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
		return null;
	}
}
