package Typechecking;

import java.util.ArrayList;

public class StatementList {
	int line;
public ArrayList<Statment>s;
public String function;
public String classname;
public StatementList()
{
	s=new ArrayList<Statment>();
}
public void set()
{
	Names.classname=classname;
	Names.functionname=function;
}
public boolean  evaluate()
{
	boolean b=true;
for(Statment s1:s)
{
if(s1 instanceof ForStatement)
{
	if(! ((ForStatement) s1).isValid())
	{
		b=false;
	}
}
else if(s1 instanceof getStatement)
{
	if(! ((getStatement) s1).validate())
	{
		b=false;
	}
}
else if(s1 instanceof putStatement)
{
	if(! ((putStatement) s1).validate())
	{
		b=false;
	}
}
else if(s1 instanceof AssignStatement)
{
	if(! ((AssignStatement) s1).evaluate())b=false;
}
else{
	if(! ((returnStatement) s1).evaulate())b=false;
}


}
return b;
}


}
