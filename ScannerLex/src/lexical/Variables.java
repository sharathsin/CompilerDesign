package lexical;
import java.io.BufferedReader;
import java.io.PushbackReader;
import java.util.ArrayList;


public class Variables {
	public static int line_number=1,column_number=0;
	public static PushbackReader br = null;
	public static int ch;
	public static ArrayList<Integer>brace;
	public static ArrayList<Integer>coments;
}
