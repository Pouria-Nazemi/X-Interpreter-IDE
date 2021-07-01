package Statements;

import Interpreter.InterpretingLineException;

import java.util.ArrayList;

public class If extends Statement {

    private ArrayList<Statement> inIfCommands = new ArrayList<>();
    private boolean condition;
    private String firstValue;
    private String secondValue;
    private String compareOperator;

    public If(String firstValue, String compareOperator, String secondValue,int lineNumber) {
        super.setLineNumber(lineNumber);
        this.setFirstValue(firstValue);
        this.setCompareOperator(compareOperator);
        this.setSecondValue(secondValue);
    }

    public ArrayList<Statement> getInIfCommands() {
        return inIfCommands;
    }

    private void setCondition(boolean condition) {
        this.condition = condition;
    }

    private boolean getCondition() {
        return condition;
    }

    private void setFirstValue(String firstValue) {
        this.firstValue = firstValue;
    }

    private void setSecondValue(String secondValue) {
        this.secondValue = secondValue;
    }

    private void setCompareOperator(String compareOperator) {
        this.compareOperator = compareOperator;
    }

    private String getFirstValue() {
        return firstValue;
    }

    private String getSecondValue() {
        return secondValue;
    }

    private String getCompareOperator() {
        return compareOperator;
    }

    private boolean checkCondition() {
        float var1 = Statement.variableGetValue(this.getFirstValue(),super.getLineNumber()).floatValue(); //getting the value of variables
        float var2 = Statement.variableGetValue(this.getSecondValue(),super.getLineNumber()).floatValue();//getting the value of variables
        if (this.getCompareOperator().length() == 2) { // check the compare operator and the condition
            switch (this.getCompareOperator()) {
                case "==":
                    return (var1 == var2);
                case "!=":
                    return (var1 != var2);
                case "<=":
                    return (var1 <= var2);
                case ">=":
                    return (var1 >= var2);
            }

        } else if (this.getCompareOperator().length() == 1) {// check the compare operator and the condition
            if (this.getCompareOperator().equals("<")) {
                return (var1 < var2);
            } else if (this.getCompareOperator().equals(">")) {
                return (var1 > var2);
            }
        }
        throw new InterpretingLineException("Wrong compare operation in if command" , super.getLineNumber());
    }


    @Override
    public Number run() {
        this.setCondition(this.checkCondition());
        if (this.getCondition()) {
            for (Statement command : this.getInIfCommands()) {
                command.run();
            }
            /*recursive amount of this statement*/
            return 1; // true case
        }
        return 0; //false case
    }

}
