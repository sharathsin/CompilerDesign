package Typechecking;

public class putStatement implements Statment {
	Expression e;
	public boolean validate()
	{
		return !e.evaluate().equals("Invalid");
	}

}
