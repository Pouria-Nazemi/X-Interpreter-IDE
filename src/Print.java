
public class Print extends Statement {

    private Number var;

    public Print(String var) {
        this.setVar(super.variableGetValue(var));
    }

    public void setVar(Number var) {
        this.var = var;
    }

    @Override
    public Number run() {
        System.out.println(this.var);
        return String.valueOf(var).length();
    }

}
