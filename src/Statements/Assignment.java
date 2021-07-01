package Statements;

import java.util.ArrayList;

public class Assignment extends Statement {

    private Number outcome;
    private String assignVariableName;
    private Statement calculateNeed = null; // if there is need to a calculation a calculation object will be made and set to this field
    private ArrayList<String> expression = new ArrayList<>();

    public Assignment(String assignVariable, String val,int lineNumber) {
        super.setLineNumber(lineNumber);
        this.setVariableName(assignVariable);
        this.getExpression().add(assignVariable);
        this.getExpression().add(val);
        
    }

    public Assignment(String assignVariable, String firstVar, String operator, String secondValue,int lineNumber) {
        super.setLineNumber(lineNumber);
        this.setVariableName(assignVariable);
        this.getExpression().add(assignVariable);
        this.getExpression().add(firstVar);
        this.getExpression().add(operator);
        this.getExpression().add(secondValue);
        
    }

    private void setOutcome(Number outcome) {
        this.outcome = outcome;
    }

    private void setVariableName(String VariableName) {
        this.assignVariableName = VariableName;
    }

    private Number getOutcome() {
        return outcome;
    }

    private String getAssignVariableName() {
        return assignVariableName;
    }

    private ArrayList<String> getExpression() {
        return expression;
    }

    @Override
    public Number run() {
        if (this.getExpression().size() == 2) { // In this case there is no need to any calculation
            this.setOutcome(Statement.variableGetValue(this.getExpression().get(1),super.getLineNumber()));
        } else if (this.getExpression().size() == 4) {/* In this case there is need to calculation so a calculation object
         is made and the result will be set to output*/
            Calculation calculate = new Calculation(this.getExpression().get(1), this.getExpression().get(2), this.getExpression().get(3),lineNumber);
            calculateNeed = calculate.getCalcIns();
            this.setOutcome(this.calculateNeed.run());
        }
        Variables.Numbers.changeVariableValue(this.getAssignVariableName(), this.getOutcome(),super.getLineNumber()); // Statements.For assigning the result into the variable
        
        return this.getOutcome(); //recursive amount of this statement
    }

}
