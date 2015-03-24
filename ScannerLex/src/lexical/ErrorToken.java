package lexical;

public class ErrorToken extends Token {
String msg;
public ErrorToken(String msg1) {
	this.msg=msg1;
}
}
