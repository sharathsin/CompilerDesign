package semantic;

import java.io.Serializable;
import java.util.ArrayList;



public class FunctionId extends Id implements Serializable{
	ArrayList<Id> parameters;
	String classname;
	SymbolT symbolList;
	public FunctionId(String idname, String type, String uniqueAddress,
			String kind, ArrayList<Id> parameters, String classname,
			SymbolT symbolList) {
		super(idname, type, uniqueAddress, kind);
		this.parameters = parameters;
		this.classname = classname;
		this.symbolList = symbolList;
	}
	public ArrayList<Id> getParameters() {
		return parameters;
	}
	public void setParameters(ArrayList<Id> parameters) {
		this.parameters = parameters;
	}
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	public SymbolT getSymbolList() {
		return symbolList;
	}
	public void setSymbolList(SymbolT symbolList) {
		this.symbolList = symbolList;
	}
	
	
	
	

}
