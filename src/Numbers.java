
import java.util.HashMap;

public class Numbers {

    private static HashMap<String, java.lang.Number> variables = new HashMap<String, java.lang.Number>();
    protected String name;
    protected Number value;

    protected Numbers(String name, java.lang.Number value) {
        this.setName(name);
        this.setValue(value);
        Numbers.getVariables().put(this.getName(), this.getValue());
    }

    public static HashMap<String, Number> getVariables() {
        return variables;
    }

    public static void changeVariableValue(String name, Number value) {
        if (Numbers.variables.get(name) instanceof Integer && value instanceof Float) {
            throw new RuntimeException("You cannot assigned a float value to an integer variable" + " At line: " + LineReader.getLinePointer());//TODO statement part
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

}
