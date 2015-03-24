package semantic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedHashMap;


public class SymbolT implements Serializable {
	public LinkedHashMap<String,Id> table = new LinkedHashMap<String, Id>();
	String tablename;
	public SymbolT(String tablename) {
		super();
		this.tablename = tablename;
		
		
	}
	public void create()
	{
		
		table=new LinkedHashMap<String, Id>();
	}
	public boolean search (SymbolT Tn,Id i, boolean found)
	{
	Id j=Tn.table.get(i.idname);
	if(j!=null)
	{
		found =true;
		Tn.table.remove(i.idname);
	}
	else
	{
		return false;
	}
	
	
	return found;
	}
	public SymbolT delete(SymbolT tn)
	{
		return null;
	}
	
	public void print(SymbolT tn) throws Exception
	{
	        FileWriter f=new FileWriter(new File(System.getProperty("user.dir")+"/symboltable/"+tn.tablename+".txt"));
	        f.write(tn.toString());
	        f.flush();
	        f.close();
		
	}
	public void insert()
	{
		
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String s=tablename;
		for(Id i : table.values())
		{
			if(i instanceof ClassId)
			s+=((ClassId)i).toString();
			if(i instanceof Arrayid)
			s+=((Arrayid)i).toString();
			if(i instanceof Id)
			s+=((Id)i).toString();
			if(i instanceof FunctionId)
			s+=((FunctionId)i).toString();
			
			
		}
		
		return s;
	}
	
	
}
