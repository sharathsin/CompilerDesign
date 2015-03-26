package Typechecking;

public class AssignStatement implements Statment {
	Variable v;
	Expression e;
public boolean evaluate()
{
	return (v.evaluate()).equals(e.evaluate())&& !v.evaluate().equals("Invalid");
	
}
}
