
import java.util.ArrayList;

public class Assignment extends Statement {

    private Number outcome;
    private String assignVariableName;
    private Statement calculateNeed = null;
    private ArrayList<String> expression = new ArrayList<>();

    public Assignment(String assignVariable, String val,int lineNumber) {
        super.setLineNumber(lineNumber);
        this.setVariableName(assignVariable);
        this.getExpression().add(assignVariable);
        this.getExpression().add(val);
        
    }

    public Assignment(String assignVariable, String firstVar, String operator, String secondValue,int lineNumber) {
        this.setVariableName(assignVariable);
        this.getExpression().add(assignVariable);
        this.getExpression().add(firstVar);
        this.getExpression().add(operator);
        this.getExpression().add(secondValue);
        super.setLineNumber(lineNumber);
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
        if (this.getExpression().size() == 2) {
            this.setOutcome(super.variableGetValue(this.getExpression().get(1),super.getLineNumber()));
        } else if (this.getExpression().size() == 4) {
            Calculation calculate = new Calculation(this.getExpression().get(1), this.getExpression().get(2), this.getExpression().get(3),lineNumber);
            calculateNeed = calculate.getCalcIns();
            this.setOutcome(this.calculateNeed.run());
        }
        Numbers.changeVariableValue(this.getAssignVariableName(), this.getOutcome(),super.getLineNumber());
        
        return this.getOutcome();
    }

}
