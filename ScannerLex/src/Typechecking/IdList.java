package Typechecking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import parser.Parser;
import semantic.*;
public class IdList {
public String id;
public ArrayList<ArithExp> a1;
public int line;
public String evalString(String classnam)
{
	
	boolean b=false;
Id f=null;
if(classnam.equals("int")||classnam.equals("'float"))
{
	if(a1!=null)
	{
	
			for(ArithExp a: a1){
			if(!a.evaluate().equals("int")){
				return "Invalid"; 
			
			
			}
			
			
		
		else{
			return classnam;
			
		}
		
		}
		
	}
}
Collection<Id> c1= Parser.Gst.table.values();
	for(Id d: c1)
	{
		if(d.getIdname().equals(classnam))
		{
		ClassId c=(ClassId)d;
		Collection<Id>fg=c.table.table.values();
		for(Id j:fg)
			if(j.getIdname().equals(id))
			{
			b=true;
			f=j;
			}
		}
	
	
	
	
	}
	if(!b)
	{
		return"Invalid";
	}
	if(a1!=null)
	{
		if(f instanceof Arrayid)
		{
			for(ArithExp a: a1){
			if(a.evaluate().equals("int"))
			return f.getType(); 
			
			
			}
			
			
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
