package Typechecking;

import java.util.ArrayList;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import parser.Parser;
import semantic.*;
public class IdList {
public String id;
public ArrayList<ArithExp> a1;

public String evalString(String classnam)
{
	int line;
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
	if(a1!=null)
	{
		if(f instanceof Arrayid)
		{
			for(ArithExp a: a1)
			if(a.evaluate().equals("int"))
			return f.getType(); 
			
		}
		else{
			return "Invalid";
		}
		
	}
	if(b)
	{
		if(f instanceof FunctionId)
		{
			return f.getIdname() +"*"+((FunctionId)f).getClassname();
		}

		return f.getType();

	}
	else{
		return "Invalid";
	}
}

}
