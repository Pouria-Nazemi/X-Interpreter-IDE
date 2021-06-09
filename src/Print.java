
public class Print extends Statement {

    private String var;

    public Print(String var) {
        //this.setVar(super.variableGetValue(var));
        this.setVar(var);
    }

    public void setVar(String var) {
        this.var = var;
    }

    @Override
    public Number run() {
        Number output = super.variableGetValue(this.var);
        System.out.println(output);
        return String.valueOf(output).length();
    }

}
