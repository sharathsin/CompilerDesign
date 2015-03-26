package Typechecking;

import parser.Parser;
import semantic.Arrayid;
import semantic.ClassId;
import semantic.Id;
import semantic.*;
public class IdList {
String id,classn,func;
ArithExp a;

public String evalString(String classnam)
{
	
	boolean b=false;
Id f=null;
	for(Id d: Parser.Gst.table.values())
	{
		if(d.getIdname().equals(classnam))
		{
		ClassId c=(ClassId)d;
		for(Id j:c.table.table.values())
			if(j.getIdname().equals(id))
			{
			b=true;
			f=j;
			}
		}
	
	
	
	
	}
	if(!b)
	{
		return"invalid";
	}
	if(a!=null)
	{
		if(f instanceof Arrayid)
		{
			if(a.evaluate().equals("int"))
			return f.getType(); 
			
		}
		else{
			return "Invalid";
		}
		
	}
	if(b)
	{
return f.getType();
	}
	else{
		return "Invalid";
	}
}

}
