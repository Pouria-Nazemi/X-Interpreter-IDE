package Statements;

import Interpreter.InterpretingLineException;

public class Calculation extends Statement {

    private Number var1;
    private Number var2;
    private char operator;
    private Statement calcIns = null; /* This field will be assigned by one of these four commands instance: addition
    ,subtraction,multiplication or division*/

    protected Calculation(String var1, String operator, String var2,int lineNumber) {
        super.setLineNumber(lineNumber);
        this.setVar1(Statement.variableGetValue(var1,super.getLineNumber()));
        this.setVar2(Statement.variableGetValue(var2,super.getLineNumber()));
        this.setOperator(operator);  // checking the calculation operator for constructing the correct instance
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

    private void setVar1(Number var1) {
        this.var1 = var1;
    }

    private void setVar2(Number var2) {
        this.var2 = var2;
    }

    private Number getVar1() {
        return var1;
    }

    private Number getVar2() {
        return var2;
    }

    private void setOperator(String operatorChar) { // This setter check if the operator is valid or not
        String operator = operatorChar;
        if (operator.equals("+") || operator.equals("-") || operator.equals("/") || operator.equals("*")) {
            this.operator = operatorChar.charAt(0);
        } else {
            throw new InterpretingLineException("Invalid calculation operator" ,  super.lineNumber);
        }

    }

    private char getOperator() {
        return operator;
    }

    protected Statement getCalcIns() {
        return calcIns;
    }

    @Override
    public  Number run() {//TODO it won`t be used
        return null;
    }
}
