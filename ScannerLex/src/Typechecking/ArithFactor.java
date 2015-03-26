package Typechecking;

public class ArithFactor implements Factor{
	ArithExp a;
	public String evaluate()
	{
		return a.evaluate();
	}

}
