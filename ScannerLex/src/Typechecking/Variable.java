package Typechecking;

import java.util.ArrayList;

import parser.Parser;
import semantic.ClassId;
import semantic.FunctionId;
import semantic.Id;
public class Variable {
ArrayList<IdList>a;
public String evaluate()
{Id d1,d2;
	IdList a1=a.get(0);
	if(Names.classname!=null){
	 ClassId d=(ClassId) Parser.Gst.table.get(Names.classname);
	FunctionId d3=(FunctionId)d.table.table.get(Names.functionname);
	d1=d3.getSymbolList().table.get(a1.id);
	ArrayList<Id>id=d3.getParameters();
	for(Id d4:id)
	{
		if(d4.getIdname().equals(a1.id))
		{
			d1=d4;
		}
	}
			if(d1==null)
			{
				return "Invalid";
					
			}	
	
	}
	else{
		FunctionId d3=(FunctionId) Parser.Gst.table.get(Names.functionname);
		d1=d3.getSymbolList().table.get(a1.id);
		ArrayList<Id>id=d3.getParameters();
		for(Id d4:id)
		{
			if(d4.getIdname().equals(a1.id))
			{
				d1=d4;
			}
		}
				if(d1==null)
				{
					return "Invalid";
				}
		
	}
	String s=d1.getType();
	for(int i=1;i<a.size();i++)
	{
	IdList ap=	a.get(i);
	if(ap.evalString(s).equals("Invalid"))
	{
		return s;
	}
	
	else{
		
	s=ap.evalString(s);
	
	
	}
		
		
		
	}
	
return s;
}
}
