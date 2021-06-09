
import java.util.ArrayList;

public class For extends Statement {

    private ArrayList<Statement> inForCommands = new ArrayList<>();
    private int repeatValue;

    public For(String repeatValue, ArrayList<Statement> commands) {
        this.setRepeatValue(repeatValue);
        this.setInForCommands(commands);
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
        try {
            repeatTime = Integer.parseInt(repeatValue);
            this.repeatValue = repeatTime;
        } catch (NumberFormatException ex) {
            throw new RuntimeException("Wrong declaration of repeat value of for command " + "At line: " + LineReader.getLinePointer());
        }

    }

    @Override
    public Number run() {
        for (int i = 0; i < this.repeatValue; i++) {
            for (int j = 0; j < this.getInForCommands().size() && this.getInForCommands().size() != 0; j++) {
                Statement command = null;
                if (this.getInForCommands().get(j) instanceof Assignment) {
                    Assignment assign = (Assignment) this.getInForCommands().get(j);
                    if(assign.getExpression().size()==4){
                        command = new Assignment(assign.getExpression().get(0),assign.getExpression().get(1),assign.getExpression().get(2),assign.getExpression().get(3));
                    }
                    else if(assign.getExpression().size()==2){
                        command = new Assignment(assign.getExpression().get(0),assign.getExpression().get(1));
                    }
                } else {
                    command = this.getInForCommands().get(j);
                }
                command.run();
            }
        }
        return this.getRepeatValue();//TODO change the output later
    }

}
