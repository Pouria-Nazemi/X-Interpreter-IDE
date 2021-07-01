package Statements;

public class Subtraction extends Statement {

    private Number result;

    protected Subtraction(Number a,Number b) {
        this.setResult(this.subNumbers(a, b));
    }

    private void setResult(Number result) {
        this.result = result;
    }

    private Number getResult() {
        return result;
    }

    private Number subNumbers(Number a, Number b) {
         if (a instanceof Float || b instanceof Float) {
            return a.floatValue() - b.floatValue();
        } else {
            return a.intValue() - b.intValue();
        }
    }

    @Override
    public Number run() {
        return this.getResult();//recursive amount of this statement
    }
}
