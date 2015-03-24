package state;


public class LexState {
public enum FSMState{
	Start,Finish,Float,float1,error,id1,comment,maybecomment,linecomment,multilinecoment, num, Floatnum
	
	
};


public FSMState state;
}
