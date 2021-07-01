package Variables;

public class FloatVar extends Numbers{

    public FloatVar(String name, float value, int lineNumber) {
        super(name,value,lineNumber);
    }

    public FloatVar(String name, int lineNumber) {
        this(name, 0.0f,lineNumber);
    }
}
