package semantic;

public class Arrayid extends Id {
int dimensions;

public Arrayid(String idname, String type, String uniqueAddress, String kind,
		int dimensions) {
	super(idname, type, uniqueAddress, kind);
	this.dimensions = dimensions;
}




}
