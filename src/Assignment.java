
public class Assignment extends Statement {

    private Number outcome;
    private String assignVariableName ;
    private Statement calculateNeed = null;

    public Assignment(String assignVariable, String val) {
        this.setVariableName(assignVariable);
        this.setOutcome(super.variableGetValue(val));
    }

    public Assignment(String assignVariable, String firstVar, String operator, String secondValue) {
        this.setVariableName(assignVariable);
        Calculation calculate = new Calculation(firstVar, operator, secondValue);
        calculateNeed = calculate.getCalcIns();
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

    @Override
    public Number run() {
        if (this.calculateNeed != null) {
            this.setOutcome(this.calculateNeed.run());
        }
        Numbers.changeVariableValue(this.getAssignVariableName(), this.getOutcome());

        return this.getOutcome();
    }

}
