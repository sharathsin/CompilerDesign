package semantic;

public class ClassId  extends Id{
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

}
