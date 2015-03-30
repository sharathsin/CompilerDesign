package Typechecking;

public class IfStatement implements Statment {
Expression ex;
StatBlock s1,s2;int line;
public Expression getEx() {
	return ex;
}
public void setEx(Expression ex) {
	this.ex = ex;
}
public StatBlock getS1() {
	return s1;
}
public void setS1(StatBlock s1) {
	this.s1 = s1;
}
public StatBlock getS2() {
	return s2;
}
public void setS2(StatBlock s2) {
	this.s2 = s2;
}
public boolean evaluate()
{
return !ex.evaluate().equals("Invalid");	


}
}
