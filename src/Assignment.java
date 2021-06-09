
import java.util.ArrayList;

public class Assignment extends Statement {

    private Number outcome;
    private String assignVariableName ;
    private Statement calculateNeed = null;
    private ArrayList<String> expression = new ArrayList<>();

    public Assignment(String assignVariable, String val) {
        this.setVariableName(assignVariable);
        this.setOutcome(super.variableGetValue(val));
        this.getExpression().add(assignVariable);
        this.getExpression().add(val);
    }

    public Assignment(String assignVariable, String firstVar, String operator, String secondValue) {
        this.setVariableName(assignVariable);
        Calculation calculate = new Calculation(firstVar, operator, secondValue);
        calculateNeed = calculate.getCalcIns();
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
        if (this.calculateNeed != null) {
            this.setOutcome(this.calculateNeed.run());
        }
        Numbers.changeVariableValue(this.getAssignVariableName(), this.getOutcome());
        return this.getOutcome();
    }

}
