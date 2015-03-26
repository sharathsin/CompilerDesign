package Typechecking;

public class getStatement implements Statment {
	Variable e;
	public boolean validate()
	{
		return !e.evaluate().equals("Invalid");
	}
}
