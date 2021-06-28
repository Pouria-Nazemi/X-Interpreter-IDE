
public class FloaT extends Numbers{

    public FloaT(String name, float value,int lineNumber) {
        super(name,value,lineNumber);
    }

    public FloaT(String name,int lineNumber) {
        this(name, 0.0f,lineNumber);
    }
}
