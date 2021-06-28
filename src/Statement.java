
public abstract class Statement implements Runnable {
    
    protected int lineNumber;

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }
    
    
    public static Number variableGetValue(String var,int lineNumber) {
        try {
            int variable = Integer.parseInt(var);
            return variable;
        } catch (NumberFormatException ex) {
            try {
                float variable = Float.parseFloat(var);
                return variable;
            } catch (NumberFormatException exp) {
                if (Numbers.getVariables().get(var) instanceof Integer) {
                    return (Numbers.getVariables().get(var).intValue());
                } else if (Numbers.getVariables().get(var) instanceof Double) {
                    return (Numbers.getVariables().get(var).floatValue());
                }
            }
        }
        throw new RuntimeException("Wrong variable used " +"At line: " + lineNumber);
    }
    
    @Override
    public abstract Number run();

}
