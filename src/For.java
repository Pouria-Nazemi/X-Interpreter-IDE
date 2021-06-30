
import java.util.ArrayList;

public class For extends Statement {

    private ArrayList<Statement> inForCommands = new ArrayList<>();
    private int repeatValue;

    public For(String repeatValue, int lineNumber) {
        super.setLineNumber(lineNumber);
        this.setRepeatValue(repeatValue);
    }

    public ArrayList<Statement> getInForCommands() {
        return inForCommands;
    }

    public int getRepeatValue() {
        return repeatValue;
    }

    public void setInForCommands(ArrayList<Statement> inForCommands) {
        this.inForCommands.addAll(inForCommands);
    }

    public void setRepeatValue(String repeatValue) {
        int repeatTime;
        if (super.variableGetValue(repeatValue, super.getLineNumber()) instanceof Integer) {/*getting the value of repeat
        value of 'for' command and it must be an Integer*/
            repeatTime = (int) super.variableGetValue(repeatValue, super.getLineNumber());
            this.repeatValue = repeatTime;
        } else {
            throw new RuntimeException("Wrong declaration of repeat value of for command " + "At line: " + super.getLineNumber());
        }

    }

    @Override
    public Number run() {
        for (int i = 0; i < this.repeatValue; i++) {
            for (int j = 0; j < this.getInForCommands().size() && this.getInForCommands().size() != 0; j++) {
                this.getInForCommands().get(j).run();
            }
        }
        return this.getRepeatValue();//recursive amount of this statement
    }

}
