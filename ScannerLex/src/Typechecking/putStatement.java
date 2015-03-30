package Typechecking;

public class putStatement implements Statment {
	public Expression e;int line;
	public boolean validate()
	{
		return !e.evaluate().equals("Invalid");
	}

}
