package Typechecking;

public class AssignStatement implements Statment {
public 	Variable v;
public	Expression e;
public boolean evaluate()
{
	return (v.evaluate()).equals(e.evaluate())&& !v.evaluate().equals("Invalid");
	
}
}
