package parser;

import java.io.IOException;

public class ParserDriver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	Parser p=new Parser();
	boolean b=p.parseFile();

	if(b){
	System.out.println("NoErrors");
	try {
		p.Gst.print(p.Gst);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	try {
		p.bf1.flush();
		p.bfe.flush();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		p.bf1.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}

}
