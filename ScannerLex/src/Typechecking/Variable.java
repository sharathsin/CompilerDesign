package Typechecking;

import java.util.ArrayList;

import parser.Parser;
import semantic.Arrayid;
import semantic.ClassId;
import semantic.FunctionId;
import semantic.Id;
import semantic.SymbolT;
import state.parseState;

public class Variable {
	public ArrayList<IdList> a;
	int line;

	public String evaluate() throws NullPointerException {
		if(a==null)
		{
			int h=0;
		}
		if(a.get(0).id.equals("value"))
		{
			line =line-1;
			
		}
		String s = "Invalid";
		IdList a1 = a.get(0);
		ClassId c;
		FunctionId f;
		if(Names.classname!=null)
		{
		 c = (ClassId) Parser.Gst.table.get(Names.classname);
			f = (FunctionId) c.table.table.get(Names.functionname);
		}
		else{
			f = (FunctionId) Parser.Gst.table.get(Names.functionname);
		}
		
			ArrayList<Id> d1 = f.getParameters();
			Id d = null;
			if (d1 != null&& d1.size()>0) {
				for (Id u : d1) {
					if (u.getIdname().equals(a1.id)) {

						d = u;

					}

				}

				if (d != null) {
					if (d instanceof ClassId) {
						for (int i = 1; i < a.size(); i++) {
							IdList d13 = a.get(i);
							String s1 = d13.evalString(d.getIdname());
							if (s1.equals("Invalid")) {
								Parser.write1(d13.id + "not declared at  "
										+ d13.line);
								return "Invalid";
							} else {
								if (!s1.contains("*") & !s1.contains("int")
										& !s1.contains("float")) {
									d = Parser.Gst.table.get(s1);

								} else {
									return s1;
								}
							}

						}
					} else if (d instanceof Arrayid) {
						if ((!d.getType().equals("int") & !d.getType().equals(
								"float"))) {
							for (int i = 1; i < a.size(); i++) {
								IdList d13 = a.get(i);
								String s1 = d13.evalString(d.getType());
								if (s1.equals("Invalid")) {
									Parser.write1(d13.id + "not declared at  "
											+ d13.line);
									return "Invalid";
								} else {
									if (!s1.contains("*") & !s1.contains("int")
											& !s1.contains("float")) {
										d = Parser.Gst.table.get(s1);
									} else {
										return s1;
									}
								}

							}

						} else {
							String s1 = a.get(0).evalString(d.getType());
							if (s1.equals("Invalid")) {
								Parser.write1(a.get(0).id
										+ "is not declared no such variable at"
										+ a.get(0).line);
							}
							return s1;
						}
					}
				} else {
					SymbolT t = f.getSymbolList();
					d = t.table.get(a1.id);
					if (t.table.get(a1.id) != null) {
						if (d instanceof ClassId) {
							for (int i = 1; i < a.size(); i++) {
								IdList d13 = a.get(i);
								String s1 = d13.evalString(d.getIdname());
								if (s1.equals("Invalid")) {
									Parser.write1(d13.id + "not declared at  "
											+ d13.line);
									return "Invalid";
								} else {
									if (!s1.contains("*") & !s1.contains("int")
											& !s1.contains("float")) {
										d = Parser.Gst.table.get(s1);

									} else {
										return s1;
									}
								}
							}
						} else if (d instanceof Arrayid) {

							if ((!d.getType().equals("int") & !d.getType()
									.equals("float"))) {

								for (int i = 1; i < a.size(); i++) {
									IdList d13 = a.get(i);
									String s1 = d13.evalString(d.getType());
									if (s1.equals("Invalid")) {
										Parser.write1(d13.id
												+ "not declared at  "
												+ d13.line);
										return "Invalid";
									} else {
										if (!s1.contains("*")
												& !s1.contains("int")
												& !s1.contains("float")) {
											d = Parser.Gst.table.get(s1);
										} else {
											return s1;
										}
									}
								}
							} else {
								String s1 = a.get(0).evalString(d.getType());
								if (s1.equals("Invalid")) {
									Parser.write1(a.get(0).id
											+ "is not declared no such variable at"
											+ a.get(0).line);
								}
								return s1;
							}
						}
					}
				}
			}

			else {
				SymbolT t = f.getSymbolList();
				d = t.table.get(a1.id);
				if (d!= null) {
					if (d instanceof ClassId) {
						for (int i = 1; i < a.size(); i++) {
							IdList d13 = a.get(i);
							String s1 = d13.evalString(d.getIdname());
							if (s1.equals("Invalid")) {
								Parser.write1(d13.id + "not declared at  "
										+ d13.line);
								return "Invalid";
							} else {
								if (!s1.contains("*") & !s1.contains("int")
										& !s1.contains("float")) {
									d = Parser.Gst.table.get(s1);

								} else {
									return s1;
								}
							}
						}
					} else if (d instanceof Arrayid) {

						if ((!d.getType().equals("int") & !d.getType()
								.equals("float"))) {

							for (int i = 1; i < a.size(); i++) {
								IdList d13 = a.get(i);
								String s1 = d13.evalString(d.getType());
								if (s1.equals("Invalid")) {
									Parser.write1(d13.id
											+ "not declared at  "
											+ d13.line);
									return "Invalid";
								} else {
									if (!s1.contains("*")
											& !s1.contains("int")
											& !s1.contains("float")) {
										d = Parser.Gst.table.get(s1);
									} else {
										return s1;
									}
								}
							}
						} else {
							String s1 = a.get(0).evalString(d.getType());
							if (s1.equals("Invalid")) {
								Parser.write1(a.get(0).id
										+ "is not declared no such variable at"
										+ a.get(0).line);
							}
							return s1;
						}
					}
					else if(d instanceof Id)
					{
						
						Id s1 = f.getSymbolList().table.get(a.get(0).id);
						
					if (s1==null) {
						Parser.write1(a.get(0).id
								+ "is not declared no such variable at"
								+ a.get(0).line);
						return "Invalid";
					}
					return s1.getType();
					}
				}
			}
	
			Parser.write1(a.get(0).id
					+ "is not declared no such variable at"
					+ a.get(0).line);
		return s;
	}
}
