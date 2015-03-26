package Typechecking;

import java.util.ArrayList;

public class Expression {
ArrayList<ArithExp>a;
public String evaluate()
{
	

String type=null;
for(ArithExp a1: a)
{
String temp=a1.evaluate();
if(type==null)
{
	type=temp;
}
else{
	if(temp.equals("Invalid"))
	{
		type="Invalid";
	}
	if(!type.equals(temp))
	{
		type="Invalid";
	}
}


}
return type;

}
}
