package Statements;

import Interpreter.InterpretingLineException;

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

    private int getRepeatValue() {
        return repeatValue;
    }

    private void setRepeatValue(String repeatValue) {
        int repeatTime;
        if (Statement.variableGetValue(repeatValue, super.getLineNumber()) instanceof Integer) {/*getting the value of repeat
        value of 'for' command and it must be an Integer*/
            repeatTime = (int) Statement.variableGetValue(repeatValue, super.getLineNumber());
            if(repeatTime<=0){
                throw new InterpretingLineException("Wrong declaration of repeat value of for command,it should be a number" +
                        " bigger than zero " , super.getLineNumber());
            }
            this.repeatValue = repeatTime;
        } else {
            throw new InterpretingLineException("Wrong declaration of repeat value of for command " , super.getLineNumber());
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
