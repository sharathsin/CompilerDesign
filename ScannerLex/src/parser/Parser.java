package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

import org.javatuples.Pair;

import Typechecking.Arith;
import Typechecking.ArithExp;
import Typechecking.ArithFactor;
import Typechecking.AssignStatement;
import Typechecking.Expression;
import Typechecking.Factor;
import Typechecking.ForStatement;
import Typechecking.IdList;
import Typechecking.IfStatement;
import Typechecking.SignFactor;
import Typechecking.StatBlock;
import Typechecking.StatementList;
import Typechecking.Statment;
import Typechecking.TE;
import Typechecking.Term;
import Typechecking.Variable;
import Typechecking.aparams;
import Typechecking.getStatement;
import Typechecking.notFact;
import Typechecking.num;
import Typechecking.numFactor;
import Typechecking.putStatement;
import Typechecking.returnStatement;
import semantic.Arrayid;
import semantic.ClassId;
import semantic.FunctionId;
import semantic.Id;
import semantic.SymbolT;
import state.LexState;
import state.LexState.FSMState;
import lexical.FSM;
import lexical.LexicalClass;
import lexical.Location;
import lexical.Token;
import lexical.Variables;

public class Parser {
	Stack<Token> stack;
	HashMap<String, ArrayList<Token>> first;
	HashMap<String, ArrayList<Token>> follow;
	ArrayList<String> term;
	ArrayList<Token> tokenStream;
	ArrayList<Token>undeclared;
	static public SymbolT Gst;
	Token lookahead;
	String type;
	FileWriter bf1,bfe;
	static int line;
	Token backup,backup1;
	ArrayList<TokenChecker> a;
	static String classn;
 boolean paramscheck,variablefun,classvariable;
	public Parser() {
		paramscheck=false;
		String[] yoken = { "if", "then", "else", "for", "class", "int", "not","numint","numfloat",
				"float", "or", "get", "put", "return","id", "==", "+", "(", "<>",
				"-", ")", "<", "*", "{", ">", "/", "}", "<=", "=", "[", ">=",
				"]", ";", ",", ".", "program" };
		term = new ArrayList<String>(Arrays.asList(yoken));
		a=new ArrayList<TokenChecker>();
		FollowandFirst f1 = new FollowandFirst();
		f1.parse();
		first = f1.s;
		follow = f1.f;
		stack = new Stack<Token>();
		LexicalClass lc = new LexicalClass();
		System.out.println("enter filename present in TestFiles folder:");
		Scanner scan = new Scanner(System.in);
		
		String file = scan.next();
		try {
			bf1=new FileWriter(new File(System.getProperty("user.dir")+"\\derv.txt"));
			bfe=new FileWriter(new File(System.getProperty("user.dir")+"\\outputs\\"+file.replace('.', '_')+"error.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		scan.close();
		tokenStream = lc.main(file);
		Token dollar = new Token();
		dollar.value = new StringBuffer("$");
		tokenStream.add( dollar);
	}

	public boolean skipErrors(ArrayList<Token> FIRST, ArrayList<Token> FOLLOW) {
		try {
			Token epsilon = new Token();
			epsilon.value = new StringBuffer("epsilon");
			if (FIRST.contains(lookahead)
					|| (FIRST.contains(epsilon) & FOLLOW.contains(lookahead)))

				return true; // no error detected, parse continues in this parsing
								// function
			else {
				write1("syntax error at  line number" + lookahead.location.line+" at "+lookahead.value);
				while (!union(FIRST, FOLLOW).contains(lookahead)) {
					lookahead = nextToken();
					if (lookahead == null)
						return true;
					if ((!FIRST.contains(epsilon)) & FOLLOW.contains(lookahead))
					 return false;
				}
			}// error detected and parsing function should be aborted
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
	 return false;
		
		}

	}

	public ArrayList<Token> union(ArrayList<Token> list1, ArrayList<Token> list2) {
		Set<Token> set = new HashSet<Token>();

		set.addAll(list1);
		set.addAll(list2);

		return new ArrayList<Token>(set);
	}

	public boolean prog()// prog -> classDeclList progBody
	{
		Gst=new SymbolT("Global");
		Gst.create();
		ArrayList<Token> sbFirst = first("prog");
		ArrayList<Token> sbFollow = follow("prog");
		ArrayList<String> nterm = new ArrayList<String>();
		nterm.add("classDeclList");
		nterm.add("progBody");
		if (!skipErrors(sbFirst, sbFollow)) {

			 return false;

		}
	ArrayList<Token > t=	firstFrom(nterm, new ArrayList<Token>());
		if (t.contains(lookahead)) {
			if (classDeclList() & progBody()) {
				write(" prog -> classDeclList progBody");
				return true;
			}
			return false;

		}

		 return false;
	}

	public boolean classDeclList() {// classDecl classDeclList | EPSILON
		ArrayList<Token> sbFirst = first("classDeclList");
		ArrayList<Token> sbFollow = follow("classDeclList");
		ArrayList<String> nterm = new ArrayList<String>();
		nterm.add("classDecl");
		nterm.add("classDeclList");
		if (!skipErrors(sbFirst, sbFollow)) {

			return false;

		}
		if (firstFrom(nterm, new ArrayList<Token>()).contains(lookahead)) {
			if (classDecl() & classDeclList()) {
				write("classDeclList->classDecl classDeclList");
				return true;
			}
			return false;

		} else if (sbFollow.contains(lookahead)) {
			write("classDeclList->epsilon");
			return true;

		}

		return false;
	}

	public boolean progBody() {// progBody -> 'program' funcBody ';' f1
		ArrayList<Token> sbFirst = first("progBody");
		ArrayList<Token> sbFollow = follow("progBody");
		ArrayList<String> nterm = new ArrayList<String>();
		Token t = new Token();
		t.value = new StringBuffer("program");
		Token t1 = new Token();
		t1.value = new StringBuffer(";");
		nterm.add("program");
		nterm.add("funcBody");
		nterm.add(";");
		nterm.add("f1");
		if (!skipErrors(sbFirst, sbFollow)) {

			return false;

		}
		if (firstFrom(nterm, new ArrayList<Token>()).contains(lookahead)) {
			boolean b=match(t);
			boolean b1=false;
			if(b)
			{
			Id c= new FunctionId("program", "function", null, "mainfunction", null, null, null);
			functionMembList=new SymbolT("program");
			
			b1=funcbody();
		((FunctionId)c).setSymbolList(functionMembList);
		Gst.table.put("program",c);
			}
			if (b &b1 & match(t1) & f1()) {
				write("progBody -> 'program' funcBody ';' f1");
				return true;
			}
			return false;

		}
		return false;
	}

	public boolean classDecl() {// classDecl -> 'class' 'id' '{' memDecList '}'
								// ';'
		variablefun=false;
		ArrayList<Token> sbFirst = first("classDecl");
		ArrayList<Token> sbFollow = follow("classDecl");
		ArrayList<String> nterm = new ArrayList<String>();
		Token t = new Token();
		t.value = new StringBuffer("class");
		Token t1 = new Token();
		t1.value = new StringBuffer("id");
		Token t2 = new Token();
		t2.value = new StringBuffer("{");
		Token t3 = new Token();
		t3.value = new StringBuffer("}");
		Token t4 = new Token();
		t4.value = new StringBuffer(";");
		nterm.add("class");
		nterm.add("id");
		nterm.add("{");
		nterm.add("memDecList");
		nterm.add("}");
		nterm.add(";");
		if (!skipErrors(sbFirst, sbFollow)) {

			return false;

		}
		if (firstFrom(nterm, new ArrayList<Token>()).contains(lookahead)) {
			boolean b=match(t);
			s=new StatementList();
			int line =backup1.location.line;
			
			boolean b1=match(t1);
			classn=backup1.name.toString();
			s.classname=classn;
			String uniqueAddress = null;
			ClassId c=new ClassId(backup1.name.toString(), "class", uniqueAddress, " class", new SymbolT(classn));
		boolean b2=	match(t2);
			Derivation d;
			epsilon=false;
			d=memDecList(c);
			c=(ClassId) d.id;
			if (b & b1 & b2 & d.d
					& match(t3)
					& match(t4)) {
				write("classDecl -> 'class' 'id' '{' memDecList '}'';'");
				if(!Gst.table.containsKey(c.getIdname()))
				Gst.table.put(c.getIdname(), c);
				else{
					write1("Error Class"+c.getIdname()+"Multiple Declarations at line no"+line);
					return false;
				}
				return true;
			}
			
			return false;
			

		}
		return false;
	}
static boolean epsilon;
	public Derivation memDecList(Id c) {// memDecList -> memDec memDecList | EPSILON
		ArrayList<Token> sbFirst = first("memDecList");
		ArrayList<Token> sbFollow = follow("memDecList");
		ArrayList<String> nterm = new ArrayList<String>();
	Derivation d=new Derivation();
		
		
		nterm.add("memDec");
		nterm.add("memDecList");
		if (!skipErrors(sbFirst, sbFollow)) {
d.d=false;
			return d;

		}
		if (firstFrom(nterm, new ArrayList<Token>()).contains(lookahead)) {
			
			d=memDec(c);
			 
			boolean b=d.d;
			if(b)
			{
				((ClassId)c).table.table.put(d.id.getIdname(),d.id);
				
				
			}
			d =memDecList(c);
			boolean b1=d.d;
			if(b1&&!epsilon)
			{
				((ClassId)c).table.table.put(d.id.getIdname(),d.id);
				
				
			}
			
			if (b &b1 ) {
				d.d=true;
				//d.id=c;
				write("memDecList -> memDec memDecList ");
				return d;
			}
			return d;

		} else if (sbFollow.contains(lookahead)) {
		write("memDecList-> epsilon");
		d.id=c;
		d.d=true;
		epsilon=true;
			return d;

		}

		return d;
	}

	public Derivation memDec(Id c) {// type 'id' memDec1

		ArrayList<Token> sbFirst = first("memDec");
		ArrayList<Token> sbFollow = follow("memDec");
		ArrayList<String> nterm = new ArrayList<String>();

		Token t1 = new Token();
		t1.value = new StringBuffer("id");
		nterm.add("type");
		nterm.add("id");
Derivation d=new Derivation();
d.d=false;
		if (!skipErrors(sbFirst, sbFollow)) {

			return d;

		}
		if (firstFrom(nterm, new ArrayList<Token>()).contains(lookahead)) {
			boolean b=type();
			String type =this.type;
			boolean b1=match(t1);
			String id=backup1.name.toString();
			d=memDec1(type,id);
			if (b &b1 & d.d) {
				write("memDec->type 'id' memDec1");
				d.d=true;
				return d;
			}
			return d;

		}

		return d;
	}
static int dime;
SymbolT functionMembList;
ArrayList<Id> flisArrayList;
	public Derivation memDec1(String type,String id) {// memDec1 -> arraySizeList ';' | '(' fParams ')'
								// funcBody ';'
		ArrayList<Token> sbFirst = first("memDec1");
		ArrayList<Token> sbFollow = follow("memDec1");
		ArrayList<String> nterm = new ArrayList<String>();
		nterm.add("arraySizeList");
		nterm.add(";");
		Token t1 = new Token();
		t1.value = new StringBuffer(";");
		Token t2 = new Token();
		t2.value = new StringBuffer("(");
		Token t3 = new Token();
		t3.value = new StringBuffer(")");
		ArrayList<String> nterm1 = new ArrayList<String>();
		nterm1.add("(");
		nterm1.add("fparams");
		nterm1.add(")");
		nterm1.add("funcBody");
		nterm1.add(";");
		Derivation d=new Derivation();
		if (!skipErrors(sbFirst, sbFollow)) {

			return d;

		}
		if (firstFrom(nterm, new ArrayList<Token>()).contains(lookahead)) {
			dime=0;
			boolean b=arraySizeList();
			if(dime!=0)
		d.id=new Arrayid(id, type, null, "'array", dime);
			else{
				d.id=new Id(id, type, null, "id");
				
			}
			if (b & match(t1)) {
				write("memDec1 -> arraySizeList ';'");
				if(!variablefun){
				d.d=true;
					return d;
				}
				else{
					write1("Syntax error you cannot create variables here at line number"+lookahead.location.line);
				}
				return d;
			}
			return d;

		}
		if (firstFrom(nterm1, new ArrayList<Token>()).contains(lookahead)) {
			d.id=new FunctionId(id, type, null, "function", null,classn , null);
			boolean b=match(t2);
			 Params p=fParams();
			((FunctionId)d.id).setParameters(p.rr);
			 
			flisArrayList=p.rr;
			functionMembList =new SymbolT(d.id.getIdname());
			  boolean c= funcbody();
				((FunctionId)d.id).setSymbolList(functionMembList);
			if ( b& p.d & match(t3) & c & match(t1)) {
		s.function=id;
				write(" memDec1 -> '(' fParams ')' funcBody ';'");
			d.d=true;
			variablefun=true;
				return d;
			}
			return d;

		}

		return d;
	}

	// f1 -> funcDef f1 | EPSILON

	public boolean f1() {

		ArrayList<Token> sbFirst = first("f1");
		ArrayList<Token> sbFollow = follow("f1");
		ArrayList<String> nterm = new ArrayList<String>();
		nterm.add("funcDef");
		nterm.add("f1");
		if (!skipErrors(sbFirst, sbFollow)) {

			return false;

		}
		if (firstFrom(nterm, new ArrayList<Token>()).contains(lookahead)) {
			if (funcdef() & f1()) {
				Gst.table.put(id_fun, (Id)new FunctionId(id_fun, type, null, "function",p1.rr , null, functionMembList));
				write("f1 -> funcDef f1");
				return true;
			}
			return false;

		} else if (sbFollow.contains(lookahead)) {
			write("f1->epsilon ");
			return true;

		}

		return false;

	}
	static String id_fun;
	static Params p1;


	// funcDef -> funcHead funcBody ';'
	public boolean funcdef() {
		ArrayList<Token> sbFirst = first("funcDef");
		ArrayList<Token> sbFollow = follow("funcDef");
		Token t1 = new Token();
		t1.value = new StringBuffer(";");
		ArrayList<String> nterm = new ArrayList<String>();
		nterm.add("funcHead");
		nterm.add("funcBody");
		nterm.add(";");
		if (!skipErrors(sbFirst, sbFollow)) {

			return false;

		}
		
		
		
		if (firstFrom(nterm, new ArrayList<Token>()).contains(lookahead)) {
			
			if (funchead() & funcbody() & match(t1)) {
				write("funcDef -> funcHead funcBody ';'");
				return true;
			}
			return false;

		}

		return false;
	}
	static StatementList s;

	// funcHead -> type 'id' '(' fParams ')'
	public boolean funchead() {
		ArrayList<Token> sbFirst = first("funcHead");
		ArrayList<Token> sbFollow = follow("funcHead");
		Token t1 = new Token();
		t1.value = new StringBuffer("id");
		Token t2 = new Token();
		t2.value = new StringBuffer("(");
		Token t3 = new Token();
		t3.value = new StringBuffer(")");
		ArrayList<String> nterm = new ArrayList<String>();
		nterm.add("type");
		nterm.add("id");
		nterm.add("(");
		nterm.add("fParams");
		nterm.add(")");

		if (!skipErrors(sbFirst, sbFollow)) {

			return false;

		}
		if (firstFrom(nterm, new ArrayList<Token>()).contains(lookahead)) {
			boolean b=type();
			boolean b1=match(t1);
			id_fun=backup1.name.toString();
			boolean b2=match(t2);
			
			functionMembList=new SymbolT(backup1.name.toString());
		p1=	fParams();
			if ( b& b1 & b2 & p1.d & match(t3)) {
				write("funcHead -> type 'id' '(' fParams ')'");
				return true;
			}
			return false;

		}

		return false;
	}

	// funcBody -> '{' f2 '}'
	public boolean funcbody() {
		classvariable=false;
		ArrayList<Token> sbFirst = first("funcBody");
		ArrayList<Token> sbFollow = follow("funcBody");
		Token t1 = new Token();
		t1.value = new StringBuffer("{");
		Token t2 = new Token();
		t2.value = new StringBuffer("}");

		ArrayList<String> nterm = new ArrayList<String>();
		nterm.add("{");
		nterm.add("f2");
		nterm.add("}");

		if (!skipErrors(sbFirst, sbFollow)) {

			return false;

		}
		if (firstFrom(nterm, new ArrayList<Token>()).contains(lookahead)) {
			if (match(t1) & f2() & match(t2)) {
				write("funcBody -> '{' f2 '}'");
				return true;
			}
			return false;

		}

		return false;
	}

	// f2 -> funcMemb f2 | EPSILON
	public boolean f2() {
		ArrayList<Token> sbFirst = first("f2");
		ArrayList<Token> sbFollow = follow("f2");
		ArrayList<String> nterm = new ArrayList<String>();
		nterm.add("funcMemb");
		nterm.add("f2");
		if (!skipErrors(sbFirst, sbFollow)) {

			return false;

		}
		if (firstFrom(nterm, new ArrayList<Token>()).contains(lookahead)) {
			if (funcMemb() & f2()) {
				write("f2 -> funcMemb f2");
				return true;
			}
			return false;

		} else if (sbFollow.contains(lookahead)) {
			write("f2->epsilon");
			return true;

		}

		return false;

	}

	/*
	 * funcMemb -> 'id' funMemb1 | 'int' funMemb2 | 'float' funMemb2 | 'if' '('
	 * expr ')' 'then' statBlock 'else' statBlock ';' | for ( type id = expr ;
	 * arithExpr relOp arithExpr ; variable = expr ) statBlock ; | 'get' '('
	 * variable ')' ';' | 'put' '(' expr ')' ';' | 'return' '(' expr ')' ';'
	 */
	public boolean funcMemb() {
		ArrayList<Token> sbFirst = first("funcMemb");
		ArrayList<Token> sbFollow = follow("funcMemb");
		Token fort = new Token();
		fort.value = new StringBuffer("for");
		
		Token then = new Token();
		then.value = new StringBuffer("then");
		Token get = new Token();
		get.value = new StringBuffer("get");
		Token put = new Token();
		put.value = new StringBuffer("put");
		Token ret = new Token();
		ret.value = new StringBuffer("return");
		Token assign = new Token();
		assign.value = new StringBuffer("=");
		Token Cpar = new Token();
		Cpar.value = new StringBuffer(")");
		Token semi = new Token();
		semi.value = new StringBuffer(";");
		Token id = new Token();
		id.value = new StringBuffer("id");
		Token integer = new Token();
		integer.value = new StringBuffer("int");
		Token real = new Token();
		real.value = new StringBuffer("float");
		Token ift = new Token();
		ift.value = new StringBuffer("if");
		Token elset = new Token();
		elset.value = new StringBuffer("else");

		Token Opar = new Token();
		Opar.value = new StringBuffer("(");
		ArrayList<String> RHS1 = new ArrayList<String>();
		RHS1.add("id");
		ArrayList<String> RHS2 = new ArrayList<String>();
		RHS2.add("int");
		ArrayList<String> RHS3 = new ArrayList<String>();
		RHS3.add("float");
		ArrayList<String> RHS4 = new ArrayList<String>();
		RHS4.add("if");
		ArrayList<String> RHS5 = new ArrayList<String>();
		RHS5.add("for");
		ArrayList<String> RHS6 = new ArrayList<String>();
		RHS6.add("get");
		ArrayList<String> RHS7 = new ArrayList<String>();
		RHS7.add("put");
		ArrayList<String> RHS8 = new ArrayList<String>();
		RHS8.add("return");
		if (!skipErrors(sbFirst, sbFollow)) {

			return false;

		}
		if (firstFrom(RHS1, new ArrayList<Token>()).contains(lookahead)) {
			boolean b=match(id);
			type=backup1.name.toString();
			
			if (b & funcMemb1()) {
				write("funcMemb -> 'id' funMemb1");
				return true;
			}
			return false;

		} else if (firstFrom(RHS2, new ArrayList<Token>()).contains(lookahead)) {
			boolean b=match(integer);
			type="int";
			if (b & funcMemb2()) {
				write("funcMemb -> 'int' funMemb2");
				if(!classvariable){
			
					
					
					return true;
				
				}
				else
				{
					write1("Syntax error you cannot declare varaibles here at line number "+(lookahead.location.line-1));
					return false;
				
				}
			}
			return false;

		} else if (firstFrom(RHS3, new ArrayList<Token>()).contains(lookahead)) {
		 type="float";
			if (match(real) & funcMemb2()) {
				
				write("funcMemb -> 'float' funMemb2");
				if(!classvariable)
					return true;
					else
					{
						write1("Syntax error you cannot declare varaibles here at line number "+(lookahead.location.line-1));
						return false;
					
					}
			}
			return false;

		} else if (firstFrom(RHS4, new ArrayList<Token>()).contains(lookahead)) {
			boolean b=match(ift);
			boolean c=match(Opar);
			Pair<Boolean, Expression> c1=expr();
			boolean d1=match(Cpar);
			boolean g2=match(then);
			Pair <Boolean ,StatBlock> d=statblock();
			boolean f=match(elset);
			
			Pair <Boolean ,StatBlock> d3=statblock();
			if (b & c& c1.getValue0() & d1
					& g2 & d.getValue0() &f 
					& d3.getValue0() & match(semi)) {
				IfStatement i=new IfStatement();
				i.setEx(c1.getValue1());
				i.setS1(d.getValue1());
				i.setS2(d3.getValue1());
				s.s.add(i);
				classvariable=true;
				write("funcMemb->if(expr)then statBlock else statBlock ;");
				return true;
			}
			return false;

		}
		
		else if (firstFrom(RHS5, new ArrayList<Token>()).contains(lookahead)) {
			boolean f=match(fort) ;
			boolean f1= match(Opar);
			boolean f2=type();
			boolean f3=match(id);
			String id1 =backup1.name.toString();
			
			boolean f4=match(assign);
			Pair<Boolean,Expression> e=expr();
			boolean f5= match(semi) ;
			Pair<Boolean,ArithExp>g=arithExpr();
			boolean f6=relOp();
			Pair<Boolean,ArithExp>g1=arithExpr();
			boolean f7=match(semi);
			Pair<Boolean,Variable>v1=variable();
			boolean f8=match(assign);
			Pair<Boolean,Expression>e2=expr();
			boolean f9=match(Cpar);
			Pair<Boolean,StatBlock>c=statblock();
			boolean f10=match(semi);
			
			
			
				
			if (f&f1 & f2 & f3 & f4
					& e.getValue0() & f5& g.getValue0() &f6
					&g1.getValue0() & f7 & v1.getValue0() & f8
					& e2.getValue0() & f9 & c.getValue0() & f10) {
				
				ForStatement i=new ForStatement();
			i.setD(new Id(id1, type, null, "id"));
			i.setA1(g.getValue1());	
			i.setA2(g1.getValue1());
			i.setExp(e.getValue1());
			i.setExp1(e2.getValue1());
			i.setS1(c.getValue1());
			i.setV1(v1.getValue1());
				s.s.add(i);
				
				
				
				write("funcMemb->for(type id =expr;arithexpr relOp arithExpr; variable = expr) statBlock  ;");
				
				classvariable=true;
				return true;
			}
			return false;

		}
		else if (firstFrom(RHS6, new ArrayList<Token>()).contains(lookahead)) {
			
			
			boolean b=match(get);
			boolean b1=match(Opar);
			Pair<Boolean,Variable>c=variable();
			boolean b2=match(Cpar);
			boolean b3=match(semi);
			if (b1&b2 &c.getValue0()& b2&b3) {
				
				getStatement g=new getStatement();
				g.e=c.getValue1();
				s.s.add(g);
				write("funcMemb->get(variable) ;");
				classvariable=true;
				return true;
			}
			return false;

		}
		else if (firstFrom(RHS7, new ArrayList<Token>()).contains(lookahead)) {
			boolean b2=match(put);
			boolean b3= match(Opar);
			Pair<Boolean,Expression>c=expr();
			boolean b4=match(Cpar);
			boolean b5=match(semi);
			if (b2&b3 &c.getValue0()& b4&b5) {
				putStatement p=new putStatement();
				p.e=c.getValue1();
				s.s.add(p);
				write("funcMemb->put(expr) ;");
				classvariable=true;
				return true;
			}
			return false;

		}
		else if (firstFrom(RHS8, new ArrayList<Token>()).contains(lookahead)) {
			boolean b=match(ret);
			boolean b1=match(Opar);
			Pair<Boolean, Expression>d=expr();
			boolean b2=match(Cpar);
			boolean b3=match(semi);
			
			if (b&b1 &d.getValue0()& b2&b3) {
				
				returnStatement r=new returnStatement();
				r.expr=d.getValue1();
				write("funcMemb->return(expr) ;");
				classvariable=true;
				return true;
			}
			return false;

		}


		return false;
	}
//funMemb1 -> funMemb2| indiceList ListTail '=' expr ';'
	public boolean funcMemb1() {
		ArrayList<Token> sbFirst = first("funMemb1");
		ArrayList<Token> sbFollow = follow("funMemb1");
		Token t=new Token();
		t.value=new StringBuffer("=");

		Token t1=new Token();
		t1.value=new StringBuffer(";");

		ArrayList<String> nterm = new ArrayList<String>();
		nterm.add("funMemb2");
		ArrayList<String> nterm1	 = new ArrayList<String>();
		nterm1.add("indiceList");

nterm1.add("ListTail");
nterm1.add("=");
nterm1.add("expr");
nterm1.add(";");
		
		if (!skipErrors(sbFirst, sbFollow)) {

			return false;

		}
		if (firstFrom(nterm, new ArrayList<Token>()).contains(lookahead)) {
			Token r=lookahead;
			if (funcMemb2() ) {
				if(!classvariable){
					write("funMemb1 -> funMemb2");
					return true;
				
				}
				else{
				write1("Syntax error you cannot declare variables here at line number "+(r.location.line));
					return false;
				
				}
			}
			return false;

		} 
		else	if (firstFrom(nterm1	, new ArrayList<Token>()).contains(lookahead)) {
			boolean b=indiceList();
			if (indiceList()&ListTail()&match(t)&expr()&match(t1) ) {
				classvariable=true;
				write("funMemb1 -> indiceList ListTail '=' expr ';'");
				return true;
			}
			return false;

		} 

		return false;
	}
//funMemb2 -> 'id' arraySizeList ';'
	public boolean funcMemb2() {
		ArrayList<Token> sbFirst = first("funMemb2");
		ArrayList<Token> sbFollow = follow("funMemb2");
		Token t=new Token();
		t.value=new StringBuffer("id");
		Token t1=new Token();
		t1.value=new StringBuffer(";");
		Token t2=new Token();
		t2.value=new StringBuffer("=");
		ArrayList<String> nterm = new ArrayList<String>();
		nterm.add("id");
		if (!skipErrors(sbFirst, sbFollow)) {

			return false;

		}
		if (firstFrom(nterm, new ArrayList<Token>()).contains(lookahead)) {
		boolean b=	match(t);
		String name =backup1.name.toString();
		dime =0;
		boolean b1=arraySizeList();
		if(dime!=0){
			functionMembList.table.put(name, new Arrayid(name, type, null, "array", dime));
			
		}
		else{
			functionMembList.table.put(name, new Id(name, type, null, "id"));
			
		}
			if (b&b1&match(t1) ) {
				write("funMemb2 -> 'id' arraySizeList ';'");
				return true;
			}
			return false;

		} 
		
	
		return false;
	}
	static ArrayList<Statment>fg;
//statementList -> statement statementList | EPSILON
	public boolean statementList() {
		ArrayList<Token> sbFirst = first("statementList");
		ArrayList<Token> sbFollow = follow("statementList");
		ArrayList<String> nterm = new ArrayList<String>();
		nterm.add("statement");
		

		if (!skipErrors(sbFirst, sbFollow)) {

			return false;
 
		}
		if (firstFrom(nterm, new ArrayList<Token>()).contains(lookahead)) {
			Pair<Boolean, Statment>s=statement();
			if (s.getValue0()&statementList() ) {
				fg.add(s.getValue1());
				write("statementList -> statement statementList");
				return true;
			}
			
			return false;

		} 
		else if(sbFollow.contains(lookahead))
		{
			write("statementList -> EPSILON");
		
			return true;
		}
		return false;

	}
	/*statement -> variable '=' expr ';'
| 'if' '(' expr ')' 'then' statBlock 'else' statBlock ';'
| for ( type id = expr ; arithExpr relOp arithExpr ; variable = expr ) statBlock ;
| 'get' '(' variable ')' ';'
| 'put' '(' expr ')' ';'
| 'return' '(' expr ')' ';'
	 * 
	 * 
	 * 
	 * 
	 * 
	 */

	public Pair<Boolean, Statment> statement() {
		Statment s;
		ArrayList<Token> sbFirst = first("statement");
		ArrayList<Token> sbFollow = follow("statement");
		Token fort = new Token();
		fort.value = new StringBuffer("for");
		
		Token then = new Token();
		then.value = new StringBuffer("then");
		Token get = new Token();
		get.value = new StringBuffer("get");
		Token put = new Token();
		put.value = new StringBuffer("put");
		Token ret = new Token();
		ret.value = new StringBuffer("return");
		Token assign = new Token();
		assign.value = new StringBuffer("=");
		Token Cpar = new Token();
		Cpar.value = new StringBuffer(")");
		Token semi = new Token();
		semi.value = new StringBuffer(";");
		Token id = new Token();
		id.value = new StringBuffer("id");
		Token integer = new Token();
		integer.value = new StringBuffer("int");
		Token real = new Token();
		real.value = new StringBuffer("float");
		Token ift = new Token();
		ift.value = new StringBuffer("if");
		Token elset = new Token();
		elset.value = new StringBuffer("else");

		Token Opar = new Token();
		Opar.value = new StringBuffer("(");
		ArrayList<String> RHS1 = new ArrayList<String>();
		RHS1.add("variable");
		RHS1.add("=");
		
		
		ArrayList<String> RHS4 = new ArrayList<String>();
		RHS4.add("if");
		ArrayList<String> RHS5 = new ArrayList<String>();
		RHS5.add("for");
		ArrayList<String> RHS6 = new ArrayList<String>();
		RHS6.add("get");
		ArrayList<String> RHS7 = new ArrayList<String>();
		RHS7.add("put");
		ArrayList<String> RHS8 = new ArrayList<String>();
		RHS8.add("return");
		if (!skipErrors(sbFirst, sbFollow)) {

			return new Pair<Boolean, Statment>(false, s);

		}
		if (firstFrom(RHS1, new ArrayList<Token>()).contains(lookahead)) {
			Pair<Boolean, Variable>v=variable();
			boolean b=match(assign);
			Pair<Boolean, Expression>e=expr();
			boolean b1=match(semi);
			if (v.getValue0()&b &e.getValue0()&b1) {
				AssignStatement s1=new AssignStatement();
				s1.v=v.getValue1();
				s1.e=e.getValue1();
				write("statement -> variable '=' expr ';'");
				return new Pair<Boolean, Statment>(true, s1);
			}
			return new Pair<Boolean, Statment>(false, null);

		}  else if (firstFrom(RHS4, new ArrayList<Token>()).contains(lookahead)) {
			boolean b=match(ift);
			boolean c=match(Opar);
			Pair<Boolean, Expression> c1=expr();
			boolean d1=match(Cpar);
			boolean g2=match(then);
			Pair <Boolean ,StatBlock> d=statblock();
			boolean f=match(elset);
			
			Pair <Boolean ,StatBlock> d3=statblock();
			if (b & c& c1.getValue0() & d1
					& g2 & d.getValue0() &f 
					& d3.getValue0() & match(semi)) {
				IfStatement i=new IfStatement();
				i.setEx(c1.getValue1());
				i.setS1(d.getValue1());
				i.setS2(d3.getValue1());
				//s.s.add(i);
				classvariable=true;
				write("funcMemb->if(expr)then statBlock else statBlock ;");
				return new Pair<Boolean, Statment>(true,i);
			}
			return false;

		}
		
		else if (firstFrom(RHS5, new ArrayList<Token>()).contains(lookahead)) {
			boolean f=match(fort) ;
			boolean f1= match(Opar);
			boolean f2=type();
			boolean f3=match(id);
			String id1 =backup1.name.toString();
			
			boolean f4=match(assign);
			Pair<Boolean,Expression> e=expr();
			boolean f5= match(semi) ;
			Pair<Boolean,ArithExp>g=arithExpr();
			boolean f6=relOp();
			Pair<Boolean,ArithExp>g1=arithExpr();
			boolean f7=match(semi);
			Pair<Boolean,Variable>v1=variable();
			boolean f8=match(assign);
			Pair<Boolean,Expression>e2=expr();
			boolean f9=match(Cpar);
			Pair<Boolean,StatBlock>c=statblock();
			boolean f10=match(semi);
			
			
			
				
			if (f&f1 & f2 & f3 & f4
					& e.getValue0() & f5& g.getValue0() &f6
					&g1.getValue0() & f7 & v1.getValue0() & f8
					& e2.getValue0() & f9 & c.getValue0() & f10) {
				
				ForStatement i=new ForStatement();
			i.setD(new Id(id1, type, null, "id"));
			i.setA1(g.getValue1());	
			i.setA2(g1.getValue1());
			i.setExp(e.getValue1());
			i.setExp1(e2.getValue1());
			i.setS1(c.getValue1());
			i.setV1(v1.getValue1());
			//	s.s.add(i);
				
				
				
				write("funcMemb->for(type id =expr;arithexpr relOp arithExpr; variable = expr) statBlock  ;");
				
				classvariable=true;
				return new Pair<Boolean, Statment>(true,i);
			}
			return new Pair<Boolean, Statment>(false, null);

		}
		else if (firstFrom(RHS6, new ArrayList<Token>()).contains(lookahead)) {
			
			
			boolean b=match(get);
			boolean b1=match(Opar);
			Pair<Boolean,Variable>c=variable();
			boolean b2=match(Cpar);
			boolean b3=match(semi);
			if (b1&b2 &c.getValue0()& b2&b3) {
				
				getStatement g=new getStatement();
				g.e=c.getValue1();
			//	s.s.add(g);
				write("funcMemb->get(variable) ;");
				classvariable=true;
				return new Pair<Boolean, Statment>(true,i);
			}
			return new Pair<Boolean, Statment>(false, null);

		}
		else if (firstFrom(RHS7, new ArrayList<Token>()).contains(lookahead)) {
			boolean b2=match(put);
			boolean b3= match(Opar);
			Pair<Boolean,Expression>c=expr();
			boolean b4=match(Cpar);
			boolean b5=match(semi);
			if (b2&b3 &c.getValue0()& b4&b5) {
				putStatement p=new putStatement();
				p.e=c.getValue1();
		//		s.s.add(p);
				write("funcMemb->put(expr) ;");
				classvariable=true;
				return new Pair<Boolean, Statment>(true,p);
			}
			return new Pair<Boolean, Statment>(false, null);

		}
		else if (firstFrom(RHS8, new ArrayList<Token>()).contains(lookahead)) {
			boolean b=match(ret);
			boolean b1=match(Opar);
			Pair<Boolean, Expression>d=expr();
			boolean b2=match(Cpar);
			boolean b3=match(semi);
			
			if (b&b1 &d.getValue0()& b2&b3) {
				
				returnStatement r=new returnStatement();
				r.expr=d.getValue1();
				write("funcMemb->return(expr) ;");
				classvariable=true;
				return new Pair<Boolean, Statment>(true,r);
			}
			return new Pair<Boolean, Statment>(false, null);

		}


		return new Pair<Boolean, Statment>(false, null);
	}
/*
 * statBlock -> '{' statementList '}' | statement | EPSILON
 */
	public Pair<Boolean,StatBlock>statblock() {
		ArrayList<Token> sbFirst = first("statBlock");
		ArrayList<Token> sbFollow = follow("statBlock");
		Token t1= new Token();
		t1.value=new StringBuffer("{");
		Token t2= new Token();
		t2.value=new StringBuffer("}");
		if (!skipErrors(sbFirst, sbFollow)) {

			return new Pair<Boolean, StatBlock>(false, null);

		}
		ArrayList<String> nterm = new ArrayList<String>();
		nterm.add("{");
		ArrayList<String> nterm1 = new ArrayList<String>();
		nterm1.add("statement");
		if (firstFrom(nterm, new ArrayList<Token>()).contains(lookahead)) {
			if (match(t1)&statementList()& match(t2) ) {
				return true;
			}
			
			return new Pair<Boolean, StatBlock>(false, null);

		} 
		else if (firstFrom(nterm1, new ArrayList<Token>()).contains(lookahead)) {
			Pair<Boolean, Statment>s=statement();
			if (s.getValue0()) {
				StatBlock s1=new StatBlock();
				s1.s=new ArrayList<Statment>();
				s1.s.add(s.getValue1());
				return new Pair<Boolean, StatBlock>(true, s1);
			}
			
			return new Pair<Boolean, StatBlock>(false, null);

		} 
		else if(sbFollow.contains(lookahead))
		{
			return new Pair<Boolean, StatBlock>(true, null);
		}
		return new Pair<Boolean, StatBlock>(false, null);
	}
	/*
	 * 
	 * 
	 * expr -> arithExpr expr1
	 * 
	 */

	public Pair<Boolean,Expression> expr() {
		ArrayList<Token> sbFirst = first("expr");
		ArrayList<Token> sbFollow = follow("expr");
		if (!skipErrors(sbFirst, sbFollow)) {

			return new Pair<Boolean, Expression>(false, null);

		}
		ArrayList<String> nterm = new ArrayList<String>();
		nterm.add("arithExpr");
		nterm.add("expr1");
		if (firstFrom(nterm, new ArrayList<Token>()).contains(lookahead)) {
			if (arithExpr()&expr1() ) {
				return true;
			}
			
			return new Pair<Boolean, Expression>(false, null);

		} 
		
		
		return new Pair<Boolean, Expression>(false, null);
	}
/*
 * 
 * 
 * expr1 -> relOp arithExpr | EPSILON
 */
	public boolean expr1() {
		ArrayList<Token> sbFirst = first("expr1");
		ArrayList<Token> sbFollow = follow("expr1");
		if (!skipErrors(sbFirst, sbFollow)) {

			return false;

		}
		ArrayList<String> nterm = new ArrayList<String>();
		nterm.add("relOp");
		if (firstFrom(nterm, new ArrayList<Token>()).contains(lookahead)) {
			Pair<Boolean, ArithExp>a=arithExpr();
			if (relOp()&a.getValue0() ) {
				return true;
			}
			
			return false;

		} 
		else if(sbFollow.contains(lookahead))
		{
			return true;
		}
		return false;
	}
/*
 * arithExpr -> term arith
 */
	public Pair <Boolean,ArithExp>arithExpr() {
		ArrayList<Token> sbFirst = first("arithExpr");
		ArrayList<Token> sbFollow = follow("arithExpr");
		if (!skipErrors(sbFirst, sbFollow)) {

			return new Pair<Boolean, ArithExp>(false, null);

		}
		ArrayList<String> nterm = new ArrayList<String>();
		nterm.add("term");
		nterm.add("arith");
		if (firstFrom(nterm, new ArrayList<Token>()).contains(lookahead)) {
			Pair<Boolean, Term>c=term();
			Pair<Boolean, Arith>a=arith();
			if (c.getValue0()&a.getValue0() ) {
				ArithExp a2=new ArithExp();
				a2.t= c.getValue1();
				a2.a=a.getValue1();
				Pair<Boolean, ArithExp> a1=new Pair<Boolean, ArithExp>(true,a2 );
				return a1;
			}
			
			return new Pair<Boolean, ArithExp>(false, null);

		} 
		else if(sbFollow.contains(lookahead))
		{
			return new Pair<Boolean, ArithExp>(true, null);
		}
		
		return new Pair<Boolean, ArithExp>(false, null);
	}
/*
 * arith -> EPSILON | addOp term arith
 */
	public Pair<Boolean,Arith> arith() {
		ArrayList<Token> sbFirst = first("arith");
		ArrayList<Token> sbFollow = follow("arith");
		if (!skipErrors(sbFirst, sbFollow)) {

			return new Pair<Boolean, Arith>(false, null);

		}
		ArrayList<String> nterm = new ArrayList<String>();
		nterm.add("addOp");
		nterm.add("term");
		nterm.add("arith");
		if (firstFrom(nterm, new ArrayList<Token>()).contains(lookahead)) {
			if (addOp()&term()&arith() ) {
				return true;
			}
			
			return new Pair<Boolean, Arith>(false, null);

		} 
		else if(sbFollow.contains(lookahead))
		{
			return new Pair<Boolean, Arith>(true, null);
		}
		return new Pair<Boolean, Arith>(false, null);

	}
/*
 * sign -> '+' | '-'
 * 
 */
	public boolean sign() {
		ArrayList<Token> sbFirst = first("arith");
		ArrayList<Token> sbFollow = follow("arith");
		if (!skipErrors(sbFirst, sbFollow)) {

			return false;

		}
		ArrayList<String> nterm = new ArrayList<String>();
		nterm.add("+");
		ArrayList<String> nterm1 = new ArrayList<String>();
		nterm1.add("-");
		Token t1=new Token();
		t1.value=new StringBuffer("+");
		Token t2=new Token();
		t2.value=new StringBuffer("-");
		if (firstFrom(nterm, new ArrayList<Token>()).contains(lookahead)) {
			if (match(t1) ) {
				return true;
			}
			
			return false;

		} 
		else if (firstFrom(nterm1, new ArrayList<Token>()).contains(lookahead)) {
			if (match(t2) ) {
				return true;
			}
			
			return false;

		} 
		
		return false;
	}
/*
 * 
 * term -> factor te
 * 
 */
	public Pair<Boolean,Term> term() {
		ArrayList<Token> sbFirst = first("term");
		ArrayList<Token> sbFollow = follow("term");
		if (!skipErrors(sbFirst, sbFollow)) {

			return new Pair<Boolean, Term>(false, null);

		}
		ArrayList<String> nterm = new ArrayList<String>();
		nterm.add("factor");
		nterm.add("te");
		if (firstFrom(nterm, new ArrayList<Token>()).contains(lookahead)) {
			if (factor()&te() ) {
				return true;
			}
			
			return new Pair<Boolean, Term>(false, null);

		} 		
		
		return new Pair<Boolean, Term>(false, null);
	}
//te -> EPSILON | multOp factor te
	public Pair<Boolean,TE> te() {
		ArrayList<Token> sbFirst = first("te");
		ArrayList<Token> sbFollow = follow("te");
		if (!skipErrors(sbFirst, sbFollow)) {

			return new Pair<Boolean, TE>(false, null);

		}
		ArrayList<String> nterm = new ArrayList<String>();
		nterm.add("multOp");
		nterm.add("factor");
		nterm.add("te");
		if (firstFrom(nterm, new ArrayList<Token>()).contains(lookahead)) {
			Pair<Boolean, Factor>f=factor();
			Pair<Boolean, TE> f1=te();
			if (multOp()&f.getValue0()&f1.getValue0() ) {
				TE r=new TE();
			r.a=f1.getValue1();
			r.f=f.getValue1();
				return new Pair<Boolean, TE>(true, r);
			}
			
			return new Pair<Boolean, TE>(false, null);

		} 		
		else if(sbFollow.contains(lookahead))
			return new Pair<Boolean, TE>(true, null);

		return new Pair<Boolean, TE>(false, null);
	}
/*
 * factor          -> ( arithExpr ) 
                 | id indiceList ListTail factor1 
                 | not factor 
                 | num 
                 | sign factor
 * 
 * 
 */
	public Pair<Boolean,Factor> factor() {

		Token t1=new Token();
		t1.value=new StringBuffer("(");
		Token t2=new Token();
		t2.value=new StringBuffer(")");
		Token t3=new Token();
		t3.value=new StringBuffer("id");
		Token t4=new Token();
		t4.value=new StringBuffer("not");
		ArrayList<Token> sbFirst = first("factor");
		ArrayList<Token> sbFollow = follow("factor");
		if (!skipErrors(sbFirst, sbFollow)) {

			return new Pair<Boolean, Factor>(false, null);

		}
		ArrayList<String>RHS1=new ArrayList<String>();
		RHS1.add("(");
		ArrayList<String>RHS2=new ArrayList<String>();
		RHS2.add("id");
		ArrayList<String>RHS3=new ArrayList<String>();
		RHS3.add("not");
		ArrayList<String>RHS4=new ArrayList<String>();
		RHS4.add("num");
		ArrayList<String>RHS5=new ArrayList<String>();
		RHS5.add("sign");
		if (firstFrom(RHS1, new ArrayList<Token>()).contains(lookahead)) {
			boolean b=match(t1);
			Pair<Boolean, ArithExp>a=arithExpr();
			if (b&a.getValue0()& match(t2) ) {
				ArithFactor f=new ArithFactor();
				f.a=a.getValue1();
				return new Pair<Boolean, Factor>(true, f);
			}
			
			return new Pair<Boolean, Factor>(false, null);

		}
		else	if (firstFrom(RHS2, new ArrayList<Token>()).contains(lookahead)) {
			if (match(t3)& indiceList()&ListTail()& factor1() ) {
				return true;
			}
			
			return new Pair<Boolean, Factor>(false, null);

		} 
		else if (firstFrom(RHS3, new ArrayList<Token>()).contains(lookahead)) {
			boolean b=match(t4);
			Pair<Boolean, Factor>b1=factor();
			if (b&b1.getValue0()) {
				notFact f=new notFact();
				f.f=b1.getValue1();
				return new Pair<Boolean, Factor>(true, f);
			}
			
			return new Pair<Boolean, Factor>(false, null);

		} 
		else if (firstFrom(RHS4, new ArrayList<Token>()).contains(lookahead)) {
			if (num() ) {
				numFactor f=new numFactor();
				f.n=new num();
				f.n.setType(num);
				return new Pair<Boolean, Factor>(true, f);
			}
			
			return new Pair<Boolean, Factor>(false, null);

		} 
		else if (firstFrom(RHS5, new ArrayList<Token>()).contains(lookahead)) {
			boolean b=sign();
			Pair<Boolean, Factor> c=factor();
			if (b&c.getValue0() ) {
			SignFactor s=new SignFactor();
			s.f=c.getValue1();
				return new Pair<Boolean, Factor>(true,s);
			}
			
			return new Pair<Boolean, Factor>(false, null);

		} 

		return new Pair<Boolean, Factor>(false, null);
	}
//factor1         -> ( aParams ) | EPSILON 
	public boolean factor1() {
		Token t1=new Token();
		t1.value=new StringBuffer("(");
		Token t2=new Token();
		t2.value=new StringBuffer(")");
		ArrayList<Token> sbFirst = first("factor1");
		ArrayList<Token> sbFollow = follow("factor1");
		if (!skipErrors(sbFirst, sbFollow)) {

			return false;

		}
		ArrayList<String>RHS1=new ArrayList<String>();
		RHS1.add("(");
		
		if (firstFrom(RHS1, new ArrayList<Token>()).contains(lookahead)) {
		Token t=backup;	
			if (match(t1)&aParams()& match(t2) ) {
				if(!t.value.toString().equals("]"))
				return true;
				else{
					write1("error missing identifier before ( at"+(lookahead.location.line));
					return false;
				}
			}
			
		
			return false;

		}
		else if(sbFollow.contains(lookahead))
		{
			return true;
		}
		
		return false;
	}
/*
 * variable -> 'id' indiceList ListTail
 */
	public Pair<Boolean,Variable> variable() {
		Token t1=new Token();
		t1.value=new StringBuffer("id");
		
		ArrayList<Token> sbFirst = first("variable");
		ArrayList<Token> sbFollow = follow("variable");
		if (!skipErrors(sbFirst, sbFollow)) {

			return new Pair<Boolean, Variable>(false, null);

		}
		ArrayList<String>RHS1=new ArrayList<String>();
		RHS1.add("id");
		if (firstFrom(RHS1, new ArrayList<Token>()).contains(lookahead)) {
			if (match(t1)&indiceList()& ListTail() ) {
				return true;
			}
			
			return new Pair<Boolean, Variable>(false, null);

		}	
		
		
		return new Pair<Boolean, Variable>(false, null);
	}
//ListTail -> idnest ListTail | EPSILON
	public boolean ListTail() {
		ArrayList<Token> sbFirst = first("ListTail");
		ArrayList<Token> sbFollow = follow("ListTail");
		if (!skipErrors(sbFirst, sbFollow)) {

			return false;

		}
		ArrayList<String>RHS1=new ArrayList<String>();
		RHS1.add("idnest");
		if (firstFrom(RHS1, new ArrayList<Token>()).contains(lookahead)) {
			if (idnest()& ListTail() ) {
				return true;
			}
			
			return false;

		}	else if(sbFollow.contains(lookahead))
		{
			return true;
		}	
		
		return false;
	}
//indiceList -> indice indiceList | EPSILON
	public boolean indiceList() {
		ArrayList<Token> sbFirst = first("indiceList");
		ArrayList<Token> sbFollow = follow("indiceList");
		if (!skipErrors(sbFirst, sbFollow)) {

			return false;

		}
		ArrayList<String>RHS1=new ArrayList<String>();
		RHS1.add("indice");
		if (firstFrom(RHS1, new ArrayList<Token>()).contains(lookahead)) {
			if (indice()& indiceList() ) {
		paramscheck=true;
				return true;
			}
			
			return false;

		}	else if(sbFollow.contains(lookahead))
		{
			paramscheck=false;
			return true;
		}	


		return false;
	}
//indice -> '[' arithExpr ']'
	public boolean indice() {
		Token t=new Token();
		t.value=new StringBuffer("[");
		Token t1=new Token();
		t1.value=new StringBuffer("]");
		ArrayList<Token> sbFirst = first("indice");
		ArrayList<Token> sbFollow = follow("indice");
		if (!skipErrors(sbFirst, sbFollow)) {

			return false;

		}
		ArrayList<String>RHS1=new ArrayList<String>();
		RHS1.add("[");
		if (firstFrom(RHS1, new ArrayList<Token>()).contains(lookahead)) {
			if (match(t)& arithExpr() &match(t1)) {
				paramscheck=true;
				return true;
			}
			
			return false;

		}	
		
		
		return false;
	}
/*
 *idnest -> '.' 'id' indiceList 
 * 
 */
	public boolean idnest() {
		Token t=new Token();
		t.value=new StringBuffer(".");
		Token t1=new Token();
		t1.value=new StringBuffer("id");
		ArrayList<Token> sbFirst = first("idnest");
		ArrayList<Token> sbFollow = follow("idnest");
		if (!skipErrors(sbFirst, sbFollow)) {

			return false;

		}
		ArrayList<String>RHS1=new ArrayList<String>();
		RHS1.add(".");
		if (firstFrom(RHS1, new ArrayList<Token>()).contains(lookahead)) {
			if (match(t) &match(t1)&indiceList()) {
				write("idnest -> '.' 'id' indiceList");
				return true;
			}
			
			return false;

		}	
		

		return false;
	}
	/*
	 *arraySizeList -> arraySize arraySizeList | EPSILON 
	 */

	public boolean arraySizeList() {
		ArrayList<Token> sbFirst = first("arraySizeList");
		ArrayList<Token> sbFollow = follow("arraySizeList");
		if (!skipErrors(sbFirst, sbFollow)) {

			return false;

		}
		ArrayList<String>RHS1=new ArrayList<String>();
		RHS1.add("arraySize");
		if (firstFrom(RHS1, new ArrayList<Token>()).contains(lookahead)) {
			if (arraySize()& arraySizeList() ) {
				dime++;
				write("arraySizeList -> arraySize arraySizeList");
				return true;
			}
			
			return false;

		}	else if(sbFollow.contains(lookahead))
		{
			write("arraySizeList -> EPSILON");
			return true;
		}	

		return false;
	}
/*
 * arraySize -> '[' 'int' ']'
 * 
 */
	static ArrayList<IdList>idlist;
	public boolean arraySize() {
		Token t=new Token();
		t.value=new StringBuffer("[");
		Token t1=new Token();
		t1.value=new StringBuffer("numint");
		Token t2=new Token();
		t2.value=new StringBuffer("]");
		
		ArrayList<Token> sbFirst = first("arraySize");
		ArrayList<Token> sbFollow = follow("arraySize");
		if (!skipErrors(sbFirst, sbFollow)) {

			return false;

		}
		ArrayList<String>RHS1=new ArrayList<String>();
		RHS1.add("[");
		if (firstFrom(RHS1, new ArrayList<Token>()).contains(lookahead)) {
			if (match(t)& match(t1)&match(t2) ) {
				write("arraySize -> '[' 'int' ']'");
				return true;
			}
			
			return false;

		}

		return false;
	}
//type -> 'int' | 'float' | 'id'
	public boolean type() {
		
		Token t=new Token();
		t.value=new StringBuffer("int");
		Token t1=new Token();
		t1.value=new StringBuffer("float");
		Token t2=new Token();
		t2.value=new StringBuffer("id");
		ArrayList<Token> sbFirst = first("type");
		ArrayList<Token> sbFollow = follow("type");
		if (!skipErrors(sbFirst, sbFollow)) {

			return false;

		}
		ArrayList<String>RHS1=new ArrayList<String>();
		RHS1.add("int");
		ArrayList<String>RHS2=new ArrayList<String>();
		RHS2.add("float");
		ArrayList<String>RHS3=new ArrayList<String>();
		RHS3.add("id");
		if (firstFrom(RHS1, new ArrayList<Token>()).contains(lookahead)) {
			if (match(t) ) {
				type="int" ;
				write("type->int");
				return true;
			}
			
			return false;

		}
		else	if (firstFrom(RHS2, new ArrayList<Token>()).contains(lookahead)) {
			if ( match(t1) ) {
				type="float";
				write("type->float");
				return true;
			}
			
			return false;

		}
		else	if (firstFrom(RHS3, new ArrayList<Token>()).contains(lookahead)) {
			if (match(t2) ) {
				type=backup1.name.toString();
				a.add(new TokenChecker(backup1.location.line,type));
				write("type->id");
				return true;
			}
			
			return false;

		}
		return false;
	}
/*
 * fParams -> type 'id' arraySizeList fParamsTailList | EPSILON
 */
	public Params fParams() {
		Token t2=new Token();
		t2.value=new StringBuffer("id");
		ArrayList<Token> sbFirst = first("fParams");
		ArrayList<Token> sbFollow = follow("fParams");
		Params p=new Params();
		p.rr=new ArrayList<Id>();
		if (!skipErrors(sbFirst, sbFollow)) {

			return p;

		}
		ArrayList<String>RHS1=new ArrayList<String>();
		RHS1.add("type");
		if (firstFrom(RHS1, new ArrayList<Token>()).contains(lookahead)) {
			Id c;
			boolean b=type();
			boolean b1=match(t2);
			String name=backup1.name.toString();
			dime=0;
			boolean d=arraySizeList();
			if(dime!=0)
			{
				c=new Arrayid(name, type, null, "array", dime);
			}
			else
			{
				c=new Id(name, type, null, "id");
			}
			p.rr.add(c);
			if (b&b1& d& fParamsTailList(p).d ) {
				write("fParams -> type 'id' arraySizeList fParamsTailList ");
				p.d=true;
				return p;
			}
			
			return p;

		}

		else if(sbFollow.contains(lookahead))
		{
			write("fParams -> Epsilon ");
			p.d=true;
			return p;
		}	
		
		
		
		
		return p; // -> type 'id' arraySizeList fParamsTailList | EPSILON
	}

	/*
	 * aParams -> expr aParamsTailList | EPSILON
	 */
	public Pair<Boolean,aparams> aParams() {
		ArrayList<Token> sbFirst = first("aParams");
		ArrayList<Token> sbFollow = follow("aParams");
		if (!skipErrors(sbFirst, sbFollow)) {

			return new Pair<Boolean, aparams>(false, null);

		}
		ArrayList<String>RHS1=new ArrayList<String>();
		RHS1.add("expr");
		if (firstFrom(RHS1, new ArrayList<Token>()).contains(lookahead)) {
			Pair<Boolean, Expression>c=expr();
			
			if (c.getValue0()& aParamsTailList() ) {
				aparams a=new aparams();
				
				write(" aParams -> expr aParamsTailList");
				return new Pair<Boolean, aparams>(true, a);
			}
			
			return new Pair<Boolean, aparams>(false, null);

		}	else if(sbFollow.contains(lookahead))
		{
			return new Pair<Boolean, aparams>(true, null);
		}	


		
		return new Pair<Boolean, aparams>(false, null); // -> expr aParamsTailList | EPSILON
	}
//fParamsTailList -> fParamsTail fParamsTailList | EPSILON
	public Params fParamsTailList(Params p) {
		ArrayList<Token> sbFirst = first("fParamsTailList");
		ArrayList<Token> sbFollow = follow("fParamsTailList");
		if (!skipErrors(sbFirst, sbFollow)) {

			return p;

		}
		ArrayList<String>RHS1=new ArrayList<String>();
		RHS1.add("fParamsTail");
		if (firstFrom(RHS1, new ArrayList<Token>()).contains(lookahead)) {
			Derivation d=fParamsTail();
			if(!p.rr.contains(d.id))
			p.rr.add(d.id);
			else
				d.d=false;
	        p=fParamsTailList(p);
			if (d.d& p.d ) {
				write("fParamsTailList -> fParamsTail fParamsTailList");
				p.d=true;
				return p;
			}
			
			return p;

		}	else if(sbFollow.contains(lookahead))
		{		write("fParamsTailList -> epsilon");
		p.d=true;
			return p;
		}	

		return p; // -> fParamsTail fParamsTailList | EPSILON
	}
//fParamsTail -> ',' type 'id' arraySizeList
	public Derivation fParamsTail() {
		Token t1=new Token();
		t1.value=new StringBuffer(",");
		Token t2=new Token();
		t2.value=new StringBuffer("id");
		ArrayList<Token> sbFirst = first("fParamsTail");
		ArrayList<Token> sbFollow = follow("fParamsTail");
		Derivation d=new Derivation();
		if (!skipErrors(sbFirst, sbFollow)) {

			return d;

		}
		ArrayList<String>RHS1=new ArrayList<String>();
		RHS1.add(",");
		if (firstFrom(RHS1, new ArrayList<Token>()).contains(lookahead)) {
			boolean b=match(t1);
			
			boolean b1=type();
			boolean b2=match(t2);
			String name=backup1.name.toString();
			dime=0;
			boolean d1= arraySizeList();
			Id c;
			if(dime!=0)
			{
			c=new Arrayid(name, type, null,"array", dime);	
			
			}
			else{
				c=new Id(name, type, null, "id");
			}
			d.id=c;
			if (b&b1&b2&d1 ) {
				write("fParamsTail -> ',' type 'id' arraySizeList");
				d.d=true;
				return d;
			}
			
			return d;

		}	else if(sbFollow.contains(lookahead))
		{
			d.d=true;
			return d;
		}	
		
		return d; // -> ',' type 'id' arraySizeList
	}
//aParamsTailList -> aParamsTail aParamsTailList | EPSILON
	public boolean aParamsTailList() {
		ArrayList<Token> sbFirst = first("aParamsTailList");
		ArrayList<Token> sbFollow = follow("aParamsTailList");
		if (!skipErrors(sbFirst, sbFollow)) {

			return false;

		}
		ArrayList<String>RHS1=new ArrayList<String>();
		RHS1.add("aParamsTail");
		if (firstFrom(RHS1, new ArrayList<Token>()).contains(lookahead)) {
			if (aParamsTail()& aParamsTailList() ) {
				write("aParamsTailList -> aParamsTail aParamsTailList ");
				return true;
			}
			
			return false;

		}	else if(sbFollow.contains(lookahead))
		{	write("aParamsTailList -> epsilon ");
			return true;
		}	
		
		
		return false; // -> aParamsTail aParamsTailList | EPSILON
	}
//aParamsTail -> ',' expr
	static ArrayList<Expression>f1;
	public boolean aParamsTail() {
		
		ArrayList<Token> sbFirst = first("aParamsTail");
		ArrayList<Token> sbFollow = follow("aParamsTail");
		if (!skipErrors(sbFirst, sbFollow)) {

			return false;

		}
		ArrayList<String>RHS1=new ArrayList<String>();
		RHS1.add(",");

		Token t = new Token();
		t.value = new StringBuffer(",");
		if (firstFrom(RHS1, new ArrayList<Token>()).contains(lookahead)) {
			boolean f=match(t);
			Pair<Boolean, Expression> e= expr();
		if (f & e.getValue0()){
		f1.add(e.getValue1());
		write("aParamsTail -> ',' expr");
			return true;
		}
		return false;
		}
		return false; // -> ',' expr
	}

	public boolean relOp() {
		ArrayList<Token> sbFirst = first("relOp");
		ArrayList<Token> sbFollow = follow("relOp");
		if (!skipErrors(sbFirst, sbFollow)) {

			return false;

		}
		Token t = new Token();
		t.value = new StringBuffer(">");
		Token t1 = new Token();
		t1.value = new StringBuffer(">=");
		Token t2 = new Token();
		t2.value = new StringBuffer("==");
		Token t3 = new Token();
		t3.value = new StringBuffer("<>");
		Token t4 = new Token();
		t4.value = new StringBuffer("<");
		Token t5 = new Token();
		t5.value = new StringBuffer("<=");
		ArrayList<String>RHS1=new ArrayList<String>();
		RHS1.add(">");
		ArrayList<String>RHS2=new ArrayList<String>();
		RHS2.add(">=");
		ArrayList<String>RHS3=new ArrayList<String>();
		RHS3.add("==");
		ArrayList<String>RHS4=new ArrayList<String>();
		RHS4.add("<>");
		ArrayList<String>RHS5=new ArrayList<String>();
		RHS5.add("<");

		ArrayList<String>RHS6=new ArrayList<String>();
		RHS6.add("<=");
		if (firstFrom(RHS1, new ArrayList<Token>()).contains(lookahead)) {
		if (match(t)){
			write("relOp -> >");
			return true;
		}
		return false;
		}
		else if (firstFrom(RHS2, new ArrayList<Token>()).contains(lookahead)) {
		 if (match(t1)){
				write("relOp -> >=");
			 return true;
		 }
		 return false;
		}
		else if (firstFrom(RHS3, new ArrayList<Token>()).contains(lookahead)) {
	 if (match(t2)){
		 write("relOp -> ==");
		 return true;
	 }
return false;
		}
		else if (firstFrom(RHS4, new ArrayList<Token>()).contains(lookahead)) {
		 if (match(t3)){
			 write("relOp -> <=");
			 return true;
		 }
		 return false;
	}
		else if (firstFrom(RHS5, new ArrayList<Token>()).contains(lookahead)) {
		 if (match(t4)){
			 write("relOp -> <>");
			 return true;
		 }
		return false;
		}
		else if (firstFrom(RHS6, new ArrayList<Token>()).contains(lookahead)) {
		 if (match(t5)){
			 write("relOp -> <");
			 return true;
		 }
		return false;
		}
		return false;
		// -> '>' | '<' | '<=' | '>=' | '<>' | '=='
	}

	public boolean addOp() {
		ArrayList<Token> sbFirst = first("addOp");
		ArrayList<Token> sbFollow = follow("addOp");
		if (!skipErrors(sbFirst, sbFollow)) {

			return false;

		}
		Token t = new Token();
		t.value = new StringBuffer("+");
		Token t1 = new Token();
		t1.value = new StringBuffer("-");
		Token t2 = new Token();
		t2.value = new StringBuffer("or");
		ArrayList<String>RHS1=new ArrayList<String>();
		RHS1.add("+");
		ArrayList<String>RHS2=new ArrayList<String>();
		RHS2.add("-");
		ArrayList<String>RHS3=new ArrayList<String>();
		RHS3.add("or");
		if (firstFrom(RHS1, new ArrayList<Token>()).contains(lookahead)) {
		if (match(t)){
		write("addOP-> +");
			return true;
		}
		return false;
		
		}
		else	if (firstFrom(RHS2, new ArrayList<Token>()).contains(lookahead)) {
		 if (match(t1)){
			
				write("addOP-> -");return true;
		 }
		}
		else	if (firstFrom(RHS3, new ArrayList<Token>()).contains(lookahead)) {
		 if (match(t2)){
				write("addOP-> or");
			 return true;
		 }
		return false;
		}
		return false;
	} // -> '+' | '-' | 'or'

	public boolean multOp() {
		ArrayList<Token> sbFirst = first("multOp");
		ArrayList<Token> sbFollow = follow("multOp");
		if (!skipErrors(sbFirst, sbFollow)) {

			return false;

		}
		Token t = new Token();
		t.value = new StringBuffer("*");
		Token t1 = new Token();
		t1.value = new StringBuffer("/");
		Token t2 = new Token();
		t2.value = new StringBuffer("and");
		ArrayList<String>RHS1=new ArrayList<String>();
		RHS1.add("*");
		ArrayList<String>RHS2=new ArrayList<String>();
		RHS2.add("/");
		ArrayList<String>RHS3=new ArrayList<String>();
		RHS3.add("and");
		if (firstFrom(RHS1, new ArrayList<Token>()).contains(lookahead)) {
		if (match(t)){
			write("multOP->*");
			return true;
			
		}
		return false;
		
		}
		else	if (firstFrom(RHS2, new ArrayList<Token>()).contains(lookahead)) {
		 if (match(t1)){
			 write("multOP->/");
			return true;
		 }
		}
		else	if (firstFrom(RHS3, new ArrayList<Token>()).contains(lookahead)) {
		 if (match(t2)){
			 write("multOP->and");
			 return true;
		 }
		return false;
		}
		return false; // -> '*' | '/' | 'and'
	}
static String num;
	public boolean num() {
		ArrayList<Token> sbFirst = first("num");
		ArrayList<Token > sbFollow = follow("num");
		if (!skipErrors(sbFirst, sbFollow)) {

			return false;

		}
		ArrayList<String>RHS1=new ArrayList<String>();
		RHS1.add("numint");
		ArrayList<String>RHS2=new ArrayList<String>();
		RHS2.add("numfloat");
		Token t = new Token();
		t.value = new StringBuffer("numint");
		Token t1 = new Token();
		t1.value = new StringBuffer("numfloat");
		if (firstFrom(RHS1, new ArrayList<Token>()).contains(lookahead)) {
		if (match(t)){
		write("num-> 'numInt'");
		num="int";
			return true;
		}
		return false;
		}
		if (firstFrom(RHS2, new ArrayList<Token>()).contains(lookahead)) {
		 if (match(t1)){
			 num="float"
			 write("num-> 'numfloat'");
			 return true;
		 }
		return false;
		}
		return false; // -> int | float
	}

	public boolean parseFile() {
if(tokenStream.size()==1)
{
	
	write1("error No program function");
	return false;
}
		lookahead = nextToken();

		Token dollar = new Token();
		dollar.value = new  StringBuffer("$");
		if (prog() & match(dollar)) {
			return true;
		}
	 return false;

	}

	public boolean match(Token t) {
		try {
			backup1=lookahead;
			if (lookahead.value.toString().equals(t.value.toString())) {
				lookahead = nextToken();
				return true;
			}
		
			write1("Syntax error at line number"
					+ lookahead.location.line + " expected  "+t.value );
			lookahead = nextToken();
			return false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
		 return false;
		}
	}

	public Token nextToken() {
		backup=lookahead;
		Token t;
		if (tokenStream.size() == 0)
			return null;
		t = tokenStream.get(0);
		tokenStream.remove(0);
if(t.location!=null)
	line=t.location.line;
		
		return t;

	}

	public ArrayList<Token> first(String nterm) {
		ArrayList<Token> s = null;

		return first.get(nterm);

	}

	public ArrayList<Token> follow(String nterm) {

		return follow.get(nterm);

	}
public void write(String s)
{
	try {
		bf1.write(s+"\n");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}
public void write1(String s)
{
	try {
		if(s!=null)
		bfe.write(s+"\n");
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}
	public ArrayList<Token> firstFrom(ArrayList<String> s, ArrayList<Token> a) {
		Token epsilon = new Token();
		epsilon.value = new StringBuffer("epsilon");
		if (s.size() == 0) {
			a.add(epsilon);
			return a;
		} else {
			String s1 = s.get(0);
			s.remove(0);
			if (term.contains(s1)) {
				Token t2 = new Token();
				t2.value = new StringBuffer(s1);
				a.add(t2);
				return a;
			} else {
				ArrayList<Token> temp =first(s1);
				for(Token t :temp)
				{
					a.add(t);
				}
				if(a.contains(epsilon)){
				a.remove(epsilon);
				
				return firstFrom(s, a);
				}
				
				return a;
			}

		}
	}

}
