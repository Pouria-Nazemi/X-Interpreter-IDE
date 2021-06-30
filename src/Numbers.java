
import java.util.HashMap;

public class Numbers {

    private static HashMap<String, Number> variables = new HashMap<String,Number>();
    protected String name;
    protected Number value;
    protected int lineNumber;

    protected Numbers(String name, Number value,int lineNumber) {
        this.setName(name);
        this.setValue(value);
        this.setLineNumber(lineNumber);
        Numbers.getVariables().put(this.getName(), this.getValue());
    }

    public static HashMap<String, Number> getVariables() {
        return variables;
    }

    public static void changeVariableValue(String name, Number value,int lineNumber) {
        if (Numbers.variables.get(name) instanceof Integer && value instanceof Float) {
            throw new RuntimeException("You cannot assign a float value to an integer variable" + " At line: " + lineNumber);
        } else {
            Numbers.variables.put(name, value);
        }
    }

    private String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    private void setValue(java.lang.Number value) {
        this.value = value;
    }

    private Number getValue() {
        return value;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }
    

}
