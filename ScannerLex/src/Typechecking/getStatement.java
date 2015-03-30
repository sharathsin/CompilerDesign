package Typechecking;

public class getStatement implements Statment {
	public Variable e;int line;
	public boolean validate()
	{
		return !e.evaluate().equals("Invalid");
	}
}
