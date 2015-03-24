package lexical;

public class Token {
public StringBuffer name;
public StringBuffer value;
public String type;
public Location location;
@Override
	public boolean equals(Object arg0) {
		// TODO Auto-generated method stub
	Token t=(Token)arg0;
	
			
		return value.toString().equals(t.value.toString());
	}
}


