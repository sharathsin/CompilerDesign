package Typechecking;

public class ArithFactor implements Factor{
public 	ArithExp a;int line;
	public String evaluate()
	{
		return a.evaluate();
	}

}
