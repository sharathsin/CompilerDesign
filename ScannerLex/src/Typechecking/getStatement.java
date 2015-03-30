package Typechecking;

public class getStatement implements Statment {
	public Variable e;
	public boolean validate()
	{
		return !e.evaluate().equals("Invalid");
	}
}
