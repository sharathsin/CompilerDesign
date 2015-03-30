package Typechecking;

import semantic.Id;

public class ForStatement implements Statment {
Id d;
Expression exp,exp1;
public Id getD() {
	return d;
}
public void setD(Id d) {
	this.d = d;
}
public Expression getExp() {
	return exp;
}
public void setExp(Expression exp) {
	this.exp = exp;
}
public Expression getExp1() {
	return exp1;
}
public void setExp1(Expression exp1) {
	this.exp1 = exp1;
}
public ArithExp getA1() {
	return a1;
}
public void setA1(ArithExp a1) {
	this.a1 = a1;
}
public ArithExp getA2() {
	return a2;
}
public void setA2(ArithExp a2) {
	this.a2 = a2;
}
public Variable getV1() {
	return v1;
}
public void setV1(Variable v1) {
	this.v1 = v1;
}
public StatBlock getS1() {
	return s1;
}
public void setS1(StatBlock s1) {
	this.s1 = s1;
}
ArithExp a1,a2;
Variable v1;
StatBlock s1;
public boolean isValid()
{
return a1.evaluate().equals(a2.evaluate())& (d.getType().equals(exp)& v1.evaluate().equals(exp1));	

}



}
