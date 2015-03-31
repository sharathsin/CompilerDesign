package Typechecking;

public class returnStatement implements Statment {
public Expression expr;
int line;
public boolean evaulate()
{
	return !expr.evaluate().equals("Invalid");
}
}
