package semantic;

import java.io.Serializable;

public class Arrayid extends Id implements Serializable{
int dimensions;

public Arrayid(String idname, String type, String uniqueAddress, String kind,
		int dimensions) {
	super(idname, type, uniqueAddress, kind);
	this.dimensions = dimensions;
}


@Override
	public String toString() {
		// TODO Auto-generated method stub
	String s="name:"+idname+"type"+type+"kind"+kind+"dimensions"+dimensions;
		return s;
	}

}
