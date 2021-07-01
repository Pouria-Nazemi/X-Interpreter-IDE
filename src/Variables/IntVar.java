package Variables;

public class IntVar extends Numbers{
     
    public IntVar(String name, int value, int lineNumber) {
        super(name,value,lineNumber);
    }

    public IntVar(String name, int lineNumber) {
        this(name,0,lineNumber);
    }
}
