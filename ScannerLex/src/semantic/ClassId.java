package semantic;

import java.io.Serializable;

public class ClassId  extends Id implements Serializable{
public SymbolT table;

public ClassId(String idname, String type, String uniqueAddress, String kind,
		SymbolT table) {
	super(idname, type, uniqueAddress, kind);
	this.table = table;
}

public SymbolT getTable() {
	return table;
}

public void setTable(SymbolT table) {
	this.table = table;
}
@Override
public String toString()
{
	String s=idname+"type\n"+type+"address\n"+UniqueAddress;
	for(Id i : table.table.values())
	{
		s+=i.toString();
		
	}
	
	return s;

}

}
