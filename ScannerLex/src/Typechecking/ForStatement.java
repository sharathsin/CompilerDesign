package Typechecking;

import semantic.Id;

public class ForStatement implements Statment {
Id d;
Expression exp,exp1;
ArithExp a1,a2;
Variable v1;
StatBlock s1;
public boolean isValid()
{
return a1.evaluate().equals(a2.evaluate())& (d.getType().equals(exp)& v1.evaluate().equals(exp1));	

}



}
