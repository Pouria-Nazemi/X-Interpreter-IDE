
public abstract class Statement implements Runnable {

    public Number variableGetValue(String var) {
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
        throw new RuntimeException("VariableGetValue method problem" + LineReader.getLinePointer());
    }

    @Override
    public abstract Number run();

}
