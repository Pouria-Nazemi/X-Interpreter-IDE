
public class Print extends Statement {

    private String var;

    public Print(String var,int lineNumber) {
        super.setLineNumber(lineNumber);
        this.setVar(var);
    }

    public void setVar(String var) {
        this.var = var;
    }

    @Override
    public Number run() {
        Number output = super.variableGetValue(this.var,super.getLineNumber()); //getting the value of output
        System.out.println(output);
        return String.valueOf(output).length(); //recursive amount of this statement
    }

}
