package Variables;

import Interpreter.InterpretingLineException;

import java.util.HashMap;

public class Numbers {

    protected static HashMap<String, Number> variables = new HashMap<String,Number>();
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

    public static void changeVariableValue(String name, Number value,int lineNumber) { /*This method is for updating the
    value of variables that have been declared*/
        if (Numbers.variables.get(name) instanceof Integer && value instanceof Float) {//check if the value can be assigned to the variable
            throw new InterpretingLineException("You cannot assign a float value to an integer variable", lineNumber);
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

    private void setValue(Number value) {
        this.value = value;
    }

    private Number getValue() {
        return value;
    }

    private void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }
    

}
