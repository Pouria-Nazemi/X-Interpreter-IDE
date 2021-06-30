
public class Calculation extends Statement {

    private Number var1;
    private Number var2;
    private char operator;
    private Statement calcIns = null;

    protected Calculation(String var1, String operator, String var2,int lineNumber) {
        super.setLineNumber(lineNumber);
        this.setVar1(super.variableGetValue(var1,super.getLineNumber()));
        this.setVar2(super.variableGetValue(var2,super.getLineNumber()));
        this.setOperator(operator);
        if (this.getOperator() == '+') {
            calcIns = new Addition(this.getVar1(),this.getVar2());
        } else if (this.getOperator() == '-') {
            calcIns = new Subtraction(this.getVar1(),this.getVar2());
        } else if (this.getOperator() == '*') {
            calcIns = new Multiplication(this.getVar1(),this.getVar2());
        } else if (this.getOperator() == '/') {
            calcIns = new Division(this.getVar1(),this.getVar2());
        }
    }

    public void setVar1(Number var1) {
        this.var1 = var1;
    }

    public void setVar2(Number var2) {
        this.var2 = var2;
    }

    public Number getVar1() {
        return var1;
    }

    public Number getVar2() {
        return var2;
    }

    public void setOperator(String operatorChar) {
        String operator = operatorChar;
        if (operator.equals("+") || operator.equals("-") || operator.equals("/") || operator.equals("*")) {
            this.operator = operatorChar.charAt(0);
        } else {
            throw new RuntimeException("Invalid calculation operator " + "At line: " + super.lineNumber);
        }

    }

    public char getOperator() {
        return operator;
    }

    public Statement getCalcIns() {
        return calcIns;
    }

    @Override
    public  Number run() {
        return null;
    }
}
