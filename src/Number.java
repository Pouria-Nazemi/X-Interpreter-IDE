
import java.util.HashMap;

public class Number {

    private static HashMap<String, java.lang.Number> variables = new HashMap<String, java.lang.Number>();
    protected String name;
    protected java.lang.Number value;

    protected Number(String name, java.lang.Number value) {
        this.setName(name);
        this.setValue(value);
        Number.getVariables().put(this.getName(), this.getValue());
    }

    public static HashMap<String, java.lang.Number> getVariables() {
        return variables;
    }

    public static void setVariablesElement(String name, java.lang.Number value) {
        Number.variables.put(name, value);
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

    private java.lang.Number getValue() {
        return value;
    }
}
