package Statements;

import Interpreter.InterpretingLineException;

public abstract class Statement implements Runnable {
    
    protected int lineNumber;

    protected int getLineNumber() {
        return lineNumber;
    }

    protected void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public static Number variableGetValue(String var,int lineNumber) { //for returning the value of a String which is variable
        try {
            int variable = Integer.parseInt(var);
            return variable;
        } catch (NumberFormatException ex) {
            try {
                float variable = Float.parseFloat(var);
                return variable;
            } catch (NumberFormatException exp) {
                if (Variables.Numbers.getVariables().get(var) instanceof Integer) {
                    return (Variables.Numbers.getVariables().get(var).intValue());
                } else if (Variables.Numbers.getVariables().get(var) instanceof Double || Variables.Numbers.getVariables().get(var) instanceof Float) {
                    return (Variables.Numbers.getVariables().get(var).floatValue());
                }
            }
        }
        throw new InterpretingLineException("Wrong variable used " , lineNumber);
    }

    @Override
    public abstract Number run();
}
