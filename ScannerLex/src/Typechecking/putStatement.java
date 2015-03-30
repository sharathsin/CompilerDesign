package Typechecking;

public class putStatement implements Statment {
	public Expression e;
	public boolean validate()
	{
		return !e.evaluate().equals("Invalid");
	}

}
