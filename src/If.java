
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

    public void setCondition(boolean condition) {
        this.condition = condition;
    }

    public boolean getCondition() {
        return condition;
    }

    public void setFirstValue(String firstValue) {
        this.firstValue = firstValue;
    }

    public void setSecondValue(String secondValue) {
        this.secondValue = secondValue;
    }

    public void setCompareOperator(String compareOperator) {
        this.compareOperator = compareOperator;
    }

    public String getFirstValue() {
        return firstValue;
    }

    public String getSecondValue() {
        return secondValue;
    }

    public String getCompareOperator() {
        return compareOperator;
    }

    private boolean checkCondition() {
        float var1 = super.variableGetValue(this.getFirstValue(),super.getLineNumber()).floatValue(); //getting the value of variables
        float var2 = super.variableGetValue(this.getSecondValue(),super.getLineNumber()).floatValue();//getting the value of variables
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
        throw new RuntimeException("Wrong compare operation in if command At line: " + super.getLineNumber());
    }

    public boolean isCondition() {
        return condition;
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
