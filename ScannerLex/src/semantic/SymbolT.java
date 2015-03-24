package semantic;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedHashMap;


public class SymbolT {
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
	     FileOutputStream fos = new FileOutputStream(System.getProperty("user.dir")+"/symboltable/"+tn.tablename+".txt");
	        ObjectOutputStream out = new ObjectOutputStream(fos);
	        out.writeObject(tn.table);
	        out.close();
		
	}
	public void insert()
	{
		
	}
	
	
	
}
