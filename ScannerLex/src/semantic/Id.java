package semantic;

import java.io.Serializable;

public class Id  implements Serializable{
	public String getIdname() {
		return idname;
	}
	public void setIdname(String idname) {
		this.idname = idname;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUniqueAddress() {
		return UniqueAddress;
	}
	public void setUniqueAddress(String uniqueAddress) {
		UniqueAddress = uniqueAddress;
	}
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	String idname ;
	String type;
	
	String UniqueAddress;
	String kind;
	public Id(String idname, String type, String uniqueAddress, String kind) {
		super();
		this.idname = idname;
		this.type = type;
		UniqueAddress = uniqueAddress;
		this.kind = kind;
	}
}
