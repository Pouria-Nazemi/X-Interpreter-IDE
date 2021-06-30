
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

    public void setOutcome(Number outcome) {
        this.outcome = outcome;
    }

    public void setVariableName(String VariableName) {
        this.assignVariableName = VariableName;
    }

    public Number getOutcome() {
        return outcome;
    }

    public String getAssignVariableName() {
        return assignVariableName;
    }

    public ArrayList<String> getExpression() {
        return expression;
    }

    @Override
    public Number run() {
        if (this.getExpression().size() == 2) { // In this case there is no need to any calculation
            this.setOutcome(super.variableGetValue(this.getExpression().get(1),super.getLineNumber()));
        } else if (this.getExpression().size() == 4) {/* In this case there is need to calculation so a calculation object
         is made and the result will be set to output*/
            Calculation calculate = new Calculation(this.getExpression().get(1), this.getExpression().get(2), this.getExpression().get(3),lineNumber);
            calculateNeed = calculate.getCalcIns();
            this.setOutcome(this.calculateNeed.run());
        }
        Numbers.changeVariableValue(this.getAssignVariableName(), this.getOutcome(),super.getLineNumber()); // For assigning the result into the variable
        
        return this.getOutcome(); //recursive amount of this statement
    }

}
